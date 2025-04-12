package manager;

import data.entity.Word;
import data.repository.BaseRepository;

import java.util.List;
import java.util.Scanner;

/**
 * 정확히 일치하는 단어를 검색하는 기능을 제공합니다.
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
     * 단어 검색 기능을 처리합니다. 사용자 입력을 받고 유효성 검사 및 정확히 일치하는 단어를 검색합니다.
     */
    public void handleExactWordSearchMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("검색할 단어를 입력해주세요");
            System.out.println("-----------------------------------------------------------------------------------------");
            System.out.print("Javavoca > ");
            String rawInput = scanner.nextLine();

            // 공백 포함되거나 영단어가 아닐 경우
            if (!isValidEnglishWord(rawInput)) {
                System.out.println("잘못된 영단어 입력입니다!");
                continue;
            }

            String input = rawInput.trim(); // 정상 입력일 경우만 trim

            Word found = findExactWord(input.toLowerCase());

            if (found != null) {
                System.out.println(found.getWord().toLowerCase() + ":" + found.getMeaning());
            } else {
                System.out.println("일치하는 단어 레코드가 없습니다!");
            }

            System.out.println("-----------------------------------------------------------------------------------------");
            break;
        }
    }

    /**
     * 사용자 입력값이 유효한 영단어 형식인지 검사합니다. (앞뒤 공백 포함 금지)
     *
     * @param input 입력값
     * @return 유효 여부
     */
    private boolean isValidEnglishWord(String input) {
        return input.equals(input.trim()) && input.matches("^[a-zA-Z]+$");
    }

    /**
     * 정확히 일치하는 단어를 검색합니다 (대소문자 구분 없이).
     *
     * @param keyword 찾고자 하는 영단어 (소문자 기준)
     * @return 일치하는 Word 객체, 없으면 null
     */
    private Word findExactWord(String keyword) {
        List<Word> wordList = baseRepository.getWordsList();
        for (Word word : wordList) {
            if (word.getWord().equalsIgnoreCase(keyword)) {
                return word;
            }
        }
        return null;
    }
}
