package data.repository;

import java.util.ArrayList;

public class WrongWordRepository extends BaseRepository {

    private static final WrongWordRepository WRONG_WORD_REPOSITORY;

    static {
        WRONG_WORD_REPOSITORY = new WrongWordRepository();
    }

    private WrongWordRepository() {
        words = new ArrayList<>();
    }

    // 전역 접근 메서드
    public static WrongWordRepository getInstance() {
        return WRONG_WORD_REPOSITORY;
    }

    @Override
    public int count(String word) {
        return (int) words.stream()
                .filter(w -> w.getWord().equalsIgnoreCase(word))
                .count();
    }

}
