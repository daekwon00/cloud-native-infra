# k8s-monitoring-stack

Kubernetes 모니터링 스택: Prometheus + Grafana + AlertManager + Slack 알림

## 구성

- **kube-prometheus-stack** — Prometheus + Grafana 통합 Helm 차트
- **커스텀 대시보드 3종** — Spring Boot, Kafka, PostgreSQL
- **AlertManager** — Slack 알림 연동

## 프로젝트 구조

```
k8s-monitoring-stack/
├── helm-values/          # kube-prometheus-stack values.yaml
├── dashboards/           # Grafana 커스텀 대시보드 JSON
└── alerting/             # AlertManager 규칙 + Slack 연동
```

## 대시보드

| 대시보드 | Grafana ID | 용도 |
|----------|-----------|------|
| Spring Boot | 19004 | JVM, HTTP, Actuator 메트릭 |
| Kafka | 7589 | Broker, Topic, Consumer 메트릭 |
| PostgreSQL | 9628 | DB 커넥션, 쿼리 성능 |
