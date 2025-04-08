package io;

import data.Word;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 오답 데이터 파일을 관리하는 클래스입니다.
 */
public class WrongFileIO {

    // 메모리 상의 단어 목록
    private static List<Word> wrongWords = new ArrayList<Word>();

    /**
     * 오답 데이터 파일(wrong_answers.txt)을 읽어서 오답 단어 목록을 초기화합니다.
     *
     * @param filePath 오답 데이터 파일 경로
     * @return 오답 단어 목록
     * @throws IOException 파일을 읽는 중 오류가 발생한 경우
     */
    public static void loadWrongWords(String filePath) throws IOException {
        // IOException 발생시키면 파일 무결성에 문제가 있다는 뜻
        // new IOException
    }

    /**
     * 현재 오답 단어 목록을 오답 데이터 파일에 저장합니다.
     *
     * @param filePath 오답 데이터 파일 경로
     * @throws IOException 파일을 쓰는 중 오류가 발생한 경우
     */
    public static void saveWrongWords(String filePath) throws IOException {
    }

    /**
     * 새로운 오답 단어를 오답 목록에 추가하고 파일에 기록합니다.
     *
     * @param word 추가할 오답 Word 객체
     * @throws IOException 파일을 쓰는 중 오류가 발생한 경우
     */
    public static void addWrongWord(Word word) throws IOException {
    }

    /**
     * 오답 단어 목록에서 해당 단어를 제거하고 파일을 갱신합니다.
     *
     * @param word 제거할 Word 객체
     * @throws IOException 파일을 쓰는 중 오류가 발생한 경우
     */
    public void removeWrongWord(Word word) throws IOException {
    }

    /**
     * 현재 메모리상의 오답 단어 목록을 반환합니다.
     *
     * @return 오답 Word 객체 목록
     */
    public List<Word> getWrongWord() {
        return wrongWords;
    }
}
