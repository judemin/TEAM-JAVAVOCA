package manager;

import data.entity.Word;
import data.repository.BaseRepository;
import enums.FilePath;
import io.BaseIO;

import java.io.*;
import java.util.*;

/**
 * 퀴즈를 관리하고 정답을 판정하며 힌트를 제공합니다.
 */
public class QuizManager {

    private final BaseRepository savedWordRepository;
    private final BaseRepository wrongWordRepository;
    private final BaseIO wrongFileIO;
    private String currentUserId;

    public QuizManager(BaseRepository savedWordRepository, BaseRepository wrongWordRepository, BaseIO wrongFileIO) {
        this.savedWordRepository = savedWordRepository;
        this.wrongWordRepository = wrongWordRepository;
        this.wrongFileIO = wrongFileIO;
    }

    public void setCurrentUserId(String userId) {
        this.currentUserId = userId;
    }

    public void handleQuizMenu() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.println("====== 퀴즈 모드 선택 ======");
                System.out.println("1(일반 모드) / 2(오답 퀴즈 모드)");
                System.out.print("번호를 선택하세요: ");
                String input = br.readLine();

                switch (input) {
                    case "1":
                        startQuiz(false);
                        return;
                    case "2":
                        startQuiz(true);
                        return;
                    default:
                        System.out.println(".!! 오류: 1 또는 2 중에서 입력해주세요!");
                }
            }
        } catch (IOException e) {
            System.out.println("입력 중 오류가 발생했습니다.");
        }
    }

    private File getUserWrongFile() {
        // 사용자 홈 디렉토리에 저장
        String userHome = System.getProperty("user.home");
        return new File(userHome, currentUserId + "_wrong_answers.txt");
    }

    public boolean startQuiz(boolean useWrongMode) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        List<Word> originalList = new ArrayList<>();
        Set<String> seenWords = new HashSet<>();

        File userWrongFile = getUserWrongFile();

        // 파일 없으면 자동 생성
        if (!userWrongFile.exists()) {
            boolean created = userWrongFile.createNewFile();
            if (created) {
                System.out.println("> 사용자 오답 파일 생성됨: " + userWrongFile.getAbsolutePath());
            }
        }

        if (useWrongMode) {
            wrongWordRepository.getWordsList().clear();
            FileManager.loadFiles(userWrongFile, wrongWordRepository);
        }

        List<Word> sourceList = useWrongMode
                ? wrongWordRepository.getWordsList()
                : savedWordRepository.getWordsList();

        for (Word w : sourceList) {
            String key = w.getWord().toLowerCase();
            if (seenWords.add(key)) {
                originalList.add(w);
            }
        }

        int totalSize = originalList.size();
        if (useWrongMode) {
            System.out.println("→ 총 틀린 단어 수: " + totalSize + "개");
        } else {
            System.out.println("→ 저장된 단어 수: " + totalSize + "개");
        }

        if (totalSize == 0) {
            System.out.println("맞은 개수: 0개");
            System.out.println("틀린 개수: 0개");
            return false;
        }

        List<Word> quizList;

        if (useWrongMode && totalSize >= 10) {
            int D = totalSize;
            int W = savedWordRepository.getWordsList().size();

            originalList.sort((w1, w2) -> {
                int c1 = wrongWordRepository.count(w1.getWord());
                int c2 = wrongWordRepository.count(w2.getWord());
                double e1 = (double) c1 * D / (W + c1 * D);
                double e2 = (double) c2 * D / (W + c2 * D);
                return Double.compare(e2, e1);
            });

            quizList = new ArrayList<>();
            Set<String> used = new HashSet<>();
            for (Word w : originalList) {
                if (used.add(w.getWord().toLowerCase())) {
                    quizList.add(w);
                }
                if (quizList.size() == 10) break;
            }

            System.out.println("→ 무작위로 오답률 기준 상위 10개의 단어를 선택하여 퀴즈를 출제합니다.");
        } else {
            Collections.shuffle(originalList);
            quizList = originalList.subList(0, Math.min(10, originalList.size()));
            if (useWrongMode) {
                System.out.println("→ 오답 단어가 10개 미만이므로 모두 출제합니다.");
            } else {
                System.out.println("→ 모든 단어를 퀴즈로 출제합니다.");
            }
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

                    if (useWrongMode) {
                        wrongFileIO.removeWord(userWrongFile, question);
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
                        System.out.println("시도 횟수가 초과되었습니다.");
                    }
                }
            }

            if (!isCorrect) {
                boolean alreadyExists = wrongWordRepository.exists(question);
                if (!alreadyExists) {
                    wrongFileIO.addWord(userWrongFile, question);
                    System.out.println("오답 데이터 파일에 추가합니다.");
                } else {
                    int currentCount = wrongWordRepository.count(question.getWord());
                    System.out.println("현재 오답 수: " + currentCount);
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
