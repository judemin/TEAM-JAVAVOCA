package data.repository;

import data.entity.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WordRepository {

    private static final WordRepository wordRepository;

    static {
        wordRepository = new WordRepository();
    }

    private WordRepository (){
        words = new ArrayList<>();
    }

    // 전역 접근 메서드
    public static WordRepository getInstance() {
        return wordRepository;
    }

    // 메모리 상의 단어 목록
    private static List<Word> words;

    public static void addWord(Word word){
        words.add(word);
    }

    public static List<Word> getWordsList(){
        return words;
    }

    // 더 필요한 메서드 있으면 추가
    public static void removeWord(Word word){
        words.removeIf(entry ->
                Objects.equals(entry.getWord(), word.getWord())
                        && Objects.equals(entry.getMeaning(), word.getMeaning()));
    }

}
