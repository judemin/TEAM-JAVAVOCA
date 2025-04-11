package manager;

import data.entity.Word;
import data.repository.BaseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 부분 문자열로 단어나 뜻을 검색하는 기능을 제공합니다.
 */
public class SearchManager {

    // 검색에 사용할 전체 Word 목록
    private final BaseRepository baseRepository;

    /**
     * SearchManager를 초기화합니다.
     *
     * @param baseRepository 단어를 저장할 내부 저장소
     */
    public SearchManager(BaseRepository baseRepository) {
        this.baseRepository = baseRepository;
    }

    /**
     * 단어 검색 기능을 처리합니다. 검색 기준(단어 또는 뜻풀이)을 선택하고 검색을 수행합니다.
     */
    public void handleSearchMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== 검색 메뉴 ===");
        System.out.println("1. 영어 단어로 검색");
        System.out.println("2. 뜻풀이로 검색");
        System.out.print("선택: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 개행 제거

        System.out.print("검색어 입력: ");
        String keyword = scanner.nextLine();

        List<Word> result;
        if (choice == 1) {
            result = searchByWord(keyword);
        } else if (choice == 2) {
            result = searchByMeaning(keyword);
        } else {
            System.out.println("잘못된 선택입니다.");
            return;
        }

        System.out.println("\n검색 결과:");
        if (result.isEmpty()) {
            System.out.println("검색 결과가 없습니다.");
        } else {
            for (Word word : result) {
                System.out.println(word.getWord() + " : " + word.getMeaning());
            }
        }
    }

    /**
     * 주어진 문자열이 포함된 영어 단어를 모두 검색하여 반환합니다.
     *
     * @param keyword 검색할 영단어 문자열 (부분 일치)
     * @return 검색된 Word 객체 목록 (영단어 기준)
     */
    public List<Word> searchByWord(String keyword) {
        List<Word> result = new ArrayList<>();
        for (Word word : baseRepository.getWordsList()) {
            if (word.getWord().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(word);
            }
        }
        return result;
    }

    /**
     * 주어진 문자열이 포함된 뜻풀이를 모두 검색하여 반환합니다.
     *
     * @param keyword 검색할 뜻풀이 문자열 (부분 일치)
     * @return 검색된 Word 객체 목록 (뜻풀이 기준)
     */
    public List<Word> searchByMeaning(String keyword) {
        List<Word> result = new ArrayList<>();
        for (Word word : baseRepository.getWordsList()) {
            if (word.getMeaning().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(word);
            }
        }
        return result;
    }
}
