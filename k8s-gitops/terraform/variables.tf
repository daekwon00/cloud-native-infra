variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "ap-northeast-2"
}

variable "project_name" {
  description = "Project name for resource tagging"
  type        = string
  default     = "cloud-native-infra"
}

variable "instance_type" {
  description = "EC2 instance type"
  type        = string
  default     = "t3.medium"
}

variable "ssh_public_key_path" {
  description = "Path to SSH public key"
  type        = string
  default     = "~/.ssh/id_ed25519.pub"
}

variable "allowed_cidr" {
  description = "CIDR block allowed for SSH and admin access"
  type        = string
  default     = "0.0.0.0/0"
}
