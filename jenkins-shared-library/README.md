# Jenkins Shared Library

This shared library contains reusable pipeline functions for microservice CI/CD pipelines.

## Usage

### In Jenkinsfile

```groovy
@Library('jenkins-shared-library') _

microservicePipeline()
```

### With Custom Configuration

```groovy
@Library('jenkins-shared-library') _

microservicePipeline(
    dockerImage: 'my-custom-app',
    helmChartPath: './my-helm-chart'
)
```

## Functions

### microservicePipeline

A complete CI/CD pipeline for microservices that includes:

- Code checkout
- Testing (pytest)
- Application build
- SonarQube analysis
- Docker image build and push
- Helm chart update
- Deployment to Kubernetes (AKS)

#### Required Credentials

- `docker-hub-credentials`: Username/password for Docker Hub
- `kubeconfig`: Kubernetes configuration file for AKS access

#### Required Tools

- Python 3
- Docker
- Helm
- kubectl
- SonarQube Scanner

## Library Structure

```
jenkins-shared-library/
├── vars/
│   └── microservicePipeline.groovy  # Main pipeline function
└── README.md                        # This file
```

## Jenkins Configuration

To use this library in Jenkins:

1. Go to Manage Jenkins → Configure System
2. Scroll to "Global Pipeline Libraries"
3. Add a new library:
   - Name: `jenkins-shared-library`
   - Default version: `main` (or your branch)
   - Retrieval method: Modern SCM
   - Source Code Management: Git
   - Repository URL: `https://github.com/your-org/jenkins-shared-library.git`
   - Credentials: (if private repo)

Or load it from the local filesystem if running Jenkins locally.
