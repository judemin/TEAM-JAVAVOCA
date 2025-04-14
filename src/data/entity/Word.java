package data.entity;

/**
 * 영어 단어와 뜻풀이를 담는 클래스입니다.
 */

public class Word {

    private String word;
    private String meaning;

    /**
     * 단어의 생성자 입니다.
     * @param word
     * @param meaning
     * @return
     */
    public static Word of(String word, String meaning) {
        return new Word(word, meaning);
    }

    /**
     * 단어 레코드의 문자열 표현을 반환합니다.
     *
     * @return "word: meaning" 형식의 문자열
     */
    @Override
    public String toString() {
        return this.word + ": " + this.meaning;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { // 동일 객체인지 확인
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Word other = (Word) obj;
        // word과 meaning를 대소문자 구분 없이 비교.
        // 만약 field가 null인 경우도 고려하여 비교합니다.
        boolean wordEquals = (this.word == null) ? (other.word == null)
                : this.word.equalsIgnoreCase(other.word);
        boolean meaningEquals = (this.meaning == null) ? (other.meaning == null)
                : this.meaning.equalsIgnoreCase(other.meaning);
        return wordEquals && meaningEquals;
    }

    @Override
    public int hashCode() {
        int result = 1;

        // word 대소문자 구분하지 않으므로 toLowerCase() (또는 toUpperCase()) 사용
        result = 31 * result + (this.word == null ? 0 : this.word.toLowerCase().hashCode());
        result = 31 * result + (this.meaning == null ? 0 : this.meaning.toLowerCase().hashCode());

        return result;
    }


    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public Word(String word, String meaning) {
        this.word = word;
        this.meaning = meaning;
    }
}
