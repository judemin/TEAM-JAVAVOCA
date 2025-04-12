package io;

import data.entity.Word;
import data.repository.SavedWordRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 단어 및 오답 데이터 파일(words.txt, wrong_answers.txt)의 입출력을 관리하는 클래스입니다.
 */
public class WordFileIO extends BaseIO {
    private static final WordFileIO wordFileIO;

    static {
        wordFileIO = new WordFileIO();
    }

    public static WordFileIO getInstance(){
        return wordFileIO;
    }


    @Override
    public void addWord(File file, Word word) throws IOException {
        List<Word> wordList = SavedWordRepository.getInstance().getWordsList();
        addWordIfDistinct(file, word, wordList);
    }

    @Override
    public void removeWord(File file, Word word) throws IOException {
        List<Word> wordList = SavedWordRepository.getInstance().getWordsList();
        removeWordIfExists(file, word, wordList);
    }

}
