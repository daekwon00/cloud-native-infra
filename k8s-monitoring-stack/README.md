# k8s-monitoring-stack

Kubernetes 모니터링 스택: Prometheus + Grafana + AlertManager + Slack 알림

## 아키텍처

```
┌──────────────┐     ┌────────────┐     ┌──────────┐
│ Spring Boot  │────▶│ Prometheus │────▶│ Grafana  │
│ /actuator    │메트릭│ (수집/저장) │시각화│ (대시보드)│
└──────────────┘     └─────┬──────┘     └──────────┘
                           │
┌──────────────┐     ┌─────▼──────┐     ┌──────────┐
│ Kafka        │────▶│ Alert      │────▶│ Slack    │
│ Exporter     │     │ Manager    │알림 │ Webhook  │
└──────────────┘     └────────────┘     └──────────┘
┌──────────────┐
│ PostgreSQL   │────▶ Prometheus
│ Exporter     │
└──────────────┘
```

## 구성 요소

| 구성 요소 | 설명 | 포트 |
|-----------|------|------|
| Prometheus | 메트릭 수집/저장/쿼리 | NodePort 30090 |
| Grafana | 대시보드 시각화 | NodePort 30300 |
| AlertManager | 알림 라우팅 + Slack 연동 | NodePort 30903 |
| Kafka Exporter | Kafka 브로커 메트릭 수집 | 9308 |
| PostgreSQL Exporter | DB 메트릭 수집 | 9187 |

## 프로젝트 구조

```
k8s-monitoring-stack/
├── helm-values/
│   ├── kube-prometheus-stack.yaml    # Prometheus + Grafana Helm values
│   ├── servicemonitor-app.yaml       # Spring Boot ServiceMonitor
│   └── servicemonitor-kafka-exporter.yaml  # Kafka Exporter ServiceMonitor
├── dashboards/
│   └── grafana-dashboards-configmap.yaml
└── alerting/
    ├── slack-secret.yaml             # Slack Webhook Secret
    ├── alertmanager-config.yaml      # AlertManagerConfig (Slack 연동)
    └── prometheus-rules.yaml         # 커스텀 알림 규칙
```

## Grafana 대시보드

| 대시보드 | Grafana ID | 용도 |
|----------|-----------|------|
| Spring Boot 3.x Statistics | 19004 | JVM, HTTP, Actuator 메트릭 |
| Kafka Exporter Overview | 7589 | Broker, Topic, Consumer 메트릭 |
| PostgreSQL Database | 9628 | DB 커넥션, 쿼리 성능 |

## 알림 규칙

| 알림 | 심각도 | 조건 |
|------|--------|------|
| PodCrashLooping | critical | 5분간 3회 이상 재시작 |
| PodNotReady | warning | 5분간 Not Ready |
| HighMemoryUsage | warning | 메모리 사용률 90% 초과 |
| HighCPUUsage | warning | CPU 사용률 80% 초과 |
| SpringBootDown | critical | 앱 1분 이상 응답 없음 |
| KafkaExporterDown | warning | Exporter 2분 이상 응답 없음 |

## 설치

```bash
# Helm repo 추가
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts

# kube-prometheus-stack 설치
kubectl create namespace monitoring
helm install prometheus prometheus-community/kube-prometheus-stack \
  -n monitoring -f helm-values/kube-prometheus-stack.yaml

# Exporter 설치
helm install kafka-exporter prometheus-community/prometheus-kafka-exporter \
  -n monitoring --set kafkaServer="{kafka.dev.svc.cluster.local:9092}"

helm install postgres-exporter prometheus-community/prometheus-postgres-exporter \
  -n monitoring --set config.datasource.host=postgres.dev.svc.cluster.local \
  --set config.datasource.user=postgres --set config.datasource.password=postgres

# ServiceMonitor + 알림 규칙 적용
kubectl apply -f helm-values/servicemonitor-app.yaml
kubectl apply -f helm-values/servicemonitor-kafka-exporter.yaml
kubectl apply -f alerting/prometheus-rules.yaml
```

## 접속 정보

| 서비스 | URL | 인증 |
|--------|-----|------|
| Grafana | http://localhost:30300 | admin / admin |
| Prometheus | http://localhost:30090 | - |
| AlertManager | http://localhost:30903 | - |
