# DAON ERP – Integrated Business Management Platform (Backend)

기업 내부 업무 프로세스를 관리하기 위한 **ERP 백엔드 REST API 서버**입니다.  
ERP 프론트엔드 애플리케이션과의 연동을 전제로 설계되었으며,  
단순 CRUD를 넘어 도메인 분리, 인증/보안, 확장성을 고려한 API 구조를 구현했습니다.

실제 ERP 시스템 구조를 참고하여  
유지보수성과 확장성을 함께 고려한 백엔드 프로젝트입니다.

---

## 📌 Project Overview

DAON ERP Backend는 기업 내부 업무 흐름을 처리하기 위한  
ERP 백엔드 API 서버입니다.

Spring Boot 기반으로 구현되었으며,  
인증 및 권한 처리, 도메인 분리, 예외 처리, 응답 구조 표준화를 고려하여 설계했습니다.

### 핵심 목표
- ERP 업무 프로세스를 위한 REST API 구현
- 인증 및 인가 구조 반영
- 도메인 중심 백엔드 구조 설계
- 유지보수성과 확장성을 고려한 API 서버 구현

---

## 🚀 Tech Stack

### Backend
- **Java 21**
- **Spring Boot**
- Spring Security
- JPA (Hibernate)
- Gradle

### Database
- MariaDB (Local)
- MySQL (Production)

### Authentication / Security
- JWT Access Token 기반 인증
- Spring Security Filter 기반 요청 검증
- Role 기반 접근 제어

---

## ✨ Main Features

### 🔐 인증 및 권한 관리
- JWT Access Token 기반 인증 처리
- Spring Security Filter를 통한 요청 검증
- 사용자 권한(Role) 기반 접근 제어
- 인증 오류 및 예외 처리 구성

### 🧩 도메인 중심 API 설계
- 도메인 분리 기반 REST API 설계
- 업무 흐름을 고려한 API 구조 구성
- 도메인 단위 패키지 분리

### ⚠️ 예외 처리 및 응답 구조
- Global Exception Handler 기반 예외 처리
- 일관된 API 응답 포맷 구성
- 공통 예외 및 응답 표준화

---

## 📁 Main Domains

- 사용자 인증 및 권한 관리
- 주문 관련 API
- 자재 / 재고 관련 API
- 생산 / 출고 흐름 API
- 공통 예외 및 응답 표준화

---

## 🔄 Authentication Flow

1. 사용자가 로그인 요청을 보낸다
2. 서버에서 인증 처리 후 JWT Access Token을 발급한다
3. 이후 요청에서 토큰을 포함해 API를 호출한다
4. Spring Security Filter가 토큰을 검증한다
5. 권한(Role)에 따라 접근 가능 여부를 판단한다
6. 인증 오류 또는 예외 발생 시 일관된 응답 형식으로 반환한다

---

## 🛠 Getting Started

### 1. Clone repository
```bash
git clone https://github.com/Nara-Park-513/erp-backend.git
cd erp-backend
```

### 2. Configure environment
`application.properties` 또는 `application.yml` 파일에 데이터베이스 및 JWT 설정을 추가합니다.

예시:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/erp
spring.datasource.username=your_username
spring.datasource.password=your_password
jwt.secret=your_jwt_secret
```

### 3. Build project
```bash
./gradlew clean build
```

### 4. Run server
```bash
./gradlew bootRun
```

### 5. Open in browser
```bash
http://localhost:8888
```

> ERP 프론트엔드 애플리케이션과 함께 실행해야 API 연동을 확인할 수 있습니다.  
> 백엔드 기본 실행 포트는 `http://localhost:8888` 입니다.

---

## ⚠️ Troubleshooting

개발 과정에서 아래와 같은 항목을 중점적으로 점검했습니다.

- JWT 인증 및 토큰 검증 흐름 구성
- Spring Security 기반 요청 검증 처리
- Role 기반 접근 제어 구조 설계
- 공통 예외 처리 및 응답 포맷 정리
- 도메인 분리 및 패키지 구조 설계

---

## 🎯 Project Purpose

- 단순 CRUD를 넘어 ERP 구조를 고려한 백엔드 설계
- 인증 / 인가 / 보안 요소를 포함한 실무형 API 서버 구현
- 유지보수성과 확장성을 고려한 도메인 중심 구조 설계
- ERP 프론트엔드와의 연동을 전제로 한 API 아키텍처 구현

---

## 🔮 Future Improvements

- API 문서 자동화 도입
- 통합 테스트 자동화
- 운영 환경 배포 자동화
- 토큰 리프레시 및 세션 관리 개선

---

## 👩‍💻 Author

DAON ERP Backend API Server