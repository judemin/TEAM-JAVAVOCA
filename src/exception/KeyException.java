package exception;

/**
 * 단어 및 뜻풀이 입력에 대한 예외를 처리하기 위한 예외 클래스입니다.
 * 단어 입력은 1자 이상 50자 이하의 영어 알파벳으로만 구성되어야 하며,
 * 내부에 공백, 탭, 개행 문자가 포함되어서는 안 됩니다.
 * 뜻풀이 입력은 1자 이상 255자 이하의 영어 알파벳과 표준 공백만 포함해야 하며,
 * 문자열 앞뒤의 불필요한 공백은 허용되지 않습니다.
 * <p>
 * 메뉴 및 기능 선택시의 Exception은 각 Class에서 담당합니다.
 */
public class KeyException extends Exception {

    /**
     * 기본 생성자입니다.
     */
    public KeyException() {
        super();
    }

    /**
     * 상세 메시지를 포함한 생성자입니다.
     *
     * @param message 예외에 대한 상세 설명
     */
    public KeyException(String message) {
        super(message);
    }

    /**
     * 상세 메시지와 원인 Throwable을 포함한 생성자입니다.
     *
     * @param message 예외에 대한 상세 설명
     * @param cause   예외의 원인
     */
    public KeyException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 원인 Throwable을 포함한 생성자입니다.
     *
     * @param cause 예외의 원인
     */
    public KeyException(Throwable cause) {
        super(cause);
    }

    /**
     * 입력된 단어가 유효한지 검증합니다.
     * 유효한 단어는 다음 규칙을 만족해야 합니다:
     * - 길이가 1자 이상 50자 이하
     * - 영어 알파벳(A-Z, a-z)로만 구성되어야 하며,
     * 탭이나 개행 문자, 내부 공백이 포함되어서는 안 됩니다.
     *
     * @param word 사용자로부터 입력된 단어 문자열
     * @throws KeyException 단어가 규칙에 어긋난 경우
     */
    public static void validateWord(String word) throws KeyException {
        // word가 null 이거나, 빈 문자열인지 검사
        if (word == null || word.isEmpty()) {
            throw new KeyException("단어는 1자 이상 50자 이하로 입력해야 합니다.");
        }

        // 정규식: 영어 알파벳만 포함하고 공백, 탭, 개행이 없어야 함
        if (!word.matches("^[A-Za-z]{1,50}$")) {
            throw new KeyException("단어는 영어 알파벳으로만 구성되어야 하며, 공백이나 특수 문자를 포함할 수 없습니다.");
        }
    }

    /**
     * 입력된 뜻풀이가 유효한지 검증합니다.
     * 유효한 뜻풀이 문자열은 다음 규칙을 만족해야 합니다:
     * - 길이가 1자 이상 255자 이하
     * - 영어 알파벳(A-Z, a-z) 및 표준 공백으로만 구성되어야 하며,
     * 문자열 앞뒤의 불필요한 공백이 없어야 합니다.
     *
     * @param meaning 사용자로부터 입력된 뜻풀이 문자열
     * @throws KeyException 뜻풀이가 규칙에 어긋난 경우
     */
    public static void validateMeaning(String meaning) throws KeyException {
        // meaning이 null 인지 검사
        if (meaning == null) {
            throw new KeyException("뜻풀이가 null일 수 없습니다.");
        }

        // 앞뒤 공백이 있으면 예외 처리
        if (!meaning.equals(meaning.trim())) {
            throw new KeyException("뜻풀이 앞뒤에 공백이 있으면 안 됩니다.");
        }

        // 정규식: 알파벳과 공백, 255 글자만 허용
        if (!meaning.matches("^[A-Za-z ]{1,255}$")) {
            throw new KeyException("뜻풀이는 1자 이상 255자 이하의 영어 알파벳과 공백만 포함해야 합니다.");
        }
    }
}
