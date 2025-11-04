# Azure Kubernetes Microservices CI/CD Pipeline

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Terraform](https://img.shields.io/badge/Terraform-1.0+-623CE4.svg)](https://www.terraform.io/)
[![Kubernetes](https://img.shields.io/badge/Kubernetes-1.19+-326ce5.svg)](https://kubernetes.io/)
[![Docker](https://img.shields.io/badge/Docker-20.10+-2496ED.svg)](https://www.docker.com/)
[![Helm](https://img.shields.io/badge/Helm-3.0+-0F1689.svg)](https://helm.sh/)
[![Jenkins](https://img.shields.io/badge/Jenkins-2.0+-D24939.svg)](https://jenkins.io/)
[![Prometheus](https://img.shields.io/badge/Prometheus-E6522C.svg)](https://prometheus.io/)
[![Grafana](https://img.shields.io/badge/Grafana-F46800.svg)](https://grafana.com/)

A comprehensive, production-ready CI/CD pipeline for deploying microservices on Azure Kubernetes Service (AKS). This project demonstrates Infrastructure as Code (IaC) with Terraform, automated provisioning with Ansible, containerized deployments with Docker and Helm, and complete monitoring with Prometheus and Grafana.

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Components](#components)
- [Deployment Guide](#deployment-guide)
- [CI/CD Pipeline](#ci/cd-pipeline)
- [Monitoring](#monitoring)
- [Development](#development)
- [Contributing](#contributing)
- [License](#license)

## Overview

This project implements a complete DevOps pipeline for microservices deployment on Azure, featuring:

- **Infrastructure as Code**: Terraform modules for Azure AKS cluster and CI/CD VM
- **Automated Provisioning**: Ansible playbooks for Jenkins, SonarQube, and Docker setup
- **Containerized Microservice**: Flask-based REST API with Prometheus metrics
- **Kubernetes Deployment**: Helm charts for application and monitoring stack
- **CI/CD Pipeline**: Jenkins with shared library for automated builds and deployments
- **Code Quality**: SonarQube integration for static analysis
- **Monitoring & Observability**: Prometheus, Grafana, and Alertmanager stack
- **Security**: Azure AD integration, network security groups, and secure configurations

### Key Features

- **End-to-End Automation**: From infrastructure provisioning to production deployment
- **Production Ready**: Load balancers, persistent storage, health checks, and monitoring
- **Cost Optimized**: Uses Azure Free Tier eligible resources where possible
- **Scalable Architecture**: Modular design supporting multiple environments
- **Observability**: Comprehensive monitoring with alerting and visualization
- **Security First**: Secure configurations, network isolation, and access controls

## Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                        Azure Cloud                                  │
│  ┌─────────────────────────────────────────────────────────────────┐  │
│  │                    Resource Group                               │  │
│  │  ┌─────────────────┐    ┌─────────────────────────────────────┐  │  │
│  │  │   AKS Cluster   │    │         CI/CD VM (Ubuntu)           │  │  │
│  │  │                 │    │  ┌─────────────────┐                │  │  │
│  │  │ • Control Plane │    │  │   Jenkins       │  (8080)        │  │  │
│  │  │ • Node Pools    │    │  │   • Pipelines   │                │  │  │
│  │  │ • Load Balancer │    │  │   • Shared Lib  │                │  │  │
│  │  │ • Auto-scaling  │    │  └─────────────────┘                │  │  │
│  │  │                 │    │  ┌─────────────────┐                │  │  │
│  │  │  Microservice   │    │  │   SonarQube     │  (9000)        │  │  │
│  │  │  Deployment     │    │  │   • Code Quality│                │  │  │
│  │  │  • Flask API    │    │  │   • Analysis    │                │  │  │
│  │  │  • Prometheus   │    │  └─────────────────┘                │  │  │
│  │  │  • Health Checks│    │  ┌─────────────────┐                │  │  │
│  │  │                 │    │  │   Docker        │                │  │  │
│  │  └─────────────────┘    │  │   • Build/Push  │                │  │  │
│  │                         │  └─────────────────┘                │  │  │
│  └─────────────────────────────────────────────────────────────────┘  │
│  ┌─────────────────────────────────────────────────────────────────┐  │
│  │                    Monitoring Stack                             │  │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐             │  │
│  │  │ Prometheus  │  │   Grafana   │  │ Alertmanager│             │  │
│  │  │ 9090        │  │ 3000        │  │ 9093        │             │  │
│  │  └─────────────┘  └─────────────┘  └─────────────┘             │  │
│  └─────────────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────┘
```

## Project Structure

```
microservices-cicd/
├── app/                          # Flask microservice application
│   ├── __init__.py              # Application factory with metrics
│   ├── main.py                  # Application entry point
│   ├── routes/                  # API route definitions
│   │   ├── user_routes.py       # User management endpoints
│   │   └── product_routes.py    # Product management endpoints
│   └── services/                # Business logic layer
│       ├── user_service.py      # User service implementation
│       └── product_service.py   # Product service implementation
├── terraform-k8s/               # Infrastructure as Code
│   ├── main.tf                  # Main Terraform configuration
│   ├── variables.tf             # Input variables
│   ├── outputs.tf               # Output values
│   ├── terraform.tfvars         # Variable values
│   └── modules/                 # Reusable Terraform modules
│       ├── aks/                 # AKS cluster module
│       ├── resource_group/      # Resource group module
│       └── vm/                  # Virtual machine module
├── ansible/                     # Configuration management
│   ├── ansible.cfg              # Ansible configuration
│   ├── inventory.ini            # Static inventory
│   ├── playbook.yml             # Main playbook
│   ├── generate_inventory.py    # Dynamic inventory script
│   └── roles/                   # Ansible roles
│       ├── docker/              # Docker installation
│       ├── jenkins/             # Jenkins setup
│       └── sonarqube/           # SonarQube setup
├── helm-chart/                  # Kubernetes packaging
│   ├── Chart.yaml               # Chart metadata
│   ├── values.yaml              # Default values
│   ├── templates/               # Kubernetes manifests
│   │   ├── deployment.yaml      # Application deployment
│   │   ├── service.yaml         # Service definition
│   │   └── _helpers.tpl         # Template helpers
│   └── README.md                # Chart documentation
├── monitoring/                  # Monitoring stack
│   ├── Chart.yaml               # Monitoring chart metadata
│   ├── values.yaml              # Monitoring configuration
│   ├── templates/               # Monitoring manifests
│   │   └── servicemonitor.yaml  # ServiceMonitor for apps
│   └── README.md                # Monitoring documentation
├── jenkins-shared-library/      # Jenkins pipeline library
│   ├── vars/                    # Pipeline functions
│   │   ├── microservicePipeline.groovy
│   │   ├── checkRepo.groovy
│   │   ├── runUnitTests.groovy
│   │   ├── build.groovy
│   │   ├── runSonarQubeAnalysis.groovy
│   │   ├── buildandPushDockerImage.groovy
│   │   ├── editHelmValues.groovy
│   │   ├── deployOnAks.groovy
│   │   └── deployMonitoring.groovy
│   └── README.md                # Library documentation
├── tests/                       # Application tests
│   └── test_app.py              # Unit tests
├── Dockerfile                   # Container definition
├── Jenkinsfile                  # Declarative pipeline
├── sonar-project.properties     # SonarQube configuration
├── requirements.txt             # Python dependencies
├── setup.py                     # Package configuration
├── run.py                       # Application runner
├── .gitignore                   # Git ignore rules
└── README.md                    # This file
```

## Prerequisites

### System Requirements

- **Operating System**: Linux, macOS, or Windows (with WSL)
- **CPU**: 4+ cores recommended
- **RAM**: 8GB+ recommended
- **Storage**: 50GB+ free space

### Required Tools

| Tool | Version | Purpose |
|------|---------|---------|
| **Terraform** | 1.0+ | Infrastructure provisioning |
| **Azure CLI** | 2.0+ | Azure authentication |
| **Ansible** | 2.9+ | Configuration management |
| **Docker** | 20.10+ | Containerization |
| **Helm** | 3.0+ | Kubernetes package management |
| **kubectl** | 1.19+ | Kubernetes cluster interaction |
| **Python** | 3.8+ | Application development |
| **Git** | 2.0+ | Version control |

### Azure Requirements

- **Azure Subscription**: Active subscription with sufficient credits
- **Azure AD Permissions**: Contributor or Owner role
- **Resource Quotas**: Adequate quotas for AKS, VMs, and networking
- **Region**: West US 2 (configured) or any supported region

### Network Requirements

- **Outbound Internet**: For downloading dependencies and container images
- **SSH Access**: For Ansible connections to VMs
- **Azure APIs**: Access to Azure Resource Manager APIs

## Quick Start

### 1. Clone the Repository

```bash
git clone <repository-url>
cd microservices-cicd
```

### 2. Authenticate with Azure

```bash
az login
az account set --subscription "your-subscription-id"
```

### 3. Deploy Infrastructure

```bash
# Navigate to Terraform directory
cd terraform-k8s

# Initialize Terraform
terraform init

# Plan the deployment
terraform plan

# Apply the configuration
terraform apply
```

### 4. Configure CI/CD Tools

```bash
# Navigate to Ansible directory
cd ../ansible

# Generate inventory from Terraform outputs
python3 generate_inventory.py

# Run Ansible playbook
ansible-playbook -i inventory.ini playbook.yml
```

### 5. Deploy Application

```bash
# Deploy microservice
helm install my-microservice ../helm-chart

# Deploy monitoring stack
helm install monitoring ../monitoring
```

### 6. Access Services

```bash
# Get service URLs
kubectl get svc

# Access the application
curl http://<EXTERNAL-IP>:5000/health

# Access monitoring
# Grafana: http://<GRAFANA-IP>:3000 (admin/admin)
# Prometheus: http://<PROMETHEUS-IP>:9090
```

## Components

### Flask Microservice (`app/`)

A RESTful API built with Flask featuring:

- **User Management**: CRUD operations for users
- **Product Management**: CRUD operations for products
- **Prometheus Metrics**: Built-in monitoring endpoints
- **Health Checks**: Application health monitoring
- **Blueprint Architecture**: Modular route organization

**API Endpoints:**
- `GET /health` - Health check
- `GET /users` - List all users
- `GET /users/<id>` - Get user by ID
- `GET /products` - List all products
- `GET /products/<id>` - Get product by ID
- `GET /metrics` - Prometheus metrics

### Infrastructure Layer (`terraform-k8s/`)

Terraform configuration for Azure resources:

- **Resource Group**: Logical container for all resources
- **AKS Cluster**: Managed Kubernetes service with auto-scaling
- **Ubuntu VM**: CI/CD server with Jenkins, SonarQube, and Docker
- **Networking**: Virtual networks, subnets, security groups
- **Security**: SSH keys, NSG rules, Azure AD integration

### Configuration Management (`ansible/`)

Ansible automation for CI/CD tool setup:

- **Jenkins**: CI/CD server with pipeline support
- **SonarQube**: Code quality analysis platform
- **Docker**: Container runtime for builds
- **Security**: Hardened configurations and access controls

### Kubernetes Packaging (`helm-chart/`)

Helm chart for application deployment:

- **Deployment**: Rolling updates and health checks
- **Service**: LoadBalancer for external access
- **ConfigMaps**: Environment-specific configurations
- **Resource Limits**: CPU and memory management

### CI/CD Pipeline (`jenkins-shared-library/`)

Reusable Jenkins pipeline functions:

- **Source Control**: Git checkout and branch management
- **Testing**: Unit tests with pytest and coverage
- **Code Quality**: SonarQube static analysis
- **Containerization**: Docker build and registry push
- **Deployment**: Helm upgrades and Kubernetes deployments

### Monitoring Stack (`monitoring/`)

Comprehensive observability solution:

- **Prometheus**: Metrics collection and alerting
- **Grafana**: Visualization dashboards
- **Alertmanager**: Alert routing and management
- **ServiceMonitor**: Automatic service discovery

## Deployment Guide

### Phase 1: Infrastructure Provisioning

```bash
# 1. Infrastructure setup
cd terraform-k8s
terraform init
terraform plan -out=tfplan
terraform apply tfplan

# 2. Configure kubectl
az aks get-credentials --resource-group $(terraform output -raw resource_group_name) --name $(terraform output -raw aks_cluster_name)
```

### Phase 2: CI/CD Tool Configuration

```bash
# 1. Generate Ansible inventory
cd ../ansible
python3 generate_inventory.py

# 2. Run provisioning playbook
ansible-playbook -i inventory.ini playbook.yml
```

### Phase 3: Application Deployment

```bash
# 1. Build and push Docker image
docker build -t your-registry/microservice-app .
docker push your-registry/microservice-app

# 2. Deploy with Helm
helm install my-microservice ./helm-chart

# 3. Verify deployment
kubectl get pods
kubectl get svc
```

### Phase 4: Monitoring Setup

```bash
# 1. Deploy monitoring stack
helm install monitoring ./monitoring

# 2. Access Grafana and import dashboards
kubectl get svc -n monitoring
```

## CI/CD Pipeline

The Jenkins pipeline automates the entire software delivery lifecycle:

### Pipeline Stages

1. **Checkout**: Source code retrieval from Git
2. **Test**: Unit testing with pytest and JUnit reporting
3. **Build**: Python application packaging
4. **SonarQube**: Static code analysis and quality gates
5. **Docker Build**: Container image creation and registry push
6. **Helm Update**: Chart values modification with new image tags
7. **AKS Deploy**: Application deployment to Kubernetes
8. **Monitoring**: Monitoring stack deployment

### Pipeline Configuration

The `Jenkinsfile` uses the shared library for consistency:

```groovy
@Library('jenkins-shared-library') _

pipeline {
    agent any
    stages {
        stage('CI') {
            steps {
                checkRepo()
                runUnitTests()
                build()
                runSonarQubeAnalysis()
            }
        }
        stage('CD') {
            steps {
                buildandPushDockerImage('docker-hub-credentials', 'my-app')
                editHelmValues('my-app', env.BUILD_NUMBER)
                deployOnAks('kubeconfig', 'default', './helm-chart')
                deployMonitoring('kubeconfig', 'monitoring', './monitoring')
            }
        }
    }
}
```

### Required Jenkins Credentials

- **docker-hub-credentials**: Username/password for Docker registry
- **kubeconfig**: File credential with Kubernetes configuration
- **sonarqube**: SonarQube authentication token

## Monitoring

### Accessing Monitoring Services

```bash
# Get service endpoints
kubectl get svc -n monitoring

# Example output:
# NAME                                    TYPE           CLUSTER-IP      EXTERNAL-IP      PORT(S)
# monitoring-grafana                     LoadBalancer   10.0.123.45     52.149.54.251    3000:31234/TCP
# monitoring-kube-prometheus-prometheus   LoadBalancer   10.0.101.112    52.149.54.253    9090:31236/TCP
```

### Pre-configured Dashboards

1. **Kubernetes Cluster Monitoring** (ID: 3119)
   - Node resource usage and status
   - Pod distribution and health
   - Cluster capacity planning

2. **Node Exporter Full** (ID: 1860)
   - Detailed node metrics
   - System performance monitoring
   - Hardware resource utilization

3. **Prometheus 2.0 Overview** (ID: 3662)
   - Prometheus server performance
   - Target health and scrape statistics
   - Storage and memory usage

### Application Metrics

The microservice exposes Prometheus metrics at `/metrics`:

- **http_requests_total**: Request count by method, endpoint, and status
- **http_request_duration_seconds**: Request latency histograms
- **Health status**: Application availability monitoring

### Alerting

Built-in alerts for common issues:

- Pod crash loops and readiness failures
- Resource utilization thresholds
- Node availability problems
- Kubernetes API server issues

## Development

### Local Development Setup

```bash
# 1. Create virtual environment
python3 -m venv venv
source venv/bin/activate

# 2. Install dependencies
pip install -r requirements.txt

# 3. Run the application
python run.py

# 4. Test the API
curl http://localhost:5000/health
curl http://localhost:5000/users
```

### Running Tests

```bash
# Run unit tests
pytest

# Run with coverage
pytest --cov=app --cov-report=html

# Run specific test file
pytest tests/test_app.py
```

### Docker Development

```bash
# Build local image
docker build -t microservice-app .

# Run container locally
docker run -p 5000:5000 microservice-app

# Test containerized application
curl http://localhost:5000/health
```

### Code Quality

```bash
# Run SonarQube analysis locally
sonar-scanner

# Or use the configured properties
sonar-scanner -Dproject.settings=sonar-project.properties
```

## Security Considerations

### Infrastructure Security

- **Network Security Groups**: Restrictive inbound/outbound rules
- **Azure AD Integration**: Managed identity for AKS
- **SSH Key Authentication**: No password-based access
- **Private Networking**: Internal load balancers where applicable

### Application Security

- **Input Validation**: Parameter validation in API endpoints
- **Error Handling**: Proper HTTP status codes and error messages
- **Container Security**: Non-root user execution
- **Dependency Management**: Pinned versions in requirements.txt

### CI/CD Security

- **Credential Management**: Jenkins credentials for sensitive data
- **Pipeline Security**: Code review requirements for pipeline changes
- **Image Scanning**: Container image vulnerability scanning
- **Secret Management**: Secure storage of API keys and tokens

## Troubleshooting

### Common Issues

1. **Terraform Authentication Errors**
   ```bash
   az login --use-device-code
   az account set --subscription "your-subscription-id"
   ```

2. **Ansible Connection Failures**
   ```bash
   # Test connectivity
   ansible -i inventory.ini ci_cd_servers -m ping
   # Check SSH key permissions
   chmod 600 ~/.ssh/id_rsa
   ```

3. **Kubernetes Deployment Issues**
   ```bash
   # Check pod status
   kubectl get pods
   kubectl describe pod <pod-name>
   # View logs
   kubectl logs <pod-name>
   ```

4. **Helm Installation Problems**
   ```bash
   # Update dependencies
   helm dependency update
   # Check chart syntax
   helm lint ./helm-chart
   ```

5. **Monitoring Not Working**
   ```bash
   # Check ServiceMonitor
   kubectl get servicemonitor
   kubectl describe servicemonitor monitoring-servicemonitor
   ```

### Debug Commands

```bash
# Infrastructure debugging
terraform state list
terraform output

# Ansible debugging
ansible-playbook -i inventory.ini playbook.yml -v

# Kubernetes debugging
kubectl get all --all-namespaces
kubectl logs -f deployment/my-microservice

# Jenkins debugging
# Check Jenkins logs in /var/log/jenkins/jenkins.log on the VM
```

## Cost Optimization

### Azure Cost Management

- **Free Tier Resources**: AKS and VM use free tier eligible SKUs
- **Auto-scaling**: AKS cluster scales based on demand
- **Resource Limits**: Proper CPU/memory limits prevent over-provisioning
- **Storage Classes**: Cost-effective storage options

### Usage Monitoring

```bash
# Monitor Azure costs
az cost export --name "cicd-costs" --query "properties"

# Check resource usage
kubectl top nodes
kubectl top pods
```

## Contributing

1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/enhanced-monitoring
   ```
3. **Make your changes**
4. **Test thoroughly**
   ```bash
   # Run tests
   pytest
   # Validate Terraform
   terraform validate
   # Lint Ansible
   ansible-lint ansible/
   # Check Helm charts
   helm lint helm-chart/
   ```
5. **Update documentation**
6. **Commit your changes**
   ```bash
   git commit -m 'Add enhanced monitoring features'
   ```
7. **Push to the branch**
   ```bash
   git push origin feature/enhanced-monitoring
   ```
8. **Open a Pull Request**

### Development Guidelines

- Follow infrastructure as code best practices
- Include comprehensive documentation
- Test changes in a development environment first
- Ensure backward compatibility
- Update README files for any new features

## License

This project is licensed under the MIT License - see the [LICENSE](../LICENSE) file for details.

## Support

For support and questions:

- **Documentation**: Check component-specific README files
- **Issues**: Create an issue in the repository
- **Azure Documentation**: [AKS Documentation](https://docs.microsoft.com/en-us/azure/aks/)
- **Kubernetes Community**: [Kubernetes Slack](https://slack.k8s.io/)
- **Terraform Community**: [Terraform Forums](https://discuss.hashicorp.com/c/terraform-core/)

---

**Note**: This project creates billable Azure resources. Monitor costs regularly and destroy resources when not in use. The infrastructure is designed for learning and demonstration purposes - review security and compliance requirements before using in production.
