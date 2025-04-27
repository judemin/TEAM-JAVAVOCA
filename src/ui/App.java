package ui;

import data.repository.SavedWordRepository;
import data.repository.WrongWordRepository;
import enums.FilePath;
import io.BaseIO;
import io.WordFileIO;
import io.WrongFileIO;
import manager.FileManager;
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

    private WordManager wordManager;
    private QuizManager quizManager;
    private SearchManager searchManager;

    private Scanner scanner;

    /**
     * App을 초기화합니다. 데이터 파일을 로드하고 필요한 매니저 객체들을 생성합니다.
     */
    public App() {
        this.wordManager = new WordManager(new Scanner(System.in), SavedWordRepository.getInstance(), WrongWordRepository.getInstance(), WordFileIO.getInstance(), WrongFileIO.getInstance());
        this.quizManager = new QuizManager(SavedWordRepository.getInstance(), WrongWordRepository.getInstance(), WrongFileIO.getInstance());
        this.searchManager = new SearchManager(SavedWordRepository.getInstance());
        this.scanner = new Scanner(System.in);
    }

    /**
     * 프로그램의 메인 실행 루프를 시작합니다. 메인 메뉴를 표시하고 사용자의 선택을 처리합니다.
     */
    public void run() throws IOException {

        // 파일 무결성 확인, 파일 데이터 가져오기
        File wordFile = FileManager.getFile(FilePath.WORDS);
        File wrongFile = FileManager.getFile(FilePath.WRONG_ANSWERS);

        FileManager.checkFileAuthority(wordFile);
        FileManager.checkFileAuthority(wrongFile);

        FileManager.checkFileIntegrity(wordFile);
        FileManager.checkFileIntegrity(wrongFile);

        // 중복 데이터 레코드 검사
        FileManager.removeDuplicates(wordFile);
        FileManager.removeDuplicates(wrongFile);

        FileManager.deleteWrongWordsNotInWordFile(wordFile, wrongFile);

        FileManager.loadFiles(wordFile, SavedWordRepository.getInstance());
        FileManager.loadFiles(wrongFile, WrongWordRepository.getInstance());


        while (true) {
            FileManager.removeDuplicates(wordFile);
            FileManager.removeDuplicates(wrongFile);

            String input = displayMainMenu();
            if (!input.isEmpty()) {
                String[] tokens = input.split("\\s+");
                String command = tokens[0];
                if (command.matches("[1-4]")) {
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
