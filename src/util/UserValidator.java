package util;

import java.util.regex.Pattern;

public class UserValidator {
    private static final Pattern VALID_USER_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{1,20}$");

    public static boolean isValid(String input) {
        return input != null && VALID_USER_PATTERN.matcher(input).matches();
    }
}
