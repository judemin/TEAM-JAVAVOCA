package ui;

import data.entity.User;
import data.repository.SavedWordRepository;
import data.repository.WrongWordRepository;
import enums.FilePath;
import io.WordFileIO;
import io.WrongFileIO;
import manager.FileManager;
import manager.QuizManager;
import manager.SearchManager;
import manager.WordManager;
import util.UserValidator;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * 프로그램의 메인 흐름을 제어하고 사용자 메뉴를 처리합니다.
 */
public class App {

    private WordManager wordManager;
    private QuizManager quizManager;
    private SearchManager searchManager;
    private Scanner scanner;

    private User loggedInUser;

    public App() {
        this.scanner = new Scanner(System.in);
        this.wordManager = new WordManager(scanner, SavedWordRepository.getInstance(), WrongWordRepository.getInstance(), WordFileIO.getInstance(), WrongFileIO.getInstance());
        this.quizManager = new QuizManager(SavedWordRepository.getInstance(), WrongWordRepository.getInstance(), WrongFileIO.getInstance());
        this.searchManager = new SearchManager(SavedWordRepository.getInstance());
    }

    public void run() throws IOException {
        initFileSystem();

        while (true) {
            String command = displayStartMenu();

            switch (command) {
                case "1":
                    loginFlow();
                    break;
                case "2":
                    signupFlow();
                    break;
                default:
                    System.out.println("잘못된 입력입니다. 다시 시도해주세요.");
                    continue;
            }

            // 로그인 또는 회원가입 성공한 경우만 다음 메뉴로 이동
            runMainMenu();
        }
    }

    private void initFileSystem() throws IOException {
        File wordFile = FileManager.getFile(FilePath.WORDS);
        File wrongFile = FileManager.getFile(FilePath.WRONG_ANSWERS);

        FileManager.checkFileAuthority(wordFile);
        FileManager.checkFileAuthority(wrongFile);
        FileManager.checkFileIntegrity(wordFile);
        FileManager.checkFileIntegrity(wrongFile);
        FileManager.removeDuplicates(wordFile);
        FileManager.removeDuplicates(wrongFile);
        FileManager.deleteWrongWordsNotInWordFile(wordFile, wrongFile);
        FileManager.loadFiles(wordFile, SavedWordRepository.getInstance());
        FileManager.loadFiles(wrongFile, WrongWordRepository.getInstance());
    }

    private String displayStartMenu() {
        while (true) {
            System.out.print("Javavoca > ");
            String input = scanner.nextLine().trim();

            String[] tokens = input.split("\\s+");
            if (tokens.length != 2 || !tokens[0].equalsIgnoreCase("Javavoca")) {
                System.out.println("올바른 형식이 아닙니다. 예시) Javavoca > 1");
                continue;
            }

            String menuNumber = tokens[1];
            if (menuNumber.equals("1") || menuNumber.equals("2")) {
                return menuNumber;
            } else {
                System.out.println("올바른 메뉴 항목 번호를 입력해주세요. (1: 로그인 / 2: 회원가입)");
            }
        }
    }

    private void loginFlow() {
        while (true) {
            System.out.print("로그인을 하기 위해 아이디를 입력해주세요.\nJavavoca > ");
            String inputId = scanner.nextLine().trim();

            if (!UserValidator.isValid(inputId)) {
                System.out.println("아이디 형식이 올바르지 않습니다. 다시 입력해주세요.");
                continue;
            }

            // 실제로는 users.txt 등에서 사용자 인증 로직 수행해야 함
            System.out.print("비밀번호를 입력해주세요.\nJavavoca > ");
            String inputPw = scanner.nextLine().trim();

            if (!UserValidator.isValid(inputPw)) {
                System.out.println("비밀번호 형식이 올바르지 않습니다. 다시 입력해주세요.");
                continue;
            }

            // 여기선 간단히 일치 여부만 확인한다고 가정
            this.loggedInUser = new User(inputId, inputPw);
            System.out.println("로그인 성공! 환영합니다, " + inputId + "님.");
            break;
        }
    }

    private void signupFlow() {
        while (true) {
            System.out.print("회원가입을 위해 사용할 아이디를 입력해주세요.\nJavavoca > ");
            String inputId = scanner.nextLine().trim();

            if (!UserValidator.isValid(inputId)) {
                System.out.println("아이디 형식이 올바르지 않습니다. 다시 입력해주세요.");
                continue;
            }

            System.out.print("사용할 비밀번호를 입력해주세요.\nJavavoca > ");
            String inputPw = scanner.nextLine().trim();

            if (!UserValidator.isValid(inputPw)) {
                System.out.println("비밀번호 형식이 올바르지 않습니다. 다시 입력해주세요.");
                continue;
            }

            // 실제로는 users.txt에 저장해야 함
            this.loggedInUser = new User(inputId, inputPw);
            System.out.println("회원가입이 완료되었습니다. 환영합니다, " + inputId + "님.");
            break;
        }
    }

    private void runMainMenu() throws IOException {
        while (true) {
            String input = displayMainMenu();
            String[] tokens = input.split("\\s+");
            if (tokens.length == 0) continue;

            String command = tokens[0];
            switch (command) {
                case "1":
                    quizManager.handleQuizMenu();
                    break;
                case "2":
                    searchManager.handleExactWordSearchMenu();
                    break;
                case "3":
                    wordManager.run();
                    break;
                case "4":
                    exitProgram();
                    return;
                default:
                    System.out.println("올바른 메뉴 번호를 입력해주세요.");
            }
        }
    }

    private String displayMainMenu() {
        System.out.println("\nJavaVoca v1.0 메인메뉴");
        System.out.println("1: 퀴즈 기능");
        System.out.println("2: 검색 기능");
        System.out.println("3: 단어 레코드 관리");
        System.out.println("4: 프로그램 종료");
        System.out.print("Javavoca > ");
        return scanner.nextLine().trim();
    }

    private void exitProgram() {
        System.out.println("프로그램을 종료합니다.");
    }
}
