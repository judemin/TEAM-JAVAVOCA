package ui;

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

    private WordManager wordManager;
    private QuizManager quizManager;
    private SearchManager searchManager;
    private WrongFileIO wrongFileIO;
    private Scanner scanner;

    /**
     * App을 초기화합니다. 데이터 파일을 로드하고 필요한 매니저 객체들을 생성합니다.
     *
     * @param wordFilePath  단어 데이터 파일 경로
     * @param wrongFilePath 오답 데이터 파일 경로
     * @throws IOException 데이터 파일 로드 중 오류가 발생한 경우
     */
    public App(String wordFilePath, String wrongFilePath) throws IOException {
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
     * 단어 퀴즈 모드 선택 및 진행을 처리합니다.
     * 일반 퀴즈와 오답 퀴즈 모드 중 선택하여 퀴즈를 시작합니다.
     */
    private void handleQuizMenu() {
    }

    /**
     * 단어 검색 기능을 처리합니다. 검색 기준(단어 또는 뜻풀이)을 선택하고 검색을 수행합니다.
     */
    private void handleSearchMenu() {
    }

    /**
     * 단어 레코드 관리 기능을 처리합니다.
     * 단어 추가, 수정, 삭제 중 하나를 선택하고 해당 기능을 실행합니다.
     */
    private void handleWordManagementMenu() {
    }

    /**
     * 프로그램을 종료합니다. 필요한 경우 자원을 정리하고 종료 메시지를 출력합니다.
     */
    private void exitProgram() {
    }
}
