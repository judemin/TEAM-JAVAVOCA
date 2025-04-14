package manager;

import data.entity.Word;
import data.repository.BaseRepository;
import enums.FilePath;
import io.BaseIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
                System.out.println("1(일반 모드) / 2(오답 퀴즈 모드)");
                System.out.print("번호를 선택하세요: ");
                String input = br.readLine();
                String[] tokens = input.trim().split("\\s+");

                // 인자가 2개 이상일 경우
                if (tokens.length != 1) {
                    System.out.println(".!! 오류: 이 명령은 인자를 받지 않습니다!\n");
                    continue;
                }

                switch (tokens[0]) {
                    case "1":
                        startQuiz(false);
                        return;
                    case "2":
                        startQuiz(true);
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
    public boolean startQuiz(boolean useWrongMode) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        List<Word> originalList = useWrongMode
                ? new ArrayList<>(wrongWordRepository.getWordsList()) // ← 깊은 복사
                : savedWordRepository.getWordsList();

        int totalSize = originalList.size();
        System.out.println("→ 저장된 단어 수: " + totalSize + "개");

        if (totalSize == 0) {
            System.out.println("맞은 개수: 0개");
            System.out.println("틀린 개수: 0개");
            return false;
        }

        List<Word> quizList;
        if (totalSize < 10) {
            if (useWrongMode) System.out.println("→ 오답 단어가 10개 미만이므로 모두 출제합니다.");
            else System.out.println("→ 모든 단어를 퀴즈로 출제합니다.");
            quizList = originalList;
        } else {
            System.out.println("→ 무작위로 10개의 단어를 선택하여 퀴즈를 출제합니다.");
            Collections.shuffle(originalList);
            quizList = originalList.subList(0, 10);
        }

        int correctCount = 0;
        int wrongCount = 0;

        for (int i = 0; i < quizList.size(); i++) {
            Word question = quizList.get(i);
            String answer = question.getWord();

            System.out.println((i + 1) + ". " + question.getMeaning());
            System.out.println("글자수: " + answer.length());

            int attempts = 0;
            boolean isCorrect = false;

            while (attempts < 3) {
                System.out.print("> ");
                String rawInput = br.readLine();
                String userInput = rawInput.trim();

                if (!userInput.matches("^[a-zA-Z]{1,50}$") || !rawInput.equals(userInput)) {
                    System.out.println(".!! 오류: 올바른 단어 형식을 입력해주세요. (공백 미 포함 영어만 입력가능)");
                    continue;
                }

                if (userInput.equalsIgnoreCase(answer)) {
                    correctCount++;
                    isCorrect = true;

                    // 오답 퀴즈 모드일 경우: 정답 맞추면 해당 단어 오답 파일에서 제거
                    if (useWrongMode) {
                        File wrongFile = FileManager.getFile(FilePath.WRONG_ANSWERS);
                        wrongFileIO.removeWord(wrongFile, question);
                        System.out.println("맞았습니다! 위 단어는 오답 데이터 파일에서 삭제됩니다.");
                    } else {
                        System.out.println("맞았습니다!");
                    }

                    break;
                } else {
                    attempts++;
                    if (attempts == 1) {
                        System.out.println("틀렸습니다! 시도 횟수: 1, 첫글자: " + answer.charAt(0));
                    } else if (attempts == 2) {
                        System.out.println("틀렸습니다! 시도 횟수: 2, 마지막글자: " + answer.charAt(answer.length() - 1));
                    } else {
                        System.out.println("시도 횟수가 초과되었습니다. 다음 문제로 넘어갑니다.");
                    }
                }
            }

            if (!isCorrect) {
                // 일반 모드에서만 오답 파일에 저장
                if (!useWrongMode) {
                    File wrongFile = FileManager.getFile(FilePath.WRONG_ANSWERS);
                    wrongFileIO.addWord(wrongFile, question);
                }
                wrongCount++;
            }

            System.out.println();
        }

        System.out.println("맞은 개수: " + correctCount + "개");
        System.out.println("틀린 개수: " + wrongCount + "개");
        return true;
    }
}
