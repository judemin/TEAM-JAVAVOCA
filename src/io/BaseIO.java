package io;

import data.entity.Word;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public abstract class BaseIO {

    /**
     * 현재 단어 목록을 파일에 저장합니다.
     *
     * @param file 단어 데이터 파일 경로
     * @throws IOException 파일을 쓰는 중 오류가 발생한 경우
     * @author 기찬
     */
    public abstract void saveWords(File file) throws IOException;

    /**
     * 새로운 단어를 목록에 추가하고 파일에 저장합니다.
     *
     * @param word 추가할 Word 객체
     * @param file 단어 데이터 파일 경로
     * @throws IOException 파일을 쓰는 중 오류가 발생한 경우
     * @author 기찬
     */
    public abstract void addWord(File file, Word word) throws IOException;

    /**
     * 단어 목록에서 해당 단어를 제거하고 파일을 갱신합니다.
     *
     * @param word 제거할 Word 객체
     * @param file 단어 데이터 파일 경로
     * @throws IOException 파일을 쓰는 중 오류가 발생한 경우
     * @author 기찬
     */
    public abstract void removeWord(File file, Word word) throws IOException;

    void writeWordListInFile(File file, List<Word> wordList) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Word word : wordList) {
                writer.write(word.getWord() + " : " + word.getMeaning());
                writer.newLine();
            }
        }
    }

    void addWordIfDistinct(File file, Word word, List<Word> wordList) throws IOException {
        for (Word w : wordList) {
            if (w.equals(word)) {
                System.out.println("이미 존재하는 단어입니다. 다시 입력해 주세요.");
                return;
            }
            if (w.getMeaning().equals(word.getMeaning())) {
                System.out.println("이미 존재하는 뜻풀이입니다. 다시 입력해 주세요.");
                return;
            }
        }
        wordList.add(word);
        saveWords(file);
    }

    void removeWordIfExists(File file, Word word, List<Word> wordList) throws IOException {
        boolean removed = wordList.removeIf(w -> w.equals(word));
        if (removed) {
            saveWords(file);
        } else {
            System.out.println("해당 단어를 찾을 수 없습니다.");
        }
    }
}
