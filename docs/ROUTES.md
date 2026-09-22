# 웹 화면 경로

[README로 돌아가기](../README.md)

Thymeleaf 화면을 반환하는 Spring MVC 경로입니다. 아래 URL의 **대소문자**를 그대로 사용하세요.

| 대상 | 입력 화면 | 등록 처리 | 목록 화면 |
| :--- | :--- | :--- | :--- |
| 학생 | `GET /Student/new` | `POST /Student/new` | `GET /Student/list` |
| 강사 | `GET /Teacher/new` | `POST /Teacher/new` | `GET /Teacher/list` |
| 레슨 기록 | `GET /LessonInfo/new` | `POST /LessonInfo/new` | `GET /LessonInfo/list` |
| 레슨 시간표 | `GET /LessonSlot/new` | `POST /LessonSlot/new` | `GET /LessonSlot/list` |

- 홈: `GET /`
- H2 콘솔: `GET /h2-console`
- H2 JDBC URL: `jdbc:h2:mem:minuet`, 사용자: `sa`, 비밀번호: 빈 값

서비스·저장소에는 수정·삭제 메서드가 있지만, 위 컨트롤러에는 해당 웹 경로가 연결되어 있지 않습니다. 이 표는 웹에서 접근할 수 있는 기능만 정리한 것입니다.

[컨트롤러 코드](../src/main/java/com/ZandhiDokkie/minuet/controller)
