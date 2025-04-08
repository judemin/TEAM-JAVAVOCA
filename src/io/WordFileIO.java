package io;

import data.Word;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * 단어 및 오답 데이터 파일(words.txt, wrong_answers.txt)의 입출력을 관리하는 클래스입니다.
 */
public class WordFileIO extends BaseIO {
    private static WordFileIO wordFileIO;

    static {
        wordFileIO = new WordFileIO();
    }

    public static WordFileIO getInstance(){
        return wordFileIO;
    }
}
