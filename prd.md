# Cloud-Native Infrastructure Portfolio - Product Requirements Document

## 개요

Mac 로컬 K8s(kind) + AWS EC2(Terraform+k3s) + AWS EKS 스프린트 환경에서 Spring Boot 기반 마이크로서비스를 GitOps 파이프라인으로 배포하는 클라우드 인프라 포트폴리오 프로젝트.

## 목표

- Mac 로컬 kind 클러스터에서 개발/테스트 ($0 운영)
- AWS EC2 t3.medium에 Terraform IaC + k3s 클러스터 구축 (상시 운영)
- Docker Compose 로컬 개발환경 → K8s 배포 전환 경험
- GitOps 파이프라인 구축 (GitHub Actions → ECR → ArgoCD)
- Terraform IaC로 AWS 인프라 코드화
- Prometheus/Grafana/AlertManager 모니터링 스택 구축
- AWS EKS 스프린트로 프로덕션급 K8s 경험 확보
- 3개 GitHub Repo로 포트폴리오 완성

## 기술 스택

- **로컬 개발**: Mac (kind/k3d) + Docker Compose
- **클라우드 상시**: AWS EC2 t3.medium + k3s (Terraform)
- **관리형 K8s**: AWS EKS (스프린트 3일)
- **애플리케이션**: Spring Boot 3.x + PostgreSQL 15 + Kafka 3.6
- **CI/CD**: GitHub Actions + ArgoCD + Kustomize
- **IaC**: Terraform (AWS Provider)
- **모니터링**: kube-prometheus-stack (Prometheus + Grafana + AlertManager)
- **컨테이너 레지스트리**: ECR (AWS)
- **Ingress**: Traefik (k3s 기본)

## 기능 요구사항

### 핵심 기능

- 로컬 kind 클러스터 + AWS EC2 k3s 클러스터 운영
- Spring Boot + PostgreSQL + Kafka 전체 스택 K8s 배포
- GitOps 자동 배포 (git push → 빌드 → ECR → ArgoCD Sync → Rolling Update)
- Kustomize base/overlay로 dev/prod 환경 분리
- Terraform으로 AWS 인프라 프로비저닝 (VPC + EC2 + Security Group)

### 부가 기능

- Docker Compose 로컬 개발환경
- Kafka UI 웹 인터페이스
- Grafana 커스텀 대시보드 3종 (Spring Boot, Kafka, PostgreSQL)
- AlertManager + Slack 알림 연동
- AWS EKS 동일 manifest 배포

## 비기능 요구사항

- Apple Silicon(로컬) + AMD64(EC2) 멀티아키텍처 컨테이너 이미지 빌드
- AWS EC2 상시 운영 비용 ~$40/월 이내 관리
- AWS EKS 스프린트 비용 ~$15 이내

## 3-Repo 구조


| Repo                   | 내용                                                  | 완성 시점   |
| ---------------------- | --------------------------------------------------- | ------- |
| `local-dev-env`        | Docker Compose 로컬 개발환경                              | Phase 1 |
| `k8s-gitops`           | Terraform IaC + Kustomize + ArgoCD + GitHub Actions | Phase 2 |
| `k8s-monitoring-stack` | Prometheus + Grafana + AlertManager + Slack 알림      | Phase 3 |


## 비용 요약


| 항목                          | 비용           |
| --------------------------- | ------------ |
| Mac 로컬 (kind)               | $0           |
| GitHub Public Repo 3개        | $0           |
| AWS EC2 t3.medium (상시)       | ~$35/월       |
| ECR / EBS / Public IP        | ~$5/월        |
| AWS EKS 스프린트 (3일)            | ~$15 (1회)    |
| **합계**                       | **~$40/월 + $15 EKS** |
