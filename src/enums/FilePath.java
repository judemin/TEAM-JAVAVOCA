package enums;

public enum FilePath {
    WORDS("words.txt"), WRONG_ANSWERS("wrong_answers.txt"), USER_INFO("users.txt");

    final String path;

    public String getPath() {
        return path;
    }

    private FilePath(String path) {
        this.path = path;
    }
}
