package manager;

import data.Word;
import io.WrongFileIO;
import java.io.IOException;
import java.util.List;

/**
 * 퀴즈를 관리하고 정답을 판정하며 힌트를 제공합니다.
 */
public class QuizManager {

    // words 전체 단어 목록
    // wrongFileIO 오답 단어 FileIO 객체
    private List<Word> words;
    private WrongFileIO wrongFileIO;

    /**
     * QuizManager를 초기화합니다.
     */
    public QuizManager() {
    }

    /**
     * 단어 퀴즈 모드 선택 및 진행을 처리합니다.
     * 일반 퀴즈와 오답 퀴즈 모드 중 선택하여 퀴즈를 시작합니다.
     */
    public void handleQuizMenu() {
    }

    /**
     * 퀴즈를 시작합니다. 일반 모드 또는 오답 모드 중 하나를 선택하여 지정된 단어들로 문제를 출제합니다.
     *
     * @param useWrongMode true이면 오답 퀴즈 모드로, false이면 일반 모드로 퀴즈를 진행합니다.
     * @throws IOException 퀴즈 진행 중 입출력 오류가 발생한 경우
     */
    public void startQuiz(boolean useWrongMode) throws IOException {
    }

    /**
     * 사용자 입력 답안을 확인하여 정답 여부를 반환합니다.
     *
     * @param question   문제로 출제된 Word 객체 (뜻풀이를 사용자에게 제시한 단어)
     * @param userAnswer 사용자가 입력한 답안 (영단어)
     * @return 정답이면 true, 오답이면 false
     */
    public boolean checkAnswer(Word question, String userAnswer) {
        return false;
    }

    /**
     * 지정된 단어에 대해 힌트를 제공합니다. 힌트 레벨에 따라 단어의 일부 문자 정보를 반환합니다.
     *
     * @param question  문제로 출제된 Word 객체
     * @param hintLevel 힌트 레벨 (1이면 첫 글자, 2이면 마지막 글자 제공)
     * @return 힌트 문자열
     */
    public String getHint(Word question, int hintLevel) {
        return null;
    }
}
