package manager;

import data.entity.Word;
import data.repository.BaseRepository;
import enums.FilePath;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class FileManager {

    private static final int NOT_EXIST = -1;
    private static final String EMPTY_STRING = "";

    public static File getFile(FilePath fp) {
        File file = new File(getCurrentPath(), fp.getPath());
        exitIfNotExist(file);
        return file;
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

    public static void checkFileIntegrity(File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            ArrayList<String> errorLineList = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                checkLineIntegrity(line, errorLineList);
            }
            if(!errorLineList.isEmpty()){
                System.out.println("파일의 중대한 결함 문제: 다음의 행이 문법 형식에 위배됩니다.");
                errorLineList.forEach(System.out::println);
                System.out.println("프로그램을 종료합니다.");
                System.exit(1);
            }
        } catch (Exception e) {
            exitProgram();
        }
    }

    private static void checkLineIntegrity(String line, ArrayList<String> errorLineList) throws IOException {
        if(line.trim().isEmpty()) return;
        if (line.indexOf(':') == NOT_EXIST || line.indexOf(':') != line.lastIndexOf(':')) {
            errorLineList.add(line);
            return;
        }
        String[] parts = line.split(":", 2);
        String word = parts[0].trim();
        String explanation = parts[1].trim();
        if (word.length() > 50 || word.equals(EMPTY_STRING) || !word.matches("^[A-Za-z]+$")) {
            errorLineList.add(line);
            return;
        }
        if (explanation.length() > 255 || explanation.equals(EMPTY_STRING) || !explanation.matches("^[A-Za-z ]+$")) {
            errorLineList.add(line);
        }
    }

    public static void removeDuplicates(File file) {
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
                String[] parts = line.split(":", 2);
                String word = parts[0].trim();
                String explanation = parts[1].trim();
                if (recordMap.containsKey(word)) {
                    duplicatedList.add("단어 " + word);
                    continue;
                } else if (recordMap.containsValue(explanation)) {
                    duplicatedList.add("뜻풀이 " + explanation);
                    continue;
                } else {
                    recordMap.put(word, explanation);
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

    public static void createUserWrongAnswerFileIfAbsent(String userId) {
        File file = new File(userId + "_wrong_answers.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("오답 파일 생성 중 오류 발생: " + e.getMessage());
            }
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
                String[] parts = line.split(":", 2);
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
