package io;

import data.entity.Word;
import data.repository.WrongWordRepository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
        List<Integer> wrongCountList = WrongWordRepository.getInstance().getWrongCountList();
        addWrongWordIfDistinct(file, word, wordList,wrongCountList);
    }

    void addWrongWordIfDistinct(File file, Word word, List<Word> wordList, List<Integer> wrongCountList) throws IOException {
        if (isDistinct(word, wordList)) {
            wordList.add(word);
            wrongCountList.add(1);
            addWordToFile(file, word);
        }
    }

    @Override
    public void addWordToFile(File file, Word word) throws IOException{

        // try-with-resources 구문 사용
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.newLine(); // 새로운 줄로 이동 (없어도 되지만 가독성 위해)
            writer.write(word.toString() + ": 1");
        } catch (IOException e) {
            exitProgram();
        }

    }

    @Override
    public void removeWord(File file, Word word) throws IOException {
        List<Word> wordList = WrongWordRepository.getInstance().getWordsList();
        removeWordIfExists(file, word, wordList);
    }

    @Override
    void removeWordIfExists(File file, Word word, List<Word> wordList) throws IOException {
        boolean removed = false;
        int i;
        for(i=0;i<wordList.size();i++){
            if(wordList.get(i).equals(word)){
                wordList.remove(i);
                removed = true;
                break;
            }
        }

        if (removed) {
            WrongWordRepository.getInstance().getWrongCountList().remove(i);
            removeWordInFile(file, word);
        } else {
            System.out.println("해당 단어를 찾을 수 없습니다.");
        }
    }

}
