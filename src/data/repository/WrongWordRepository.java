package data.repository;

import data.entity.Word;

import java.util.ArrayList;
import java.util.List;

public class WrongWordRepository extends BaseRepository {

    private static final WrongWordRepository WRONG_WORD_REPOSITORY;

    static {
        WRONG_WORD_REPOSITORY = new WrongWordRepository();
    }

    private ArrayList<Integer> wrongCountList;

    public void addWrongCount(int count){
        wrongCountList.add(count);
    }

    public List<Integer> getWrongCountList(){
        return wrongCountList;
    }

    private WrongWordRepository() {
        words = new ArrayList<>();
        wrongCountList = new ArrayList<>();
    }

    // 전역 접근 메서드
    public static WrongWordRepository getInstance() {
        return WRONG_WORD_REPOSITORY;
    }
    @Override
    public int getCount(Word word) {
        int i = getWordIndex(word);
        return wrongCountList.get(i);
    }
    @Override
    public void setCount(Word word, int count){
        int i = getWordIndex(word);
        wrongCountList.set(i,count);
    }

    @Override
    public void removeWord(Word word) {
        int i = getWordIndex(word);
        words.remove(i);
        wrongCountList.remove(i);
    }

    private int getWordIndex(Word word) {
        int i;
        for(i=0;i<words.size();i++){
            Word entry = words.get(i);
            if(entry.getWord().equalsIgnoreCase(word.getWord())){
                break;
            }
        }
        if(i == words.size()){
            throw new IllegalArgumentException("존재하지 않는 단어");
        }
        return i;
    }
}
