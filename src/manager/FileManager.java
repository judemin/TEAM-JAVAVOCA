package manager;

import data.Word;
import enums.FilePath;
import io.BaseIO;
import io.WordFileIO;
import io.WrongFileIO;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class FileManager {

    /**
     * 주어진 두 파일 경로에 해당하는 파일들의 존재여부와 권한을 검사합니다.
     */

    // TODO: wordfileIO와 wrongfileIO는 싱글톤으로
    static final HashMap<String, Class<? extends BaseIO>> fileToIO = (HashMap<String, Class<? extends BaseIO>>) Map.of(
            FilePath.WORDS.getPath(), WordFileIO.class,
            FilePath.WRONG_ANSWERS.getPath(), WrongFileIO.class
    );


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
            System.exit(1);
        }
        return homePath;
    }

    private static void exitIfNotExist(File file) {
        if (!file.exists()) {
            System.out.print("!!! 오류: 홈 경로에 "+file.getName()+" 데이터 파일이 없습니다! ");
            exitProgram();
            System.exit(1);
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
            System.exit(1);
        }

    }

    public static void checkFileIntegrity(File file) {
        // 파일 무결성 확인
        try {
            fileToIO.get(file.getName()).getMethod("loadWords").invoke(file.getName());
        } catch (Exception e) {
            exitProgram();
            // 원래 app.run() 에서 return 이었음!
            System.exit(1);
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
            // 원래 app.run() 에서 return 이었음!
            System.exit(1);
        }

        try {
            Files.write(file.toPath(), filteredLines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            exitProgram();
            System.exit(1);
        }

        // 중복 제거 결과 출력
        duplicatedWordList.forEach(w ->
                System.out.println("성공적으로 중복 데이터 레코드를 제거했습니다. 사유: 중복인 단어 " + w)
        );
        duplicatedExplanationList.forEach(e ->
                System.out.println("성공적으로 중복 데이터 레코드를 제거했습니다. 사유: 중복인 뜻풀이 " + e)
        );
    }

    /*
    TODO: checkFileIntegrity에서 분리
     */
    public static void loadFiles(File file) {
        // 파일 데이터를 WordFileIO, WrongFileIO 객체에 넣기
        try {
            fileToIO.get(file.getName()).getMethod("loadWords").invoke(file.getName());
        } catch (Exception e) {
            exitProgram();
            // 원래 app.run() 에서 return 이었음!
            System.exit(1);
        }
    }

    private static void exitProgram() {
        System.out.println("프로그램을 종료합니다.");
    }

}
