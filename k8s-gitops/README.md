# k8s-gitops

Terraform IaC + Kustomize K8s Manifests + ArgoCD GitOps + GitHub Actions CI/CD

## 아키텍처

```
GitHub Actions (CI) → ECR (이미지) → ArgoCD (CD) → k3s/EKS (배포)
```

## 프로젝트 구조

```
k8s-gitops/
├── terraform/              # AWS 인프라 (VPC, EC2, Security Group)
├── k8s-config/
│   ├── base/
│   │   ├── database/       # PostgreSQL StatefulSet
│   │   ├── messaging/      # Kafka + Zookeeper
│   │   └── app/            # Spring Boot Deployment
│   └── overlays/
│       ├── dev/            # 개발 환경
│       └── prod/           # 프로덕션 환경
└── .github/
    └── workflows/          # CI 파이프라인
```

## 기술 스택

- **IaC**: Terraform (AWS Provider)
- **K8s**: k3s (EC2) / EKS
- **GitOps**: ArgoCD + Kustomize
- **CI**: GitHub Actions
- **Registry**: AWS ECR
