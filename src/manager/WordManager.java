package manager;

import data.entity.Word;
import data.repository.BaseRepository;
import enums.FilePath;
import io.BaseIO;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class WordManager {
    private static final Pattern WORD_PATTERN = Pattern.compile("^[a-zA-Z]+$");
    private static final Pattern MEANING_PATTERN = Pattern.compile("^[a-zA-Z ]+$");

    private Scanner scanner;
    private final BaseRepository baseRepository;
    private final BaseIO wordFileIO;

    public WordManager(Scanner scanner, BaseRepository baseRepository, BaseIO wordFileIO) {
        this.scanner = scanner;
        this.baseRepository = baseRepository;
        this.wordFileIO = wordFileIO;
    }

    public void run() throws IOException {
        while (true) {
            System.out.println("1. 단어 추가");
            System.out.println("2. 단어 수정");
            System.out.println("3. 단어 제거");
            System.out.print("Javavoca > ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    addWord();
                    break;
                case "2":
                    updateWord();
                    break;
                case "3":
                    removeWord();
                    break;
                default:
                    return;
            }
        }
    }

    private void addWord() throws IOException {
        String word = promptWord();
        if (word == null) return;

        String meaning = promptMeaningWithDuplicateCheck(word);

        if (confirmSave(word, meaning)) {
            saveWord(word, meaning);
            System.out.println("단어가 저장되었습니다.");
        }
    }

    private void removeWord() throws IOException {
        List<Word> records = baseRepository.getWordsList();

        if (records.isEmpty()) {
            System.out.println("단어 데이터 파일에 데이터가 존재하지 않습니다!");
            return;
        }

        String word = removePromptWord();
        Word existingWord = records.stream()
                .filter(w -> w.getWord().equalsIgnoreCase(word))
                .findFirst()
                .orElse(null);

        if (existingWord == null) {
            System.out.println("기존 단어가 존재하지 않습니다.");
            return;
        }

        System.out.println("정말 삭제하시겠습니까? (.../No) > ");
        if (!scanner.nextLine().trim().equals("No")) {
            wordFileIO.removeWord(FileManager.getFile(FilePath.WORDS), existingWord); // 파일 전체 저장
            System.out.println("단어가 삭제되었습니다.");
        }
    }

    private void updateWord() throws IOException {
        List<Word> records = baseRepository.getWordsList();

        if (records.isEmpty()) {
            System.out.println("단어 데이터 파일에 데이터가 존재하지 않습니다!");
            return;
        }

        String word = removePromptWord();
        Word existingWord = records.stream()
                .filter(w -> w.getWord().equalsIgnoreCase(word))
                .findFirst()
                .orElse(null);

        if (existingWord == null) {
            System.out.println("기존 단어가 존재하지 않습니다.");
            return;
        }

        String meaning = promptMeaningWithDuplicateCheck(word);

        if (confirmSave(word, meaning)) {
            records.remove(existingWord);
            records.add(Word.of(word, meaning));
            wordFileIO.editWordInFile(FileManager.getFile(FilePath.WORDS),Word.of(word, meaning)); // 파일 전체 저장
            System.out.println("단어가 수정되었습니다.");
        }
    }

    private String removePromptWord() {
        while (true) {
            System.out.print("단어: ");
            String rawInput = scanner.nextLine();
            String word = rawInput.trim().toLowerCase();

            if (!WORD_PATTERN.matcher(word).matches() || !rawInput.equals(rawInput.trim())) {
                System.out.println(".!! 오류: 올바른 단어 형식을 입력해주세요. (공백 미 포함 영어만 입력가능)");
                continue;
            }
            return word;
        }
    }

    private String promptWord() {
        while (true) {
            System.out.print("단어: ");
            String rawInput = scanner.nextLine();
            String word = rawInput.trim().toLowerCase();

            if (!WORD_PATTERN.matcher(word).matches() || !rawInput.equals(rawInput.trim())) {
                System.out.println(".!! 오류: 올바른 단어 형식을 입력해주세요. (공백 미 포함 영어만 입력가능)");
                continue;
            }

            List<Word> wordsList = baseRepository.getWordsList();
            for (Word w : wordsList) {
                if (w.getWord().equalsIgnoreCase(word)) {
                    System.out.println("기존 단어와 같은 단어가 존재합니다.");
                    System.out.println("단어: " + w.getWord());
                    System.out.println("뜻: " + w.getMeaning());
                    return null;
                }
            }

            return word;
        }
    }

    private String promptMeaningWithDuplicateCheck(String word) {
        List<Word> wordsList = baseRepository.getWordsList();

        while (true) {
            System.out.print("뜻: ");
            String rawInput = scanner.nextLine();
            String meaning = rawInput.trim();

            if (!MEANING_PATTERN.matcher(meaning).matches() || !rawInput.equals(rawInput.trim())) {
                System.out.println(".!! 오류: 올바른 뜻 형식을 입력해주세요. (공백 포함 영어만 입력가능)");
                continue;
            }

            boolean duplicate = false;
            for (Word w : wordsList) {
                if (w.getMeaning().equals(meaning)) {
                    duplicate = true;
                    System.out.println("기존 뜻과 같은 뜻이 존재합니다.");
                    break;
                }
            }
            if (!duplicate) return meaning;
        }
    }

    private boolean confirmSave(String word, String meaning) {
        System.out.println("... 현재까지 입력한 단어입니다:");
        System.out.println("==> 단어: " + word);
        System.out.println("뜻: " + meaning);
        System.out.print("Javavoca: 정말 저장하시겠습니까? (.../No) > ");
        String input = scanner.nextLine().trim();
        return !input.equals("No");
    }

    private void saveWord(String word, String meaning) throws IOException {
        File wordfile = FileManager.getFile(FilePath.WORDS);
        wordFileIO.addWord(wordfile, Word.of(word, meaning));
    }

}
