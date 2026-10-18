# 로컬 실행

[README로 돌아가기](../README.md)

## 준비

- JDK 17
- 저장소의 Gradle Wrapper 8.13
- 첫 실행 시 Gradle과 의존성을 내려받을 인터넷 연결

```bash
git clone https://github.com/ZANDHIFORCE/Minuet_project.git
cd Minuet_project
java -version
```

Java 17이 선택되어 있어야 합니다. 다른 버전이 기본이면 `JAVA_HOME`을 JDK 17 경로로 지정합니다.

## 테스트와 서버 실행

Windows PowerShell:

```powershell
.\gradlew.bat test --no-daemon
.\gradlew.bat bootRun
```

macOS / Linux:

```bash
sh gradlew test --no-daemon
sh gradlew bootRun
```

`bootRun` 실행 후 `http://localhost:8080`에서 시작합니다. 종료는 `Ctrl+C`입니다. 전체 테스트와 HTTP 등록·조회 확인 결과는 [검증 기록](VALIDATION.md)에 남겼습니다.

## 데이터베이스와 초기 데이터

- H2 콘솔: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:minuet`
- 사용자: `sa`, 비밀번호: 빈 값
- `schema.sql`, `data.sql`로 테이블과 예시 학생·강사·레슨을 초기화합니다.
- H2 메모리 DB이므로 프로세스가 종료되면 데이터가 사라집니다.
- 레슨 시간표는 기존 예시 학생·강사 ID를 사용해 등록할 수 있습니다. 초기 강사는 1·2번, 학생은 1·2·3번입니다.
- 테스트 프로필은 별도 `testdb`를 사용하고 예시 데이터를 자동 삽입하지 않습니다. JDBC 테스트는 필요한 부모 데이터를 직접 준비합니다.

실제 화면 경로는 [화면 경로](ROUTES.md)를 참고하세요. 레슨 시간표에는 [입력 검증과 강사·학생 중복 방지](LESSON_REGISTRATION.md)가 적용됩니다. 수정·삭제 화면과 수업 시간 구간의 겹침 검사는 후속 과제입니다.

## 해결한 초기화 문제

기존 `lesson_slot`의 예약어 컬럼과 마지막 쉼표 때문에 서버 기동이 실패했습니다. `lesson_day`, `lesson_time`으로 SQL·매핑을 맞추고 구문을 수정했습니다. 기존 영속 DB의 컬럼 마이그레이션은 제공하지 않으며 이 실행 안내는 새 H2 메모리 DB 기준입니다.
