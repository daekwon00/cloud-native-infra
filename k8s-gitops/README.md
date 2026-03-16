# k8s-gitops

Terraform IaC + Kustomize K8s Manifests + ArgoCD GitOps + GitHub Actions CI/CD

## 아키텍처

```
GitHub Actions (CI) → ECR (이미지) → ArgoCD (CD) → k3s/EKS (배포)
```

```
┌─────────────┐     ┌──────────┐     ┌──────────┐     ┌────────────┐
│ local-dev-env│────▶│ GitHub   │────▶│ AWS ECR  │────▶│ ArgoCD     │
│ (소스 코드)  │push │ Actions  │push │ (이미지) │감지 │ (자동 Sync) │
└─────────────┘     └──────────┘     └──────────┘     └─────┬──────┘
                                                            │
                                                      ┌─────▼──────┐
                                                      │ k3s / EKS  │
                                                      │ (K8s 클러스터)│
                                                      └────────────┘
```

## 프로젝트 구조

```
k8s-gitops/
├── terraform/              # AWS 인프라 (VPC, EC2, Security Group)
│   ├── main.tf             # Provider + VPC + Subnet + IGW
│   ├── ec2.tf              # EC2 + Key Pair + EIP + k3s user_data
│   ├── security.tf         # Security Group
│   ├── variables.tf
│   └── outputs.tf
├── k8s-config/
│   ├── base/
│   │   ├── database/       # PostgreSQL StatefulSet + Headless Service
│   │   ├── messaging/      # Zookeeper + Kafka + kafka-init Job
│   │   └── app/            # Spring Boot Deployment + Service
│   └── overlays/
│       ├── dev/            # dev Namespace + Secret + kustomization
│       └── prod/           # prod Namespace + Secret + ClusterIP patch
├── argocd/
│   ├── values.yaml         # ArgoCD Helm values (경량화)
│   └── application-dev.yaml # ArgoCD Application (자동 Sync)
└── kind-config.yaml        # 로컬 kind 클러스터 설정
```

## 기술 스택

| 분류 | 기술 |
|------|------|
| IaC | Terraform (AWS Provider) |
| K8s 배포 | Kustomize (base/overlay) |
| GitOps | ArgoCD (자동 Sync + Self-Heal) |
| CI | GitHub Actions (Gradle → Docker buildx → ECR) |
| 로컬 K8s | kind (3노드 클러스터) |
| 원격 K8s | k3s (EC2) / EKS |
| Registry | AWS ECR |

## 로컬 개발 (kind)

```bash
# kind 클러스터 생성
kind create cluster --config kind-config.yaml

# 앱 이미지 로드
kind load docker-image local-dev-app:latest --name local-dev

# dev 환경 배포
kubectl apply -k k8s-config/overlays/dev/

# ArgoCD 설치
helm install argocd argo/argo-cd -n argocd --create-namespace -f argocd/values.yaml
kubectl apply -f argocd/application-dev.yaml
```

## AWS 인프라 (Terraform)

```bash
cd terraform
terraform init
terraform plan
terraform apply
```

## GitOps 파이프라인

1. `local-dev-env` repo에 코드 push
2. GitHub Actions → Gradle 빌드 → Docker 이미지 ECR push
3. `k8s-gitops` repo의 이미지 태그 자동 업데이트
4. ArgoCD가 변경 감지 → 자동 Sync → Rolling Update
