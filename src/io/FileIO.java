package io;

import data.Word;
import java.io.IOException;
import java.util.List;

/**
 * 데이터 파일(words.txt, wrong_answers.txt)의 입출력을 관리하는 클래스입니다.
 */
public class FileIO {

    /**
     * 단어 데이터 파일(words.txt)을 읽어서 단어 목록을 반환합니다.
     *
     * @param filePath 단어 데이터 파일 경로
     * @return Word 객체 목록 (파일의 모든 단어 레코드)
     * @throws IOException 파일을 읽는 중 오류가 발생한 경우
     */
    public static List<Word> readWordsFile(String filePath) throws IOException {
        return null;
    }

    /**
     * 주어진 단어 목록을 단어 데이터 파일에 저장합니다.
     *
     * @param filePath 단어 데이터 파일 경로
     * @param words    저장할 Word 객체 목록
     * @throws IOException 파일을 쓰는 중 오류가 발생한 경우
     */
    public static void writeWordsFile(String filePath, List<Word> words) throws IOException {
    }
}
