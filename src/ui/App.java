package ui;

import exception.KeyException;
import io.WordFileIO;
import io.WrongFileIO;
import manager.QuizManager;
import manager.SearchManager;
import manager.WordManager;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * 프로그램의 메인 흐름을 제어하고 사용자 메뉴를 처리합니다.
 */
public class App {

    private String wordFilePath;
    private String wrongFilePath;

    private WordManager wordManager;
    private QuizManager quizManager;
    private SearchManager searchManager;

    private Scanner scanner;

    /**
     * App을 초기화합니다. 데이터 파일을 로드하고 필요한 매니저 객체들을 생성합니다.
     *
     * @param wordFilePath  단어 데이터 파일 경로
     * @param wrongFilePath 오답 데이터 파일 경로
     */
    public App(String wordFilePath, String wrongFilePath) {
        this.wordFilePath = wordFilePath;
        this.wrongFilePath = wrongFilePath;

        this.wordManager = new WordManager();
        this.quizManager = new QuizManager();
        this.searchManager = new SearchManager();
        this.scanner = new Scanner(System.in);
    }

    /**
     * 프로그램의 메인 실행 루프를 시작합니다. 메인 메뉴를 표시하고 사용자의 선택을 처리합니다.
     */
    public void run() {
        // 홈경로, 파일 존재, 입출력 권한 확인
        checkFiles();

        // 파일 무결성 확인
        try {
            WordFileIO.loadWords(wordFilePath);
            WrongFileIO.loadWrongWords(wrongFilePath);
        } catch (IOException e) {
            exitProgram();
            return;
        }

        while(true){
            // TODO: 에러 메세지 출력을 할지 말지 (수정사항)
            // 만일 주 프롬프트에서 그냥 곧바로 Enter ⏎ 키만 누르거나 (즉, 빈 문자열 입력),
            // 공백류(들)만 입력하거나, 입력 중 첫번째 단어가 메뉴 항목 번호가 아닐 경우,
            // 틀린 입력으로 간주하고 (“잘못된 입력입니다.”같은 진부한 안내 없이, 조용히)
            // 표1에 준하는 표준 메뉴 항목 번호 및 인자 안내 화면을 출력하고 주 프롬프트로 되돌아갑니다
            String input = displayMainMenu();
            if (!input.isEmpty()){
                String[] tokens = input.split("\\s+");
                String command = tokens[0];
                if (command.matches("[1-4]")) {
                    switch (command) {
                        case "1":
                            quizManager.handleQuizMenu();
                            break;
                        case "2":
                            searchManager.handleSearchMenu();
                            break;
                        case "3":
                            wordManager.handleWordManagementMenu();
                            break;
                        case "4":
                            exitProgram();
                            scanner.close();
                            return;
                    }
                }
            }
            System.out.println();
        }
    }

    /**
     * 메인 메뉴를 출력합니다.
     */
    private String displayMainMenu() {
        System.out.println("JavaVoca v1.0 메인메뉴");
        System.out.println("원하시는 기능을 선택해주세요");
        System.out.println("(1: 퀴즈 기능 / 2: 검색 기능 / 3: 단어 레코드 관리 기능 / 4: 프로그램 종료)");
        System.out.print("Javavoca > ");
        String input = scanner.nextLine().trim();
        return input;
    }

    /**
     * 주어진 두 파일 경로에 해당하는 파일들의 존재여부와 권한을 검사합니다.
     */
    public void checkFiles() {
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
        File wordFile = new File(homePath, "words.txt");
        File wrongFile = new File(homePath, "wrong_answers.txt");

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

    /**
     * 종료 메시지를 출력합니다.
     */
    private void exitProgram() {
        System.out.println("프로그램을 종료합니다.");
    }
}
