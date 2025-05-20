package io;

import data.entity.Word;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class BaseIO {

    /**
     * 주어진 단어를 파일에 저장합니다.
     * @param file 단어 데이터 파일 경로
     * @throws IOException 파일을 쓰는 중 오류가 발생한 경우
     * @author 기찬, 승우
     */
    public void addWordToFile(File file, Word word) throws IOException{

        // try-with-resources 구문 사용
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.newLine(); // 새로운 줄로 이동 (없어도 되지만 가독성 위해)
            writer.write(word.toString());
        } catch (IOException e) {
            exitProgram();
        }

    }

    /**
     * 주어진 단어를 파일에서 삭제합니다.
     * @param file 단어 데이터 파일 경로
     * @throws IOException 파일을 쓰는 중 오류가 발생한 경우
     * @author 기찬, 승우
     */
    public void removeWordInFile(File file, Word word) throws IOException{
        ArrayList<String> filteredLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {  // 파일 끝(null)을 만날 때까지 읽기
                if(line.trim().isEmpty()){
                    filteredLines.add(line);
                    continue;
                }

                String[] parts = line.split(":", 2);
                String engWord = parts[0].trim();
                String explanation = parts[1].trim();

                if (!engWord.equalsIgnoreCase(word.getWord())) {
                    // 중복이 없으면 기록하고, 유효한 라인 목록에 추가
                    filteredLines.add(line);
                }

            }

        } catch (Exception e) {
            exitProgram();
        }

        try {
            Files.write(file.toPath(), filteredLines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            exitProgram();
        }

    }

    /**
     * 주어진 단어를 파일에서 삭제합니다.
     * @param file 단어 데이터 파일 경로
     * @throws IOException 파일을 쓰는 중 오류가 발생한 경우
     * @author 기찬, 승우
     */
    public void editWordInFile(File file, Word word) throws IOException{
        ArrayList<String> filteredLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {  // 파일 끝(null)을 만날 때까지 읽기
                if(line.trim().isEmpty()){
                    filteredLines.add(line);
                    continue;
                }

                String[] parts = line.split(":", 2);
                String engWord = parts[0].trim();
                String explanation = parts[1].trim();

                if (engWord.equalsIgnoreCase(word.getWord())) {
                    String[] temp = line.split(explanation);
                    if(temp.length >= 2){
                        filteredLines.add(temp[0] + word.getMeaning() + temp[1]);
                    } else {
                        filteredLines.add(temp[0] + word.getMeaning());
                    }
                } else {
                    filteredLines.add(line);
                }
            }

        } catch (Exception e) {
            exitProgram();
        }

        try {
            Files.write(file.toPath(), filteredLines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            exitProgram();
        }

    }

    /**
     * 새로운 단어를 목록에 추가하고 파일에 저장합니다.
     *
     * @param word 추가할 Word 객체
     * @param file 단어 데이터 파일 경로
     * @throws IOException 파일을 쓰는 중 오류가 발생한 경우
     * @author 기찬
     */
    public abstract void addWord(File file, Word word) throws IOException;

    /**
     * 단어 목록에서 해당 단어를 제거하고 파일을 갱신합니다.
     *
     * @param word 제거할 Word 객체
     * @param file 단어 데이터 파일 경로
     * @throws IOException 파일을 쓰는 중 오류가 발생한 경우
     * @author 기찬
     */
    public abstract void removeWord(File file, Word word) throws IOException;

    void writeWordListInFile(File file, List<Word> wordList) throws IOException {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Word word : wordList) {
                writer.write(word.getWord() + " : " + word.getMeaning());
                writer.newLine();
            }
        }
    }

    void addWordIfDistinct(File file, Word word, List<Word> wordList) throws IOException {
        if (isDistinct(word, wordList)) {
            wordList.add(word);
            addWordToFile(file, word);
        }
    }

    void removeWordIfExists(File file, Word word, List<Word> wordList) throws IOException {
        boolean removed = wordList.removeIf(w -> w.equals(word));
        if (removed) {
            removeWordInFile(file, word);
        } else {
            System.out.println("해당 단어를 찾을 수 없습니다.");
        }
    }

    static boolean isDistinct(Word word, List<Word> wordList) {
        for (Word w : wordList) {
            if (w.equals(word)) {
                System.out.println("이미 존재하는 단어입니다. 다시 입력해 주세요.");
                return false;
            }
            if (w.getMeaning().equals(word.getMeaning())) {
                System.out.println("이미 존재하는 뜻풀이입니다. 다시 입력해 주세요.");
                return false;
            }
        }
        return true;
    }

    static void exitProgram() {
        System.out.println("프로그램을 종료합니다.");
        System.exit(1);
    }

    public void IncrementWrongCount(Word question, File file) {}
}
