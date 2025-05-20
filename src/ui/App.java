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
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static manager.FileManager.getCurrentPath;

/**
 * 프로그램의 메인 흐름을 제어하고 사용자 메뉴를 처리합니다.
 */
public class App {
/**
 * p10 users.txt는 “사용자 정보”가 기록된 형식의 텍스트 파일이며, 프로그램 실행 시 반드시 존재해야 합니다.
 *
 */
    private WordManager wordManager;
    private QuizManager quizManager;
    private SearchManager searchManager;
    private Scanner scanner;

    private User loggedInUser;

    public App() {
        this.scanner = new Scanner(System.in);
        this.quizManager = new QuizManager(SavedWordRepository.getInstance(), WrongWordRepository.getInstance(), WrongFileIO.getInstance());
        this.wordManager = new WordManager(scanner, SavedWordRepository.getInstance(), WrongWordRepository.getInstance(), WordFileIO.getInstance(), WrongFileIO.getInstance());
        this.searchManager = new SearchManager(SavedWordRepository.getInstance(), WrongWordRepository.getInstance());
    }

    public void run() throws IOException {
        initUserFile();
        while (true) {
            String command = displayStartMenu();

            boolean loginSuccess = false;

            loginSuccess = switch (command) {
                case "1" -> loginFlow();
                case "2" -> signupFlow();
                default -> false;
            };

            if (loginSuccess) {
                initWordAndWrongFiles();
                quizManager.setCurrentUserId(loggedInUser.getId());
                runMainMenu();
            }
        }
    }

    private void initUserFile() throws IOException {
        File userFile = FileManager.getFile(FilePath.USER_INFO);
        FileManager.checkFileAuthority(userFile);
        FileManager.checkFileIntegrity(userFile,FilePath.USER_INFO);
        FileManager.removeUserEntryDuplicates(userFile);
    }

    // TODO 유저에 알맞는 wrong_answer로 해주어야 함.
    private void initWordAndWrongFiles() throws IOException {
        File wordFile = FileManager.getFile(FilePath.WORDS);
        FileManager.checkFileAuthority(wordFile);
        FileManager.checkFileIntegrity(wordFile,FilePath.WORDS);

        FileManager.removeWordEntryDuplicates(wordFile);
        FileManager.loadWordFile(wordFile);

        String wrongFileName = loggedInUser.getId() + FilePath.WRONG_ANSWERS.getPath();
        File wrongFile = new File(getCurrentPath(),wrongFileName);
        if (!wrongFile.exists()) {
            wrongFile.createNewFile();
            return;
        }

        // wrongFile이 이미 존재하고 있어야 의미있는 작업들
        FileManager.checkFileAuthority(wrongFile);
        FileManager.checkFileIntegrity(wrongFile,FilePath.WRONG_ANSWERS);
        FileManager.removeWordEntryDuplicates(wrongFile);
        FileManager.deleteWrongWordsNotInWordFile(wordFile, wrongFile);
        FileManager.loadWrongFile(wrongFile);
    }

    private String displayStartMenu() {
        while (true) {
            System.out.print("Javavoca > ");
            // trim 붙인 이유: p36: "입력 중 첫번째 단어가"
            String input = scanner.nextLine().trim();

            if (input.equals("1") || input.equals("2")) {
                return input;
            } else {
                System.out.println("올바른 메뉴 항목 번호를 입력해주세요. (1: 로그인 / 2: 회원가입)");
            }
        }
    }

    private boolean loginFlow() {
        String inputId;
        while (true) {
            System.out.print("로그인을 하기 위해 아이디를 입력해주세요.\nJavavoca > ");
            inputId = scanner.nextLine();/*.trim();*/

//            if (inputId.isBlank()) {
//                System.out.println(".!!오류: 공백만 입력할 수 없습니다!");
//                continue;
//            }

            if (!UserValidator.isValid(inputId)) {
                System.out.println(".!!오류: 올바른 형식의 아이디가 아닙니다!");
                continue;
            }

            break;
        }

        String inputPw;
        while (true) {
            System.out.print("비밀번호를 입력해주세요.\nJavavoca > ");
            inputPw = scanner.nextLine(); /*.trim();*/

//            if (inputPw.isBlank()) {
//                System.out.println(".!!오류: 공백만 입력할 수 없습니다!");
//                continue;
//            }

            if (!UserValidator.isValid(inputPw)) {
                System.out.println(".!!오류: 올바른 형식의 비밀번호가 아닙니다!");
                continue;
            }

            break;
        }

        if (!authenticateUser(inputId, inputPw)) {
            System.out.println("입력하신 아이디 또는 비밀번호가 일치하지 않습니다.");
            return false;
        }

        System.out.println("로그인이 성공적으로 완료되었습니다!");
        this.loggedInUser = new User(inputId, inputPw);
        quizManager.setCurrentUserId(inputId);

        return true;
    }

    private boolean signupFlow() {
        while (true) {
            System.out.print("회원가입을 위한 아이디를 입력해주세요.\nJavavoca > ");
            String inputId = scanner.nextLine(); /*.trim();*/

            if (!UserValidator.isValid(inputId)) {
                System.out.println("아이디 형식이 올바르지 않습니다. 다시 입력해주세요.");
                continue;
            }

            if (isUserIdExists(inputId)) {
                System.out.println("사용자 데이터 파일에 중복 아이디가 존재합니다.");
                return false;
            }

            System.out.print("비밀번호를 입력해주세요.\nJavavoca > ");
            String inputPw = scanner.nextLine(); /*.trim();*/

            if (!UserValidator.isValid(inputPw)) {
                System.out.println("비밀번호 형식이 올바르지 않습니다. 다시 입력해주세요.");
                continue;
            }

            saveUserToFile(inputId, inputPw);
            this.loggedInUser = new User(inputId, inputPw);
            quizManager.setCurrentUserId(inputId);
            System.out.println("회원가입이 완료되었습니다.");
            return true;
        }
    }

    private boolean isUserIdExists(String userId) {
        File userFile = new File(getCurrentPath(),"users.txt");
        if (!userFile.exists()) return false;

        try (Scanner fileScanner = new Scanner(userFile)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] tokens = line.split(":");
                if (tokens.length >= 1 && tokens[0].equals(userId)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("사용자 파일을 읽는 중 오류가 발생했습니다.");
        }

        return false;
    }

    private boolean authenticateUser(String userId, String password) {
        File userFile = new File(getCurrentPath(),"users.txt");
        //파일이 무조건 존재해야 이 상태로 오니까 필요없음
        //if (!userFile.exists()) return false;

        try (Scanner fileScanner = new Scanner(userFile)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] tokens = line.split(":");
                if (tokens.length == 2 && tokens[0].equals(userId) && tokens[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("로그인 중 오류가 발생했습니다.");
        }

        return false;
    }

    private void saveUserToFile(String userId, String password) {
        File userFile = new File(getCurrentPath(),"users.txt");

        try (FileWriter writer = new FileWriter(userFile, true)) {
            writer.write(userId + ":" + password + "\n");
        } catch (IOException e) {
            System.out.println("사용자 정보를 저장하는 중 오류가 발생했습니다.");
        }
    }

    private void runMainMenu() throws IOException {
        while (true) {
            String input = displayMainMenu();
            switch (input) {
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
        // trim 붙인 이유: "p43: 입력 중 첫 번째 단어가"
        return scanner.nextLine().trim();
    }

    private void exitProgram() {
        System.out.println("프로그램을 종료합니다.");
    }
}