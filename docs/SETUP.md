# 실행 조건과 알려진 초기화 문제

[README로 돌아가기](../README.md)

## 현재 상태

JDK 17과 저장소의 Gradle Wrapper로 빌드·테스트를 시작할 수 있습니다. 다만 **현재 스키마의 H2 초기화 오류 때문에 전체 애플리케이션과 통합 테스트가 정상 시작되지 않습니다.** 이 문서 개편은 애플리케이션 코드를 변경하지 않습니다.

## 준비와 실행 명령

```bash
git clone https://github.com/ZANDHIFORCE/Minuet_project.git
cd Minuet_project
java -version
```

Java 17이 선택되어 있어야 합니다. 다른 Java가 기본인 경우 `JAVA_HOME`을 JDK 17로 지정합니다.

Windows PowerShell:

```powershell
.\gradlew.bat test
.\gradlew.bat bootRun
```

macOS / Linux:

```bash
sh gradlew test
sh gradlew bootRun
```

Wrapper는 Gradle 8.13을 사용합니다. 위 명령은 현재 실패를 재현하는 데에도 사용할 수 있습니다.

## 초기화 오류

`schema.sql`의 `lesson_slot` 정의에서 H2가 `day`를 예약어로 해석해 구문 오류가 발생합니다. 정의 끝에는 불필요한 쉼표도 남아 있습니다. 컬럼 이름/인용 방식과 연결된 SQL을 일관되게 정리하고, 마지막 쉼표를 제거하는 후속 코드 수정이 필요합니다.

현재 테스트 결과는 **141개 실행, 93개 통과, 48개 실패**입니다. 실패의 공통 원인으로 애플리케이션 컨텍스트 초기화 오류를 확인했습니다. 초기화 문제를 해결한 뒤에도 전체 테스트 재실행이 필요합니다.

## 정상 초기화 후의 접속 정보

- 웹: `http://localhost:8080`
- H2 콘솔: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:minuet`
- 사용자: `sa`, 비밀번호: 빈 값
- H2 메모리 DB이므로 프로세스가 종료되면 데이터가 사라집니다.
- `schema.sql`, `data.sql`에 초기 테이블과 예시 데이터가 있습니다.

실제 웹 경로는 [화면 경로](ROUTES.md), 실행 근거는 [검증 기록](VALIDATION.md)을 참고하세요.
