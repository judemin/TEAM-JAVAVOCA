package io;

import data.entity.Word;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BaseIO {
    // 싱글톤 객체임.
    private static final BaseIO baseIO;

    static {
        baseIO = new BaseIO();
    }

    private BaseIO(){}

    public static BaseIO getInstance(){
        return baseIO;
    }


    /**
     * 현재 단어 목록을 파일에 저장합니다.
     * @author 기찬
     * @param file 단어 데이터 파일 경로
     * @throws IOException 파일을 쓰는 중 오류가 발생한 경우
     */
    public static void saveWords(File file) throws IOException {
    }

    /**
     * 새로운 단어를 목록에 추가하고 파일에 저장합니다.
     * @author 기찬
     * @param word 추가할 Word 객체
     * @param file 단어 데이터 파일 경로
     * @throws IOException 파일을 쓰는 중 오류가 발생한 경우
     */
    public static void addWord(File file, Word word) throws IOException {
    }

    /**
     * 단어 목록에서 해당 단어를 제거하고 파일을 갱신합니다.
     * @author 기찬
     * @param word 제거할 Word 객체
     * @param file 단어 데이터 파일 경로
     * @throws IOException 파일을 쓰는 중 오류가 발생한 경우
     */
    public static void removeWord(File file, Word word) throws IOException {
    }
}
