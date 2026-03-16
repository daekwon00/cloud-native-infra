# local-dev-env

Docker Compose 기반 로컬 개발환경 (PostgreSQL + Kafka + Spring Boot)

## 구성

- **PostgreSQL 15** — 관계형 데이터베이스
- **Zookeeper + Kafka 3.6** — 메시지 브로커
- **Kafka UI** — Kafka 웹 관리 도구
- **Spring Boot 3.x** — 백엔드 애플리케이션

## 시작하기

```bash
cp .env.example .env
docker compose up -d
```

## 접속 정보

| 서비스 | URL |
|--------|-----|
| Spring Boot | http://localhost:8080 |
| Kafka UI | http://localhost:8989 |
| PostgreSQL | localhost:5432 |

## 프로젝트 구조

```
local-dev-env/
├── docker-compose.yml
├── .env.example
├── app/                  # Spring Boot 애플리케이션
│   └── src/
└── init/
    └── postgres/         # DB 초기화 스크립트
```
