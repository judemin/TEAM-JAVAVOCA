package data.repository;

import java.util.ArrayList;

public class SavedWordRepository extends BaseRepository {

    private static final SavedWordRepository SAVED_WORD_REPOSITORY;

    static {
        SAVED_WORD_REPOSITORY = new SavedWordRepository();
    }

    private SavedWordRepository(){
        words = new ArrayList<>();
    }

    // 전역 접근 메서드
    public static SavedWordRepository getInstance() {
        return SAVED_WORD_REPOSITORY;
    }

}
