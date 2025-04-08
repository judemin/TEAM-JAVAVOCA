# Code Style Guide

## 목차

1. [서론](#서론)
2. [IDE 및 개발 환경 설정](#ide-및-개발-환경-설정)
3. [네이밍 규칙](#네이밍-규칙)
4. [코드 포맷팅](#코드-포맷팅)
5. [주석 작성 방법](#주석-작성-방법)
6. [예외 처리](#예외-처리)
7. [모순 발생 시](#모순-발생-시)

---

## 서론

이 문서는 팀 내 Java 프로젝트의 코드 스타일 일관성을 유지하고, 협업의 효율성을 높이기 위해 작성되었습니다. 모든 팀원은 이 가이드를 준수하여 개발을 진행합니다.

---

## IDE 및 개발 환경 설정

- IDE는 기본적으로 IntelliJ IDEA를 사용합니다.
- 코드 스타일 설정은 프로젝트 단위로 동일한 `.editorconfig`를 적용합니다.

예시 `.editorconfig` 설정:

```editorconfig
root = true

[*]
charset = utf-8
indent_style = space
indent_size = 4
end_of_line = lf
insert_final_newline = true
trim_trailing_whitespace = true
```

---

## 네이밍 규칙

### 클래스 및 인터페이스
- PascalCase 사용

```java
// Good
public class UserProfile {}

// Bad
public class user_profile {}
```

### 메소드 및 변수
- camelCase 사용

```java
// Good
private int userAge;
public void calculateTotal() {}

// Bad
private int User_Age;
public void Calculate_total() {}
```

### 상수
- 대문자 SNAKE_CASE 사용

```java
// Good
public static final int MAX_SIZE = 100;

// Bad
public static final int maxSize = 100;
```

---

## 코드 포맷팅

- 들여쓰기: 공백 4칸
- 중괄호 위치는 다음과 같이 작성

```java
// Good
if (condition) {
    // 코드
}

// Bad
if (condition)
{
    // 코드
}
```

- 라인 길이는 최대 120자로 제한합니다.

---

## 주석 작성 방법

- 클래스 및 메소드 레벨에서는 Javadoc 주석을 사용합니다.

```java
/**
 * 사용자 정보를 반환합니다.
 *
 * @param userId 사용자 아이디
 * @return User 객체
 */
public User getUserById(String userId) {
    // 구현 내용
}
```

- 코드 내부 설명은 간결한 단일행 주석을 사용합니다.

```java
// 사용자 인증 체크
checkUserAuthentication();
```

---

## 예외 처리

- 예외는 명확한 메시지와 함께 처리합니다.
- 일부 FileIO와 관련된 메소드를 제외하고는 전부 throw하지 않고 각 클래스 내에서 자체적으로 처리합니다.

```java
// Good
try {
    // 코드
} catch (Exception e) {
    logger.error("에러 메시지", e);
}
```

## 모순 발생 시
- 구현 도중 기획안과 다른 내용을 발견했을 때 TODO와 함께 상세한 내용을 주석으로 남깁니다.

```java
// TODO: 에러 메세지 출력을 할지 말지 (수정사항)
// 만일 주 프롬프트에서 그냥 곧바로 Enter ⏎ 키만 누르거나 (즉, 빈 문자열 입력),
// 공백류(들)만 입력하거나, 입력 중 첫번째 단어가 메뉴 항목 번호가 아닐 경우,
// 틀린 입력으로 간주하고 (“잘못된 입력입니다.”같은 진부한 안내 없이, 조용히)
// 표1에 준하는 표준 메뉴 항목 번호 및 인자 안내 화면을 출력하고 주 프롬프트로 되돌아갑니다
```
