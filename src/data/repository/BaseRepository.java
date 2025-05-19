package data.repository;

import data.entity.Word;

import java.util.List;
import java.util.Objects;

public abstract class BaseRepository {
    // 메모리 상의 단어 목록
    protected List<Word> words;

    public void addWord(Word word) {
        words.add(word);
    }

    public int count(String word) {
        return 0;
    }

    public List<Word> getWordsList() {
        return words;
    }

    public boolean exists(Word word){
        return word != null && words.contains(word);
    }

    // 더 필요한 메서드 있으면 추가
    public void removeWord(Word word) {
        words.removeIf(entry ->
                Objects.equals(entry.getWord(), word.getWord())
                        && Objects.equals(entry.getMeaning(), word.getMeaning()));
    }
}
