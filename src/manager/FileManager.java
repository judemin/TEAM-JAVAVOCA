package manager;

import data.entity.Word;
import data.repository.BaseRepository;
import data.repository.SavedWordRepository;
import enums.FilePath;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public class FileManager {

    private static final int NOT_EXIST = -1;
    private static final String EMPTY_STRING = "";

    /**
     * 주어진 두 파일 경로에 해당하는 파일들의 존재여부와 권한을 검사합니다.
     */

    public static File getFile(FilePath fp) {
        // TODO: 5.4 무결성 확인 및 처리 - 1. 항목이 이상하게 들여쓰기 되어있음

        // 홈 경로에서 단어 데이터 파일(words.txt, wrong_answers.txt) 존재 여부 확인
        File file = new File(getHomePath(), fp.getPath());
        exitIfNotExist(file);

        return file;
    }

    // 사용자 홈 경로 파악
    public static String getHomePath(){
        String homePath = System.getProperty("user.home");
        // 테스트 전에 한번씩 찍어보세요
        // System.out.println(homePath);
        /*
            /Users/seungwoo - 오잉?
         */
        if (homePath == null || homePath.trim().isEmpty()) {
            System.out.print("!!! 오류: 홈 경로를 파악할 수 없습니다! ");
            exitProgram();
        }
        return homePath;
    }

    private static void exitIfNotExist(File file) {
        if (!file.exists()) {
            System.out.print("!!! 오류: 홈 경로에 "+file.getName()+" 데이터 파일이 없습니다! ");
            exitProgram();
        }
    }


    public static void checkFileAuthority(File file){
        // 3. 두 파일에 대한 입출력(R/W) 권한 검사
        if (!file.canRead() || !file.canWrite()) {
            String errorMessage = "!!! 오류: 데이터 파일\n"
                    + file.getAbsolutePath()
                    + "\n에 대한 입출력 권한이 없습니다! ";
            System.out.print(errorMessage);
            exitProgram();
        }

    }

    public static void checkFileIntegrity(File file) {
        // 파일 무결성 확인
        try {
            // IOException 발생시키면 파일 무결성에 문제가 있다는 뜻
            // new IOException
//        throw new IOException("파일 무결성 오류: " + filePath);

            BufferedReader br = new BufferedReader(new FileReader(file));
            ArrayList<String> errorLineList = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {  // 파일 끝(null)을 만날 때까지 읽기
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

    /**
     *
     * @throws IOException 파일 무결성 위반 또는 읽기 도중 오류
     *
     */
    private static void checkLineIntegrity(String line, ArrayList<String> errorLineList) throws IOException {
        // 줄에 : 이 없으면
        if (line.indexOf(':') == NOT_EXIST) {
            errorLineList.add(line);
            return;
        }
        // 줄에 :이 두 개 이상 있으면
        if (line.indexOf(':') != line.lastIndexOf(':')) {
            errorLineList.add(line);
            return;
        }
        String[] parts = line.split(":", 2);
        String word = parts[0].trim();
        String explanation = parts[1].trim();


        // page 14: 단어 문법 형식
        // 단어의 길이는 1자 이상 50자 이하이어야 합니다.
        if (word.length() > 50 || word.equals(EMPTY_STRING)) {
            errorLineList.add(line);
            return;
        }
        //전체 문자열은 알파벳(A-Z, a-z)으로만 구성됩니다.
        //탭(\t)이나 개행 문자(\n), 문자열 내부 공백은 허용되지 않습니다.
        String regexOfOnlyAlphabets = "^[A-Za-z]+$";
        if (!word.matches(regexOfOnlyAlphabets)) {
            errorLineList.add(line);
            return;
        }

        // page 15: 뜻풀이 문법 형식
        // 길이가 1이상 255자 이하이어야 합니다.
        // 공백만으로 이루어진 문자열이 아니어야 합니다.
        if (explanation.length() > 255 || explanation.equals(EMPTY_STRING)) {
            errorLineList.add(line);
            return;
        }
        // 영어 알파벳(A-Z, a-z) 및 표준 공백으로만 구성되어야 합니다.
        if (!explanation.matches("^[A-Za-z ]+$")) {
            errorLineList.add(line);
            return;
        }

    }

    public static void removeDuplicates(File file) {
        HashMap<String, String> recordMap = new HashMap<>();
        ArrayList<String> duplicatedWordList = new ArrayList<>();
        ArrayList<String> duplicatedExplanationList = new ArrayList<>();
        ArrayList<String> filteredLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {  // 파일 끝(null)을 만날 때까지 읽기
                String[] parts = line.split(":", 2);
                String word = parts[0].trim();
                String explanation = parts[1].trim();

                if (recordMap.containsKey(word)) {
                    // 단어가 이미 존재하면 현재 라인을 제거(추가하지 않음)
                    // 중복 단어 리스트에 단어 추가
                    duplicatedWordList.add(word);
                    continue;
                } else if (recordMap.containsValue(explanation)) {
                    // 뜻풀이가 이미 존재하면 현재 라인을 제거(추가하지 않음)
                    // 중복 뜻풀이 리스트에 뜻풀이 추가
                    duplicatedExplanationList.add(explanation);
                    continue;
                } else {
                    // 중복이 없으면 기록하고, 유효한 라인 목록에 추가
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

        // 중복 제거 결과 출력
        duplicatedWordList.forEach(w ->
                System.out.println("성공적으로 중복 데이터 레코드를 제거했습니다. 사유: 중복인 단어 " + w)
        );
        duplicatedExplanationList.forEach(e ->
                System.out.println("성공적으로 중복 데이터 레코드를 제거했습니다. 사유: 중복인 뜻풀이 " + e)
        );
    }


    public static void loadFiles(File file, BaseRepository repositoryType) {

        BaseRepository repository = repositoryType;

        // 파일 데이터를 WordFileIO, WrongFileIO 객체에 넣기
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {  // 파일 끝(null)을 만날 때까지 읽기
                String[] parts = line.split(":", 2);
                String word = parts[0].trim();
                String explanation = parts[1].trim();
                repository.addWord(Word.of(word,explanation));
            }

        } catch (Exception e) {
            exitProgram();
        }
    }

    private static void exitProgram() {
        System.out.println("프로그램을 종료합니다.");
        System.exit(1);
    }

}
