package data.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

/**
 * 영어 단어와 뜻풀이를 담는 클래스입니다.
 */

@Getter
@Setter
@AllArgsConstructor
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
}
