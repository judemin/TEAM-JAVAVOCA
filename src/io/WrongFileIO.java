package io;

/**
 * 오답 데이터 파일을 관리하는 클래스입니다.
 */
public class WrongFileIO extends BaseIO {
    private static WrongFileIO wrongFileIO;

    static {
        wrongFileIO = new WrongFileIO();
    }

    public static WrongFileIO getInstance(){
        return wrongFileIO;
    }
}
