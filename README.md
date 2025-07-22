# Minuet - 음악 레슨 관리 시스템

## 프로젝트 소개
Minuet은 음악 학원이나 개인 음악 교습소에서 사용할 수 있는 레슨 관리 시스템입니다.
학생, 선생님, 레슨 정보, 레슨 시간표를 효율적으로 관리할 수 있습니다.

## 기술 스택
- **Backend**: Spring Boot 3.x
- **Build Tool**: Gradle
- **Database**: H2 Database (개발용)
- **Template Engine**: Thymeleaf
- **Java Version**: 17+

## 주요 기능
- 👨‍🎓 **학생 관리**: 학생 등록, 조회, 수정, 삭제
- 👨‍🏫 **선생님 관리**: 선생님 등록, 조회, 수정, 삭제  
- 📚 **레슨 정보 관리**: 레슨 과목별 정보 관리
- 📅 **레슨 스케줄 관리**: 레슨 시간표 관리

## 프로젝트 구조
```
src/
├── main/
│   ├── java/com/ZandhiDokkie/minuet/
│   │   ├── config/          # 설정 클래스
│   │   ├── controller/      # 컨트롤러 계층
│   │   ├── domain/          # 도메인 모델
│   │   ├── repository/      # 데이터 접근 계층
│   │   │   ├── interfaces/  # 리포지토리 인터페이스
│   │   │   ├── memory/      # 메모리 기반 구현
│   │   │   └── jdbc/        # JDBC 기반 구현
│   │   └── service/         # 비즈니스 로직 계층
│   └── resources/
│       ├── templates/       # Thymeleaf 템플릿
│       ├── static/          # 정적 리소스
│       └── data/            # 테스트 데이터
└── test/                    # 테스트 코드
```

## 실행 방법
1. 프로젝트 클론
```bash
git clone [repository-url]
cd minuet
```

2. 프로젝트 빌드 및 실행
```bash
./gradlew bootRun
```

3. 브라우저에서 접속
```
http://localhost:8080
```

## 개발자
- **조동휘** - 컴퓨터공학과, Spring Boot 학습 중

## 학습 목표
- Spring Boot 기본 구조 이해
- MVC 패턴 적용
- 계층형 아키텍처 구현
- 테스트 코드 작성
- 데이터베이스 연동

---
*이 프로젝트는 Spring Boot 학습을 위한 개인 프로젝트입니다.*
