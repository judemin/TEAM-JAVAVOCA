package data;

/**
 * 영어 단어와 뜻풀이를 담는 클래스입니다.
 */

public class Word {

    private String word;
    private String meaning;

    /**
     * 기본 생성자. 빈 Word 객체를 생성합니다.
     */
    public Word() {
    }

    /**
     * 주어진 단어와 뜻풀이로 새로운 Word 객체를 생성합니다.
     *
     * @param word    영어 단어
     * @param meaning 단어의 뜻풀이
     */
    public Word(String word, String meaning) {
        this.word = word;
        this.meaning = meaning;
    }

    public static Word of(String word, String meaning){
        return new Word(word,meaning);
    }

    /**
     * 영어 단어를 반환합니다.
     *
     * @return 단어 문자열
     */
    public String getWord() {
        return null;
    }

    /**
     * 영어 단어를 설정합니다.
     *
     * @param word 설정할 영어 단어
     */
    public void setWord(String word) {
    }

    /**
     * 단어의 뜻풀이를 반환합니다.
     *
     * @return 뜻풀이 문자열
     */
    public String getMeaning() {
        return null;
    }

    /**
     * 단어의 뜻풀이를 설정합니다.
     *
     * @param meaning 설정할 뜻풀이
     */
    public void setMeaning(String meaning) {
    }

    /**
     * 단어 레코드의 문자열 표현을 반환합니다.
     *
     * @return "word: meaning" 형식의 문자열
     */
    @Override
    public String toString() {
        return null;
    }
}
