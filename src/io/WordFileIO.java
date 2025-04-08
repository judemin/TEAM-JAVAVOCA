package io;

import data.Word;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * 단어 및 오답 데이터 파일(words.txt, wrong_answers.txt)의 입출력을 관리하는 클래스입니다.
 */
public class WordFileIO {

    // 메모리 상의 단어 목록
    private static List<Word> words = new ArrayList<>();

    /**
     * 단어 데이터 파일(words.txt)을 읽어서 오답 단어 목록을 초기화합니다.
     *
     * @param filePath 단어 데이터 파일 경로
     * @return 단어 레코드 목록
     * @throws IOException 파일을 읽는 중 오류가 발생한 경우
     */
    public static void loadWords(String filePath) throws IOException {
        // IOException 발생시키면 파일 무결성에 문제가 있다는 뜻
        // new IOException
    }

    /**
     * 현재 단어 목록을 파일에 저장합니다.
     *
     * @param filePath 단어 데이터 파일 경로
     * @throws IOException 파일을 쓰는 중 오류가 발생한 경우
     */
    public static void saveWords(String filePath) throws IOException {
    }

    /**
     * 새로운 단어를 목록에 추가하고 파일에 저장합니다.
     *
     * @param word 추가할 Word 객체
     * @param filePath 단어 데이터 파일 경로
     * @throws IOException 파일을 쓰는 중 오류가 발생한 경우
     */
    public static void addWord(String filePath, Word word) throws IOException {
    }

    /**
     * 단어 목록에서 해당 단어를 제거하고 파일을 갱신합니다.
     *
     * @param word 제거할 Word 객체
     * @param filePath 단어 데이터 파일 경로
     * @throws IOException 파일을 쓰는 중 오류가 발생한 경우
     */
    public static void removeWord(String filePath, Word word) throws IOException {
    }

    /**
     * 현재 메모리상의 단어 목록을 반환합니다.
     *
     * @return Word 객체 목록
     */
    public List<Word> getWrongWord() {
        return words;
    }
}
