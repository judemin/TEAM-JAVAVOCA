package manager;

import enums.FilePath;
import io.WordFileIO;
import io.WrongFileIO;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class FileManager {

    /**
     * 주어진 두 파일 경로에 해당하는 파일들의 존재여부와 권한을 검사합니다.
     */

    static File wordFile;
    static File wrongFile;


    public static void checkFilesExistance() {
        // TODO: 5.4 무결성 확인 및 처리 - 1. 항목이 이상하게 들여쓰기 되어있음

        // 1. 사용자 홈 경로 파악
        String homePath = System.getProperty("user.home");
        // 테스트 전에 한번씩 찍어보세요
        // System.out.println(homePath);
        if (homePath == null || homePath.trim().isEmpty()) {
            System.out.print("!!! 오류: 홈 경로를 파악할 수 없습니다! ");
            exitProgram();
            System.exit(1);
        }

        // 2. 홈 경로에서 단어 데이터 파일(words.txt, wrong_answers.txt) 존재 여부 확인
        wordFile = new File(homePath, "words.txt");
        wrongFile = new File(homePath, "wrong_answers.txt");

        if (!wordFile.exists()) {
            System.out.print("!!! 오류: 홈 경로에 words.txt 데이터 파일이 없습니다! ");
            exitProgram();
            System.exit(1);
        }
        if (!wrongFile.exists()) {
            System.out.print("!!! 오류: 홈 경로에 wrong_answers.txt 데이터 파일이 없습니다! ");
            exitProgram();
            System.exit(1);
        }

    }


    public static void checkFilesAuthority(){
        // 3. 두 파일에 대한 입출력(R/W) 권한 검사
        if (!wordFile.canRead() || !wordFile.canWrite()) {
            System.out.println("!!! 오류: 데이터 파일");
            System.out.println(wordFile.getAbsolutePath());
            System.out.print("에 대한 입출력 권한이 없습니다! ");
            exitProgram();
            System.exit(1);
        }
        if (!wrongFile.canRead() || !wrongFile.canWrite()) {
            System.out.println("!!! 오류: 데이터 파일 ");
            System.out.println(wrongFile.getAbsolutePath());
            System.out.print("에 대한 입출력 권한이 없습니다! ");
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
            return;
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
