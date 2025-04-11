package manager;

import data.entity.Word;
import data.repository.BaseRepository;
import enums.FilePath;
import io.BaseIO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * 단어 데이터를 관리하는 클래스입니다.
 * 단어 목록을 불러오고 추가/수정/삭제 기능을 제공합니다.
 */
public class WordManager {

    /**
     * baseRepository: 가져올 WordRepository
     * baseIO: 파일 저장을 위한 클래스
     */
    private final BaseRepository baseRepository;
    private final BaseIO baseIO;

    /**
     * WordManager를 초기화합니다.
     */
    public WordManager(BaseRepository baseRepository, BaseIO baseIO) {
        this.baseRepository = baseRepository;
        this.baseIO = baseIO;
    }

    /**
     * 현재 메모리상의 전체 단어 목록을 반환합니다.
     *
     * @return Word 객체 목록
     */
    public List<Word> getAllWords() {
        return baseRepository.getWordsList();
    }

    /**
     * 단어 레코드 관리 기능을 처리합니다.
     * 단어 추가, 수정, 삭제 중 하나를 선택하고 해당 기능을 실행합니다.
     */
    public void handleWordManagementMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== 단어 관리 메뉴 ===");
            System.out.println("1. 단어 추가");
            System.out.println("2. 단어 수정");
            System.out.println("3. 단어 삭제");
            System.out.println("0. 종료");
            System.out.print("선택: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 개행 제거

            try {
                if (choice == 1) {
                    System.out.print("추가할 단어: ");
                    String word = scanner.nextLine();
                    System.out.print("뜻풀이: ");
                    String meaning = scanner.nextLine();
                    if (addWord(word, meaning)) {
                        System.out.println("단어가 추가되었습니다.");
                    } else {
                        System.out.println("이미 존재하는 단어입니다.");
                    }
                } else if (choice == 2) {
                    System.out.print("수정할 기존 단어: ");
                    String original = scanner.nextLine();
                    System.out.print("새 단어: ");
                    String newWord = scanner.nextLine();
                    System.out.print("새 뜻풀이: ");
                    String newMeaning = scanner.nextLine();
                    if (updateWord(original, newWord, newMeaning)) {
                        System.out.println("단어가 수정되었습니다.");
                    } else {
                        System.out.println("해당 단어를 찾을 수 없습니다.");
                    }
                } else if (choice == 3) {
                    System.out.print("삭제할 단어: ");
                    String word = scanner.nextLine();
                    if (removeWord(word)) {
                        System.out.println("단어가 삭제되었습니다.");
                    } else {
                        System.out.println("해당 단어를 찾을 수 없습니다.");
                    }
                } else if (choice == 0) {
                    break;
                } else {
                    System.out.println("잘못된 입력입니다.");
                }
            } catch (IOException e) {
                System.out.println("파일 저장 중 오류가 발생했습니다: " + e.getMessage());
            }
        }
    }

    /**
     * 새로운 단어를 추가합니다. 단어 목록을 갱신하고 파일에 저장합니다.
     *
     * @param word    추가할 영어 단어
     * @param meaning 추가할 단어의 뜻풀이
     * @return 추가 성공 시 true, 실패 시 false (예: 중복 단어인 경우)
     * @throws IOException 파일 저장 중 오류가 발생한 경우
     */
    public boolean addWord(String word, String meaning) throws IOException {
        for (Word w : baseRepository.getWordsList()) {
            if (w.getWord().equalsIgnoreCase(word)) {
                return false; // 중복 단어
            }
        }
        baseRepository.addWord(new Word(word, meaning));
        baseIO.addWord(FileManager.getFile(FilePath.WORDS), new Word(word, meaning));
        return true;
    }

    /**
     * 기존 단어의 뜻풀이 또는 단어 자체를 수정합니다. 단어 목록을 갱신하고 파일에 저장합니다.
     *
     * @param originalWord 수정할 대상 단어 (영어)
     * @param newWord      변경할 영단어 (동일하면 단어는 수정되지 않음)
     * @param newMeaning   변경할 뜻풀이
     * @return 수정 성공 시 true, 대상 단어를 찾을 수 없으면 false
     * @throws IOException 파일 저장 중 오류가 발생한 경우
     */
    public boolean updateWord(String originalWord, String newWord, String newMeaning) throws IOException {
        for (Word w : baseRepository.getWordsList()) {
            if (w.getWord().equalsIgnoreCase(originalWord)) {
                baseIO.removeWord(FileManager.getFile(FilePath.WORDS), w);
                baseRepository.removeWord(w);

                w.setWord(newWord);
                w.setMeaning(newMeaning);

                baseIO.addWord(FileManager.getFile(FilePath.WORDS), w);
                baseRepository.addWord(w);
                return true;
            }
        }
        return false;
    }

    /**
     * 단어를 목록에서 제거합니다. 단어 목록을 갱신하고 파일을 저장합니다.
     *
     * @param word 제거할 영어 단어
     * @return 제거 성공 시 true, 제거할 단어를 찾지 못한 경우 false
     * @throws IOException 파일 저장 중 오류가 발생한 경우
     */
    public boolean removeWord(String word) throws IOException {
        Iterator<Word> iterator = baseRepository.getWordsList().iterator();
        while (iterator.hasNext()) {
            Word w = iterator.next();
            if (w.getWord().equalsIgnoreCase(word)) {
                iterator.remove();
                baseIO.removeWord(FileManager.getFile(FilePath.WORDS), w);
                return true;
            }
        }
        return false;
    }
}
