package manager;

import data.entity.Word;
import data.repository.BaseRepository;
import data.repository.SavedWordRepository;
import data.repository.WrongWordRepository;
import enums.FilePath;
import util.UserValidator;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class FileManager {

    private static final int NOT_EXIST = -1;
    private static final String EMPTY_STRING = "";
    private static final int WRONG_ANSWER_COUNT_LIMIT = 10;

    public static File getFile(FilePath fp) {
        File file = new File(getCurrentPath(), fp.getPath());
        exitIfNotExist(file);
        return file;
    }

    public static ArrayList<File> getAllWrongFileNames(){
        ArrayList<File> list = new ArrayList<>();
        File userFile = getFile(FilePath.USER_INFO);
        // userfile.txt는 무결성 체크 안 해도 됨.
        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.trim().isEmpty()) continue;
                String[] parts = line.split(":", 2);
                String eachUserId = parts[0].trim();
                String wrongFileName = eachUserId + FilePath.WRONG_ANSWERS.getPath();
                File wrongFile = new File(getCurrentPath(),wrongFileName);
                // 존재하면 넣기. 유저파일에서 아이디 조회 => 실제로 없다면, 리스트에 넣지 않는다.
                if(wrongFile.exists()){
                    list.add(wrongFile);
                }
            }
        } catch (Exception e) {
            exitProgram();
        }
        // test용
        list.forEach(file -> System.out.println(file.getAbsolutePath()));
        return list;
    }

    public static String getCurrentPath(){
        String currentPath = System.getProperty("user.home");
        if (currentPath == null || currentPath.trim().isEmpty()) {
            System.out.print("!!! 오류: 홈 경로를 파악할 수 없습니다! ");
            exitProgram();
        }
        return currentPath;
    }

    private static void exitIfNotExist(File file) {
        if (!file.exists()) {
            System.out.print("!!! 오류: 홈 경로에 "+file.getName()+" 데이터 파일이 없습니다! ");
            exitProgram();
        }
    }

    public static void checkFileAuthority(File file) {
        boolean exit = false;
        if (!file.exists() || !file.isFile() || !file.canRead() || !file.canWrite()) {
            exit = true;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
        } catch (Exception e) {
            exit = true;
        }
        if (exit) {
            System.out.print("!!! 오류: 데이터 파일\n" + file.getAbsolutePath() + "\n에 대한 입출력 권한이 없습니다! ");
            exitProgram();
        }
    }

    /**
     * 파일 유형별 무결성 검사를 수행한다. 기존 checkFileIntegrity(File)는 유지하고,
     * 새로운 규칙이 필요한 경우 아래 전용 메소드를 호출하도록 한다.
     */
    public static void checkFileIntegrity(File file, FilePath fp) {
        switch (fp) {
            case WORDS -> checkWordFileIntegrity(file);
            case WRONG_ANSWERS -> checkWrongAnswerFileIntegrity(file);
            case USER_INFO -> checkUserFileIntegrity(file);
            default -> throw new IllegalArgumentException("지원하지 않는 파일 타입: " + fp);
        }
    }

    /**
     * WORDS 파일 무결성 검사.
     */
    private static void checkWordFileIntegrity(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            ArrayList<String> errorLineList = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // 빈 행 무시

                // 콜론( :) 이 정확히 한 번만 등장해야 함
                if (line.indexOf(':') == NOT_EXIST || line.indexOf(':') != line.lastIndexOf(':')) {
                    errorLineList.add(line);
                    continue;
                }

                String[] parts = line.split(":", 2);
                String word = parts[0].trim();
                String explanation = parts[1].trim();

                // 단어 검증
                if (word.length() > 50 || word.equals(EMPTY_STRING) || !word.matches("^[A-Za-z]+$")) {
                    errorLineList.add(line);
                    continue;
                }

                // 설명 검증 (영문 + 공백, 255자 이하)
                if (explanation.length() > 255 || explanation.equals(EMPTY_STRING) || !explanation.matches("^[A-Za-z ]+$")) {
                    errorLineList.add(line);
                }
            }

            if (!errorLineList.isEmpty()) {
                System.out.println(file.getName() + " 파일의 중대한 결함 문제: 다음의 행이 문법 형식에 위배됩니다.");
                errorLineList.forEach(System.out::println);
                System.out.println("프로그램을 종료합니다.");
                System.exit(1);
            }
        } catch (Exception e) {
            exitProgram();
        }
    }

    /**
     * WRONG_ANSWERS 파일 무결성 검사.
     */
    private static void checkWrongAnswerFileIntegrity(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            ArrayList<String> errorLineList = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // 빈 행 무시

                // 콜론 2개인지 검사
                int firstColon = line.indexOf(':');
                int lastColon = line.lastIndexOf(':');
                if (firstColon == NOT_EXIST || firstColon == lastColon ||
                        line.chars().filter(ch -> ch == ':').count() != 2) {
                    errorLineList.add(line);
                    continue;
                }

                String[] parts = line.split(":", 3);
                if (parts.length != 3) { // 안전망
                    errorLineList.add(line);
                    continue;
                }

                String word = parts[0].trim();
                String explanation = parts[1].trim();
                String countStr = parts[2].trim();

                // 단어 검증
                if (word.length() > 50 || word.equals(EMPTY_STRING) || !word.matches("^[A-Za-z]+$")) {
                    errorLineList.add(line);
                    continue;
                }

                // 설명 검증
                if (explanation.length() > 255 || explanation.equals(EMPTY_STRING) || !explanation.matches("^[A-Za-z ]+$")) {
                    errorLineList.add(line);
                    continue;
                }

                // count 검증
                if (!countStr.matches("^\\d{1,2}$")) { // 0~99 두 자리까지 허용, 아래에서 범위 체크
                    errorLineList.add(line);
                    continue;
                }
                int count;
                try {
                    count = Integer.parseInt(countStr);
                } catch (NumberFormatException nfe) {
                    errorLineList.add(line);
                    continue;
                }
                if (count < 0 || count > WRONG_ANSWER_COUNT_LIMIT) {
                    errorLineList.add(line);
                }
            }

            if (!errorLineList.isEmpty()) {
                System.out.println(file.getName() + " 파일의 중대한 결함 문제: 다음의 행이 문법 형식에 위배됩니다.");
                errorLineList.forEach(System.out::println);
                System.out.println("프로그램을 종료합니다.");
                System.exit(1);
            }
        } catch (Exception e) {
            exitProgram();
        }
    }

    /**
     * USER_INFO 파일 무결성 검사.
     */
    private static void checkUserFileIntegrity(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            ArrayList<String> errorLineList = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                // 콜론 1개 확인
                if (line.indexOf(':') == NOT_EXIST || line.indexOf(':') != line.lastIndexOf(':')) {
                    errorLineList.add(line);
                    continue;
                }

                String[] parts = line.split(":", 2);
                if (parts.length != 2) {
                    errorLineList.add(line);
                    continue;
                }

                String field1 = parts[0].trim();
                String field2 = parts[1].trim();

                boolean valid1 = UserValidator.isValid(field1);
                boolean valid2 = UserValidator.isValid(field2);

                if (!valid1 || !valid2) {
                    errorLineList.add(line);
                }
            }

            if (!errorLineList.isEmpty()) {
                System.out.println(file.getName() + " 파일의 중대한 결함: 다음의 행이 문법 형식에 위배됩니다.");
                errorLineList.forEach(System.out::println);
                System.out.println("프로그램을 종료합니다.");
                System.exit(1);
            }
        } catch (Exception e) {
            exitProgram();
        }
    }

    public static void removeWordEntryDuplicates(File file) {
        HashMap<String, String> recordMap = new HashMap<>();
        ArrayList<String> duplicatedList = new ArrayList<>();
        ArrayList<String> filteredLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.trim().isEmpty()){
                    filteredLines.add(line);
                    continue;
                }
                String[] parts = line.split(":", 3);
                String word = parts[0].trim();
                String explanation = parts[1].trim();
                if (recordMap.containsKey(word)) {
                    duplicatedList.add("단어 " + word);
                } else if (recordMap.containsValue(explanation)) {
                    duplicatedList.add("뜻풀이 " + explanation);
                } else {
                    recordMap.put(word.toLowerCase(), explanation.toLowerCase());
                    filteredLines.add(line);
                }
            }
        } catch (Exception e) {
            exitProgram();
        }

        try {
            Files.write(file.toPath(), filteredLines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            exitProgram();
        }

        duplicatedList.forEach(w ->
                System.out.println("성공적으로 중복 데이터 레코드를 제거했습니다. 사유: 중복인 " + w)
        );
    }

    public static void removeUserEntryDuplicates(File file) {
        HashSet<String> recordSet = new HashSet<>();
        ArrayList<String> duplicatedList = new ArrayList<>();
        ArrayList<String> filteredLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.trim().isEmpty()){
                    filteredLines.add(line);
                    continue;
                }
                String[] parts = line.split(":", 3);
                String Id = parts[0].trim();
                if (recordSet.contains(Id)) {
                    duplicatedList.add("아이디 " + Id);
                } else {
                    recordSet.add(Id);
                    filteredLines.add(line);
                }
            }
        } catch (Exception e) {
            exitProgram();
        }

        try {
            Files.write(file.toPath(), filteredLines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.print("데이터 파일에 결함이 존재하므로 프로그램 작동을 정지합니다.");
            exitProgram();
        }

        duplicatedList.forEach(w ->
                System.out.println("성공적으로 중복 데이터 레코드를 제거했습니다. 사유: 중복인 " + w)
        );
    }

    public static void loadFiles(File file, BaseRepository repositoryType) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.trim().isEmpty()) continue;
                String[] parts = line.split(":", 2);
                String word = parts[0].trim();
                String explanation = parts[1].trim();
                repositoryType.addWord(Word.of(word,explanation));
            }
        } catch (Exception e) {
            exitProgram();
        }
    }
    public static void loadWordFile(File file) {
        SavedWordRepository swr = SavedWordRepository.getInstance();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(":", 2);
                String word = parts[0].trim();
                String explanation = parts[1].trim();
                swr.addWord(Word.of(word, explanation));
            }
        } catch (Exception e) {
            exitProgram();
        }
    }

    public static void loadWrongFile(File file) {
        WrongWordRepository wwr = WrongWordRepository.getInstance();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(":", 3);
                String word = parts[0].trim();
                String explanation = parts[1].trim();
                int wrongCount = Integer.parseInt(parts[2].trim());

                wwr.addWord(Word.of(word, explanation));
                wwr.addWrongCount(wrongCount);
            }
        } catch (Exception e) {
            exitProgram();
        }
    }

    private static void exitProgram() {
        System.out.println("프로그램을 종료합니다.");
        System.exit(1);
    }

    public static void deleteWrongWordsNotInWordFile(File wordFile, File wrongFile) {
        HashSet<Word> wordHashSet = new HashSet<>();
        ArrayList<String> filteredLines = new ArrayList<>();
        ArrayList<String> removedLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(wordFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.trim().isEmpty()) continue;
                String[] parts = line.split(":", 2);
                String word = parts[0].trim();
                String explanation = parts[1].trim();
                wordHashSet.add(Word.of(word,explanation));
            }
        } catch (Exception e) {
            exitProgram();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(wrongFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.trim().isEmpty()){
                    filteredLines.add(line);
                    continue;
                }
                String[] parts = line.split(":", 3);
                String word = parts[0].trim();
                String explanation = parts[1].trim();
                if (wordHashSet.contains(Word.of(word,explanation))) {
                    filteredLines.add(line);
                } else {
                    removedLines.add(line);
                }
            }
        } catch (Exception e) {
            exitProgram();
        }

        try {
            Files.write(wrongFile.toPath(), filteredLines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            exitProgram();
        }

        removedLines.forEach(w ->
                System.out.println("제거한 wrong_answers.txt에만 있는 Word: " + w)
        );
    }
}
