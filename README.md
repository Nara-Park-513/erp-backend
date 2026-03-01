# ERP Backend API Server

기업 내부 업무 프로세스를 관리하기 위한 **ERP(Enterprise Resource Planning) 백엔드 REST API 서버**입니다.  
실제 ERP 시스템 구조를 참고하여 단순한 CRUD를 넘어 도메인 분리·인증/보안·확장성을 고려한 API 설계에 중점을 두고 구현했습니다.  
본 API 서버는 ERP 프론트엔드 애플리케이션과의 연동을 전제로 설계되었습니다. :contentReference[oaicite:0]{index=0}

---

## 📌 기술 스택

- **Language:** Java 21
- **Framework:** Spring Boot
- **Security:** Spring Security + JWT
- **ORM:** JPA (Hibernate)
- **Build Tool:** Gradle
- **Database:** MariaDB (Local), MySQL (Production) :contentReference[oaicite:1]{index=1}

---

## 🛠 주요 기능

- 도메인 중심의 REST API 설계
- JWT Access Token 기반 인증 / 인가 처리
- 사용자 권한(Role) 기반 접근 제어
- Spring Security Filter 기반 요청 검증
- 공통 예외 처리(Global Exception Handler)
- 일관된 API 응답 포맷 구성
- 도메인 단위 패키지 분리 구조 :contentReference[oaicite:2]{index=2}

---

## 📁 주요 도메인

- 사용자 인증 및 권한 관리
- 주문 관련 API
- 자재/재고 관련 API
- 생산/출고 흐름 API
- 공통 예외 및 응답 표준화 :contentReference[oaicite:3]{index=3}

---

## 🚀 설치 및 실행 방법

### 1. 저장소 클론

```bash
git clone https://github.com/Nara-Park-513/erp-backend.git
cd erp-backend
2. 환경 설정

application.properties 또는 application.yml 파일에 데이터베이스 및 JWT 설정을 추가합니다.

예시:

spring.datasource.url=jdbc:mysql://localhost:3306/erp
spring.datasource.username=your_username
spring.datasource.password=your_password
jwt.secret=your_jwt_secret
3. 의존성 설치 및 빌드
./gradlew clean build
4. 실행
./gradlew bootRun

기본 실행 포트는 http://localhost:8080 입니다.

🔐 인증 및 권한 흐름

로그인 시 JWT Access Token 발급

Spring Security Filter를 통한 토큰 검증

Role 기반 접근 제어 적용

인증 오류 및 예외는 Global Exception Handler로 일관된 응답 처리

🧠 설계 의도

단순 CRUD 중심이 아닌 실제 ERP 환경을 가정한 백엔드 아키텍처

유지보수성과 확장성을 고려한 도메인 구조

인증/인가, 보안, 예외 처리 등 실무 필수 요소 반영

📌 향후 개선 예정

API 문서 자동화(Swagger/OpenAPI) 도입

통합 테스트 자동화

운영 환경 배포 자동화(CI/CD)

토큰 리프레시 & 세션 관리 개선

📜 라이선스

본 프로젝트는 포트폴리오 목적의 학습용 프로젝트입니다.