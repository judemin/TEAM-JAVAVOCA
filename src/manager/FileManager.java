package manager;

import enums.FilePath;
import io.WordFileIO;
import io.WrongFileIO;

import java.io.File;
import java.io.IOException;

public class FileManager {

    /**
     * 주어진 두 파일 경로에 해당하는 파일들의 존재여부와 권한을 검사합니다.
     */
    static File wordFile;
    static File wrongFile;


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


    public static void checkFilesAuthority(File file){
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

    public static void checkFileIntegrity() {
        // 파일 무결성 확인
        try {
            WordFileIO.loadWords(FilePath.WORDS.getPath());
            WrongFileIO.loadWrongWords(FilePath.WRONG_ANSWERS.getPath());
        } catch (IOException e) {
            exitProgram();
            // 원래 app.run() 에서 return 이었음!
            System.exit(1);
        }
    }

    /*
    TODO: checkFileIntegrity에서 분리
     */
    public static void loadFiles() {

    }

    private static void exitProgram() {
        System.out.println("프로그램을 종료합니다.");
    }

}
