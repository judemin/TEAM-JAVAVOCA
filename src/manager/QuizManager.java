package manager;

import data.entity.Word;
import data.repository.BaseRepository;
import enums.FilePath;
import io.BaseIO;
import io.WrongFileIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;


/**
 * 퀴즈를 관리하고 정답을 판정하며 힌트를 제공합니다.
 */
public class QuizManager {

    /**
     * words: Word List
     * baseRepository: 오답 목록 시, 가져올 WordRepository
     * baseIO: 파일 저장을 위한 클래스
     */
    private final BaseRepository savedWordRepository;
    private final BaseRepository wrongWordRepository;
    private final BaseIO wrongFileIO;

    /**
     * QuizManager를 초기화합니다.
     */
    public QuizManager(BaseRepository savedWordRepository, BaseRepository wrongWordRepository, BaseIO wrongFileIO) {
        this.savedWordRepository = savedWordRepository;
        this.wrongWordRepository = wrongWordRepository;
        this.wrongFileIO = wrongFileIO;
    }

    /**
     * 단어 퀴즈 모드 선택 및 진행을 처리합니다.
     * 일반 퀴즈와 오답 퀴즈 모드 중 선택하여 퀴즈를 시작합니다.
     *
     * @throws IOException 퀴즈 진행 중 입출력 오류가 발생한 경우
     */
    public void handleQuizMenu() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.println("====== 퀴즈 모드 선택 ======");
                System.out.println("1. 일반 단어 퀴즈");
                System.out.println("2. 오답 단어 퀴즈");
                System.out.println("0. 뒤로가기");
                System.out.print("번호를 선택하세요: ");
                String input = br.readLine();

                switch (input) {
                    case "1":
                        startQuiz(false);
                        break;
                    case "2":
                        startQuiz(true);
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
                }
            }
        } catch (IOException e) {
            System.out.println("입력 중 오류가 발생했습니다.");
        }
    }

    /**
     * 퀴즈를 시작합니다. 일반 모드 또는 오답 모드 중 하나를 선택하여 지정된 단어들로 문제를 출제합니다.
     *
     * @param useWrongMode true이면 오답 퀴즈 모드로, false이면 일반 모드로 퀴즈를 진행합니다.
     * @throws IOException 퀴즈 진행 중 입출력 오류가 발생한 경우
     */
    public void startQuiz(boolean useWrongMode) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        List<Word> quizList = useWrongMode ? wrongWordRepository.getWordsList() : savedWordRepository.getWordsList();

        if (quizList.isEmpty()) {
            System.out.println("문제가 없습니다. 퀴즈를 진행할 수 없습니다.");
            return;
        }

        Collections.shuffle(quizList);
        int correctCount = 0;

        for (Word question : quizList) {
            System.out.println("뜻: " + question.getMeaning());
            System.out.print("입력: ");
            String userAnswer = br.readLine();

            if (checkAnswer(question, userAnswer)) {
                System.out.println("정답입니다!");
                correctCount++;
            } else {
                System.out.println("오답입니다.");
                File wrongFile = FileManager.getFile(FilePath.WRONG_ANSWERS);
                wrongFileIO.addWord(wrongFile, question);
                wrongWordRepository.addWord(question);
            }
            System.out.println();
        }
        System.out.println("퀴즈 종료! 맞힌 개수: " + correctCount + "/" + quizList.size());
    }

    /**
     * 사용자 입력 답안을 확인하여 정답 여부를 반환합니다.
     *
     * @param question   문제로 출제된 Word 객체 (뜻풀이를 사용자에게 제시한 단어)
     * @param userAnswer 사용자가 입력한 답안 (영단어)
     * @return 정답이면 true, 오답이면 false
     */
    public boolean checkAnswer(Word question, String userAnswer) {
        // 양쪽 공백 제거 후, 대소문자 구분 없이 정답 여부 반환
        return question.getWord().equalsIgnoreCase(userAnswer.trim());
    }

    /**
     * 지정된 단어에 대해 힌트를 제공합니다. 힌트 레벨에 따라 단어의 일부 문자 정보를 반환합니다.
     *
     * @param question  문제로 출제된 Word 객체
     * @param hintLevel 힌트 레벨 (1이면 첫 글자, 2이면 마지막 글자 제공)
     * @return 힌트 문자열
     */
    public String getHint(Word question, int hintLevel) {
        String word = question.getWord();
        switch (hintLevel) {
            case 1:
                return "첫 글자: " + word.charAt(0);
            case 2:
                return "마지막 글자: " + word.charAt(word.length() - 1);
            default:
                return "힌트가 없습니다.";
        }
    }
}
