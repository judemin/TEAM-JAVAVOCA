package manager;

import data.entity.Word;
import data.repository.BaseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 단어 검색 기능을 제공합니다 (부분 문자열 포함 검색).
 */
public class SearchManager {

    private final BaseRepository baseRepository;

    public SearchManager(BaseRepository baseRepository) {
        this.baseRepository = baseRepository;
    }

    /**
     * 단어 검색 메뉴 실행 (부분 문자열 검색 지원)
     */
    public void handleExactWordSearchMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("검색할 단어를 입력해주세요");
            System.out.println("-----------------------------------------------------------------------------------------");
            System.out.print("Javavoca > ");
            String rawInput = scanner.nextLine();

            if (!isValidEnglishWord(rawInput)) {
                System.out.println("잘못된 영단어 입력입니다!");
                continue;
            }

            String input = rawInput.trim().toLowerCase();
            List<Word> matchedWords = findPartialWords(input);

            if (matchedWords.isEmpty()) {
                System.out.println("일치하는 단어 레코드가 없습니다!");
            } else {
                matchedWords.forEach(word ->
                        System.out.println(word.getWord().toLowerCase() + " : " + word.getMeaning()));
            }

            System.out.println("-----------------------------------------------------------------------------------------");
            break;
        }
    }

    /**
     * 입력값 유효성 검사
     */
    private boolean isValidEnglishWord(String input) {
        return input.equals(input.trim()) && input.matches("^[a-zA-Z]+$");
    }

    /**
     * 입력 문자열을 포함하는 단어 모두 반환 (대소문자 무시)
     */
    private List<Word> findPartialWords(String keyword) {
        List<Word> matched = new ArrayList<>();
        List<Word> wordList = baseRepository.getWordsList();

        for (Word word : wordList) {
            if (word.getWord().toLowerCase().contains(keyword)) {
                matched.add(word);
            }
        }

        return matched;
    }
}
