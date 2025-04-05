package manager;

import data.Word;
import java.util.List;

/**
 * 부분 문자열로 단어나 뜻을 검색하는 기능을 제공합니다.
 */
public class SearchManager {

    private List<Word> words;

    /**
     * SearchManager를 초기화합니다.
     *
     * @param words 검색에 사용할 전체 Word 목록
     */
    public SearchManager(List<Word> words) {
    }

    /**
     * 주어진 문자열이 포함된 영어 단어를 모두 검색하여 반환합니다.
     *
     * @param keyword 검색할 영단어 문자열 (부분 일치)
     * @return 검색된 Word 객체 목록 (영단어 기준)
     */
    public List<Word> searchByWord(String keyword) {
        return null;
    }

    /**
     * 주어진 문자열이 포함된 뜻풀이를 모두 검색하여 반환합니다.
     *
     * @param keyword 검색할 뜻풀이 문자열 (부분 일치)
     * @return 검색된 Word 객체 목록 (뜻풀이 기준)
     */
    public List<Word> searchByMeaning(String keyword) {
        return null;
    }
}
