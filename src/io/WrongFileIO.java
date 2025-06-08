package io;

import data.entity.Word;
import data.repository.WrongWordRepository;
import enums.FilePath;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static manager.FileManager.getCurrentPath;

/**
 * 오답 데이터 파일을 관리하는 클래스입니다.
 */
public class WrongFileIO extends BaseIO {
    private static final WrongFileIO wrongFileIO;
    private static final int WRONG_COUNT_UPPER_BOUND = 10;

    private static String userId;

    public static void setUserId(String userId) {
        WrongFileIO.userId = userId;
    }

    static {
        wrongFileIO = new WrongFileIO();
    }

    public static WrongFileIO getInstance() {
        return wrongFileIO;
    }

    @Override
    public void addWord(File file, Word word) throws IOException {
        List<Word> wordList = WrongWordRepository.getInstance().getWordsList();
        List<Integer> wrongCountList = WrongWordRepository.getInstance().getWrongCountList();
        addWrongWordIfDistinct(file, word, wordList,wrongCountList);
    }

    void addWrongWordIfDistinct(File file, Word word, List<Word> wordList, List<Integer> wrongCountList) throws IOException {
        if (isDistinct(word, wordList)) {
            wordList.add(word);
            wrongCountList.add(1);
            addWordToFile(file, word);
        }
    }

    @Override
    public void addWordToFile(File file, Word word) throws IOException{

        // try-with-resources 구문 사용
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.newLine(); // 새로운 줄로 이동 (없어도 되지만 가독성 위해)
            writer.write(word.toString() + ": 1");
        } catch (IOException e) {
            exitProgram();
        }

    }

    @Override
    public void removeWord(Word word) throws IOException {
        if (userId == null){
            return;
        }
        String wrongFileName = userId + FilePath.WRONG_ANSWERS.getPath();
        removeWord(new File(getCurrentPath(),wrongFileName), word);
    }

    @Override
    public void removeWord(File file, Word word) throws IOException {
        List<Word> wordList = WrongWordRepository.getInstance().getWordsList();
        removeWordIfExists(file, word, wordList);
    }

    @Override
    void removeWordIfExists(File file, Word word, List<Word> wordList) throws IOException {
        boolean removed = false;
        int i;
        for(i=0;i<wordList.size();i++){
            if(wordList.get(i).equals(word)){
                wordList.remove(i);
                removed = true;
                break;
            }
        }

        if (removed) {
            WrongWordRepository.getInstance().getWrongCountList().remove(i);
            removeWordInFile(file, word);
        } else {
            System.out.println("해당 단어를 찾을 수 없습니다.");
        }

        // TODO 파일 처리 부분 여기에 추가해 줘야함
    }

    @Override
    public void IncrementWrongCount(Word question, File file) {
        WrongWordRepository wwr = WrongWordRepository.getInstance();
        if(wwr.getCount(question) >= WRONG_COUNT_UPPER_BOUND){
            return;
        }
        int changedCount = wwr.getCount(question) + 1;
        wwr.setCount(question, changedCount);

        try {
            Path path = file.toPath();
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            List<String> updated = new ArrayList<>();

            for (String line : lines) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    // 빈 줄은 그대로
                    updated.add(line);
                    continue;
                }
                // ':' 첫 분리만 써서 key 추출
                String key = trimmed.split(":", 2)[0].trim();
                if (key.equals(question.getWord())) {
                    // parts 마지막 필드를 변경
                    String[] parts = line.split(":", -1);
                    parts[parts.length - 1] = " " + changedCount;
                    updated.add(String.join(":", parts));
                } else {
                    updated.add(line);
                }
            }

            Files.write(path, updated, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("파일 업데이트 중 오류 발생: " + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void editWrongWord(Word word) {
        String wrongFileName = userId + FilePath.WRONG_ANSWERS.getPath();
        File file = new File(getCurrentPath(),wrongFileName);
        BaseIO.editWrongWordInFile(file, word);
    }
}
