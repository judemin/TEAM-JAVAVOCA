package io;

import data.entity.Word;
import data.repository.WrongWordRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 오답 데이터 파일을 관리하는 클래스입니다.
 */
public class WrongFileIO extends BaseIO {
    private static final WrongFileIO wrongFileIO;

    static {
        wrongFileIO = new WrongFileIO();
    }

    public static WrongFileIO getInstance() {
        return wrongFileIO;
    }

    @Override
    public void addWord(File file, Word word) throws IOException {
        List<Word> wordList = WrongWordRepository.getInstance().getWordsList();
        addWordIfDistinct(file, word, wordList);
    }

    @Override
    void addWordIfDistinct(File file, Word word, List<Word> wordList) throws IOException {
        super.addWordIfDistinct(file, word, wordList);

    }

    @Override
    public void removeWord(File file, Word word) throws IOException {
        List<Word> wordList = WrongWordRepository.getInstance().getWordsList();
        removeWordIfExists(file, word, wordList);
    }

}
