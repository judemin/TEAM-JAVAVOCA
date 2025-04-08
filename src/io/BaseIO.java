package io;

import data.Word;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BaseIO {

    // 메모리 상의 단어 목록
    private static List<Word> words = new ArrayList<>();
    private static final int NOT_EXIST = -1;
    private static final String EMPTY_STRING = "";

    /**
     * 단어 데이터 파일(words.txt)을 읽어서 오답 단어 목록을 초기화합니다.
     *
     * @param filePath 읽을 데이터 File 객체
     * @return 단어 레코드 목록
     * @throws IOException 파일을 읽는 중 오류가 발생한 경우
     */
    public static void loadWords(String filePath) throws IOException {
        // IOException 발생시키면 파일 무결성에 문제가 있다는 뜻
        // new IOException
//        throw new IOException("파일 무결성 오류: " + filePath);

    }

    public static void checkFileIntegrity(File file) throws IOException {
        // IOException 발생시키면 파일 무결성에 문제가 있다는 뜻
        // new IOException
//        throw new IOException("파일 무결성 오류: " + filePath);

        BufferedReader br = new BufferedReader(new FileReader(file));
        ArrayList<String> errorLineList = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {  // 파일 끝(null)을 만날 때까지 읽기
            checkLineIntegrity(line, errorLineList);
        }
        if(!errorLineList.isEmpty()){
            System.out.println("파일의 중대한 결함 문제: 다음의 행이 문법 형식에 위배됩니다.");
            errorLineList.forEach(System.out::println);
            System.out.println("프로그램을 종료합니다.");
            System.exit(1);
        }

    }

    /**
     *
     * @throws IOException 파일 무결성 위반 또는 읽기 도중 오류
     *
     */
    private static void checkLineIntegrity(String line, ArrayList<String> errorLineList) throws IOException {
        // 줄에 : 이 없으면
        if (line.indexOf(':') == NOT_EXIST) {
            errorLineList.add(line);
            return;
        }
        // 줄에 :이 두 개 이상 있으면
        if (line.indexOf(':') != line.lastIndexOf(':')) {
            errorLineList.add(line);
            return;
        }
        String[] parts = line.split(":", 2);
        String word = parts[0].trim();
        String explanation = parts[1].trim();


        // page 14: 단어 문법 형식
        // 단어의 길이는 1자 이상 50자 이하이어야 합니다.
        if (word.length() > 50 || word.equals(EMPTY_STRING)) {
            errorLineList.add(line);
            return;
        }
        //전체 문자열은 알파벳(A-Z, a-z)으로만 구성됩니다.
        //탭(\t)이나 개행 문자(\n), 문자열 내부 공백은 허용되지 않습니다.
        String regexOfOnlyAlphabets = "^[A-Za-z]+$";
        if (!word.matches(regexOfOnlyAlphabets)) {
            errorLineList.add(line);
            return;
        }

        // page 15: 뜻풀이 문법 형식
        // 길이가 1이상 255자 이하이어야 합니다.
        // 공백만으로 이루어진 문자열이 아니어야 합니다.
        if (explanation.length() > 255 || explanation.equals(EMPTY_STRING)) {
            errorLineList.add(line);
            return;
        }
        // 영어 알파벳(A-Z, a-z) 및 표준 공백으로만 구성되어야 합니다.
        if (!explanation.matches("^[A-Za-z ]+$")) {
            errorLineList.add(line);
            return;
        }

    }

    /**
     * 현재 단어 목록을 파일에 저장합니다.
     *
     * @param filePath 단어 데이터 파일 경로
     * @throws IOException 파일을 쓰는 중 오류가 발생한 경우
     */
    public static void saveWords(String filePath) throws IOException {
    }

    /**
     * 새로운 단어를 목록에 추가하고 파일에 저장합니다.
     *
     * @param word 추가할 Word 객체
     * @param filePath 단어 데이터 파일 경로
     * @throws IOException 파일을 쓰는 중 오류가 발생한 경우
     */
    public static void addWord(String filePath, Word word) throws IOException {
    }

    /**
     * 단어 목록에서 해당 단어를 제거하고 파일을 갱신합니다.
     *
     * @param word 제거할 Word 객체
     * @param filePath 단어 데이터 파일 경로
     * @throws IOException 파일을 쓰는 중 오류가 발생한 경우
     */
    public static void removeWord(String filePath, Word word) throws IOException {
    }

    /**
     * 현재 메모리상의 단어 목록을 반환합니다.
     *
     * @return Word 객체 목록
     */
    public List<Word> getWordList() {
        return words;
    }
}
