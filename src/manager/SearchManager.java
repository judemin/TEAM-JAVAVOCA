package manager;

import data.entity.Word;
import data.repository.BaseRepository;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 단어 검색 및 수정 기능을 제공합니다 (부분 문자열 포함 검색).
 */
public class SearchManager {

    private final BaseRepository baseRepository;
    private final BaseRepository wrongRepository;
    private int savedSize;

    public SearchManager(BaseRepository baseRepository, BaseRepository wrongRepository) {
        this.baseRepository = baseRepository;
        this.wrongRepository = wrongRepository;
        this.savedSize = baseRepository.getWordsList().size();
    }

    /**
     * 단어 검색 메뉴 실행 및 오답률 기반 수정 허용 처리
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

                System.out.println("수정하시겠습니까? (yes / no)");
                System.out.print("Javavoca > ");
                String confirm = scanner.nextLine().trim().toLowerCase();
                if (confirm.equals("yes")) {
                    Word target = matchedWords.get(0); // 첫 번째 결과만 대상으로 처리
                    double errorRate = calculateErrorRate(target);
                    if (errorRate > 0.3) {
                        System.out.println("[오류] 이 단어는 오답률이 0.3을 초과하여 수정할 수 없습니다. (오답률: " + errorRate + ")");
                        return;
                    }

                    System.out.print("새 단어를 입력하세요: ");
                    String newWord = scanner.nextLine().trim();
                    System.out.print("새 뜻풀이를 입력하세요: ");
                    String newMeaning = scanner.nextLine().trim();

                    try (FileWriter writer = new FileWriter("words.txt", true)) {
                        writer.write(newWord + ":" + newMeaning + "\n");
                        System.out.println("단어가 성공적으로 추가되었습니다.");
                    } catch (IOException e) {
                        System.out.println("파일에 쓰는 중 오류 발생: " + e.getMessage());
                    }
                }
            }

            System.out.println("-----------------------------------------------------------------------------------------");
            break;
        }
    }

    private double calculateErrorRate(Word word) {
        int wrongCount = wrongRepository.count(word);
        return (double) wrongCount * savedSize / (savedSize + wrongCount * savedSize);
    }

    private boolean isValidEnglishWord(String input) {
        return input.equals(input.trim()) && input.matches("^[a-zA-Z]{1,50}$");
    }

    private List<Word> findPartialWords(String keyword) {
        return baseRepository.getWordsList().stream()
                .filter(word -> word.getWord().toLowerCase().contains(keyword))
                .collect(Collectors.toList());
    }
}
