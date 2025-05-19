package data.entity;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 영어 단어 학습 프로그램의 사용자 정보를 나타내는 클래스입니다.
 * 사용자 정보는 고유 아이디와 비밀번호로 구성됩니다.
 */
public class User {

    private static final Pattern VALID_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{1,20}$");

    private String userId;
    private String password;

    /**
     * 정적 팩토리 메서드. 아이디와 비밀번호를 받아 유저 객체를 생성합니다.
     * @param userId 사용자 아이디
     * @param password 사용자 비밀번호
     * @return 유효성 검사를 통과한 User 객체
     * @throws IllegalArgumentException 아이디 또는 비밀번호 형식이 잘못된 경우
     */
    public static User of(String userId, String password) {
        validate("아이디", userId);
        validate("비밀번호", password);
        return new User(userId, password);
    }

    /**
     * 유효성 검사 메서드 - 아이디와 비밀번호 공통 포맷을 검사
     */
    private static void validate(String fieldName, String value) {
        if (value == null || !VALID_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    fieldName + "는 1~20자의 영문 대소문자와 숫자를 모두 포함해야 하며, 공백이나 특수문자는 허용되지 않습니다.");
        }
    }

    public User(String userId, String password) {
        validate("아이디", userId);
        validate("비밀번호", password);
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        validate("아이디", userId);
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        validate("비밀번호", password);
        this.password = password;
    }

    /**
     * 문자열 표현 - 디버깅 또는 로그용. 비밀번호는 포함하지 않음.
     */
    @Override
    public String toString() {
        return "UserId: " + userId;
    }

    /**
     * 사용자 비교 - 아이디와 비밀번호 모두 대소문자 구분하여 비교
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User other = (User) obj;
        return Objects.equals(this.userId, other.userId) &&
                Objects.equals(this.password, other.password);
    }

    /**
     * 해시코드 - userId와 password 기준
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId, password);
    }
}
