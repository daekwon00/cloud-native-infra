# local-dev-env

Docker Compose 기반 로컬 개발환경 (PostgreSQL + Kafka + Spring Boot)

## 구성

- **PostgreSQL 15** — 관계형 데이터베이스
- **Zookeeper + Kafka 3.6** (Confluent 7.6) — 메시지 브로커
- **Kafka UI** — Kafka 웹 관리 도구
- **Spring Boot 4.0.3** (Java 25) — 백엔드 애플리케이션

## 시작하기

```bash
cp .env.example .env
docker compose up -d
```

## 접속 정보

| 서비스 | URL |
|--------|-----|
| Spring Boot | http://localhost:8081 |
| Actuator Health | http://localhost:8081/actuator/health |
| Kafka UI | http://localhost:9090 |
| PostgreSQL | localhost:5432 |

## API 엔드포인트

| 메서드 | 경로 | 설명 |
|--------|------|------|
| GET | /api/health | 앱 상태 확인 |
| POST | /api/messages | Kafka 메시지 전송 (`{"message": "hello"}`) |
| GET | /api/messages | 수신된 메시지 목록 (최근 20건) |

## 프로젝트 구조

```
local-dev-env/
├── docker-compose.yml
├── .env.example
├── app/                  # Spring Boot 애플리케이션
│   ├── Dockerfile
│   ├── build.gradle
│   └── src/
└── init/
    └── postgres/         # DB 초기화 스크립트
```
