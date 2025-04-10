package ui;

import data.repository.SavedWordRepository;
import data.repository.WrongWordRepository;
import enums.FilePath;
import manager.FileManager;
import manager.QuizManager;
import manager.SearchManager;
import manager.WordManager;

import java.io.File;
import java.util.Scanner;

/**
 * 프로그램의 메인 흐름을 제어하고 사용자 메뉴를 처리합니다.
 */
public class App {

    private WordManager wordManager;
    private QuizManager quizManager;
    private SearchManager searchManager;

    private Scanner scanner;

    /**
     * App을 초기화합니다. 데이터 파일을 로드하고 필요한 매니저 객체들을 생성합니다.
     *
     */
    public App() {
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
        // 분리
        File wordFile = FileManager.getFile(FilePath.WORDS);
        File wrongFile = FileManager.getFile(FilePath.WRONG_ANSWERS);

        FileManager.checkFileAuthority(wordFile);
        FileManager.checkFileAuthority(wrongFile);

        // 파일 무결성 확인, 파일 데이터 가져오기
        FileManager.checkFileIntegrity(wordFile);
        FileManager.checkFileIntegrity(wrongFile);

        FileManager.removeDuplicates(wordFile);
        FileManager.removeDuplicates(wordFile);

        FileManager.loadFiles(wordFile, SavedWordRepository.getInstance());
        FileManager.loadFiles(wrongFile, WrongWordRepository.getInstance());

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
     * 종료 메시지를 출력합니다.
     */
    private void exitProgram() {
        System.out.println("프로그램을 종료합니다.");
    }
}
