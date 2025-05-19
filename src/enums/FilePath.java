package enums;

public enum FilePath {
    // WRONG_ANSWERS는 사용할 때 항상 유저 아이디를 앞에 붙여줘야 함!
    WORDS("words.txt"), WRONG_ANSWERS("_wrong_answers.txt"), USER_INFO("users.txt");

    final String path;

    public String getPath() {
        return path;
    }

    private FilePath(String path) {
        this.path = path;
    }
}
