# TEAM-JAVAVOCA
2025-1 전공기초프로젝트 C01팀

### 디렉터리 구조
```bash
├── src/
│   ├── data/
│   │   └── Word.java
│   ├── exception/
│   │   └── KeyException.java
│   ├── io/
│   │   ├── FileIO.java
│   │   └── WrongFileIO.java
│   ├── manager/
│   │   ├── QuizManager.java
│   │   ├── SearchManager.java
│   │   └── WordManager.java
│   └── ui/
│       ├── App.java
│       └── Main.java
├── JavaVoca.jar
├── manifest.mf
├── wrong_answers.txt
└── words.txt
```

---
### 수동 실행 방법

1. **컴파일**:
```bash
javac -encoding UTF-8 -d bin src/**/*.java
```

2. **JAR 생성**:
```bash
jar cfm JavaVoca.jar manifest.mf -C bin .
```

3. **데이터 파일 배치**: 
`JavaVoca.jar`, `words.txt`, `wrong_answers.txt`를 같은 디렉터리에 위치

4. **실행**:
```bash
java -jar JavaVoca.jar
```

---
### IntelliJ 설정
1. **프로젝트 열기**
    - IntelliJ IDEA를 실행한 후, `File → Open` 메뉴를 선택하고 프로젝트 루트 폴더(예: src, data, bin, manifest.mf 등이 있는 폴더)를 엽니다.

2. **소스 루트 지정**
    - 프로젝트 트리에서 `src` 폴더를 우클릭한 후 **Mark Directory as → Sources Directory**로 지정합니다.

3. **출력 디렉터리 설정**
    - `File → Project Structure`에서 **Modules** 탭을 선택하고, `Paths` 설정에서 **Compiler output path**를 `bin` 폴더로 지정합니다.

4. **런 구성 (Run Configuration) 생성**
    - 상단 메뉴에서 **Run → Edit Configurations**를 선택한 후, 새 **Application** 구성(configuration)을 만듭니다.
    - **Main class**는 `ui.Main` (실제 Main 클래스의 패키지 경로와 이름에 맞게)로 설정합니다.
    - **Program Arguments** 설정에 `-encoding UTF-8`를 추가합니다
    - **Working directory**는 기본적으로 프로젝트 루트로 설정되어야 합니다.

5. **빌드 및 실행**
    - **Build → Build Project**를 통해 프로젝트를 빌드합니다.
    - 이후 **Run** 버튼을 눌러 실행합니다.