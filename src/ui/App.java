package ui;

import exception.KeyException;
import io.WordFileIO;
import io.WrongFileIO;
import manager.QuizManager;
import manager.SearchManager;
import manager.WordManager;
import java.io.IOException;
import java.util.Scanner;

/**
 * 프로그램의 메인 흐름을 제어하고 사용자 메뉴를 처리합니다.
 */
public class App {

    private String wordFilePath;
    private String wrongFilePath;

    private KeyException keyException;
    private WordManager wordManager;
    private QuizManager quizManager;
    private SearchManager searchManager;
    private WordFileIO fileIO;
    private WrongFileIO wrongFileIO;
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

        this.keyException = new KeyException();
        this.wordManager = new WordManager();
        this.quizManager = new QuizManager();
        this.searchManager = new SearchManager();
        this.fileIO = new WordFileIO();
        this.wrongFileIO = new WrongFileIO();
        this.scanner = new Scanner(System.in);
    }

    /**
     * 프로그램의 메인 실행 루프를 시작합니다. 메인 메뉴를 표시하고 사용자의 선택을 처리합니다.
     */
    public void run() {
    }

    /**
     * 메인 메뉴를 출력합니다.
     */
    private void displayMainMenu() {
    }

    /**
     * 프로그램을 종료합니다. 필요한 경우 자원을 정리하고 종료 메시지를 출력합니다.
     */
    private void exitProgram() {
    }
}
