package manager;

import data.entity.Word;
import java.io.IOException;
import java.util.List;

/**
 * 단어 데이터를 관리하는 클래스입니다.
 * 단어 목록을 불러오고 추가/수정/삭제 기능을 제공합니다.
 */
public class WordManager {

    /** 프로그램이 관리하는 전체 단어 목록 */
    private List<Word> words;

    /** 단어 데이터 파일 경로 */
    private String wordFilePath;

    /**
     * WordManager를 초기화합니다.
     */
    public WordManager(){
    }

    /**
     * 현재 메모리상의 전체 단어 목록을 반환합니다.
     *
     * @return Word 객체 목록
     */
    public List<Word> getAllWords() {
        return null;
    }

    /**
     * 단어 레코드 관리 기능을 처리합니다.
     * 단어 추가, 수정, 삭제 중 하나를 선택하고 해당 기능을 실행합니다.
     */
    public void handleWordManagementMenu() {
        System.out.println("[handleWordManagementMenu]");
    }

    /**
     * 새로운 단어를 추가합니다. 단어 목록을 갱신하고 파일에 저장합니다.
     *
     * @param word    추가할 영어 단어
     * @param meaning 추가할 단어의 뜻풀이
     * @return 추가 성공 시 true, 실패 시 false (예: 중복 단어인 경우)
     * @throws IOException 파일 저장 중 오류가 발생한 경우
     */
    public boolean addWord(String word, String meaning) throws IOException {
        return false;
    }

    /**
     * 기존 단어의 뜻풀이 또는 단어 자체를 수정합니다. 단어 목록을 갱신하고 파일에 저장합니다.
     *
     * @param originalWord 수정할 대상 단어 (영어)
     * @param newWord      변경할 영단어 (동일하면 단어는 수정되지 않음)
     * @param newMeaning   변경할 뜻풀이
     * @return 수정 성공 시 true, 대상 단어를 찾을 수 없으면 false
     * @throws IOException 파일 저장 중 오류가 발생한 경우
     */
    public boolean updateWord(String originalWord, String newWord, String newMeaning) throws IOException {
        return false;
    }

    /**
     * 단어를 목록에서 제거합니다. 단어 목록을 갱신하고 파일을 저장합니다.
     *
     * @param word 제거할 영어 단어
     * @return 제거 성공 시 true, 제거할 단어를 찾지 못한 경우 false
     * @throws IOException 파일 저장 중 오류가 발생한 경우
     */
    public boolean removeWord(String word) throws IOException {
        return false;
    }
}
