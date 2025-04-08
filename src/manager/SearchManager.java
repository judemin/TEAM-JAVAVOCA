package manager;

import data.Word;
import java.util.List;

/**
 * 부분 문자열로 단어나 뜻을 검색하는 기능을 제공합니다.
 */
public class SearchManager {

    // 검색에 사용할 전체 Word 목록
    private List<Word> words;

    /**
     * SearchManager를 초기화합니다.
     */
    public SearchManager() {
    }

    /**
     * 단어 검색 기능을 처리합니다. 검색 기준(단어 또는 뜻풀이)을 선택하고 검색을 수행합니다.
     */
    public void handleSearchMenu() {
        System.out.println("[handleSearchMenu]");
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
