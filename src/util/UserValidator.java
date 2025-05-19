package util;

import java.util.regex.Pattern;

public class UserValidator {

    // 대소문자, 숫자 각각 1개 이상 포함 + 총 1~20자 (특수문자/공백 불가)
    private static final Pattern VALID_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{1,20}$");

    /**
     * 아이디 혹은 비밀번호가 유효한 형식인지 확인
     */
    public static boolean isValid(String input) {
        return input != null && VALID_PATTERN.matcher(input).matches();
    }
}
