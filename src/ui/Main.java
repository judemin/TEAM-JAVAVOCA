package ui;

import enums.FilePath;

/**
 * JavaVoca 프로그램의 진입점입니다.
 */
public class Main {

    /**
     * 프로그램의 시작점입니다. App 객체를 생성하여 프로그램을 실행합니다.
     *
     * @param args 명령행 인자
     */
    public static void main(String[] args) {
        App app = new App();
        app.run();
    }
}
