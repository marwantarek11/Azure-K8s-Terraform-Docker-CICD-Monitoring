# Jenkins Shared Library for Microservices CI/CD

[![Jenkins](https://img.shields.io/badge/Jenkins-2.0+-blue.svg)](https://jenkins.io/)
[![Groovy](https://img.shields.io/badge/Groovy-2.4+-purple.svg)](https://groovy-lang.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A comprehensive Jenkins Shared Library providing reusable pipeline functions for microservice CI/CD workflows. This library streamlines the development and deployment process for containerized microservices on Kubernetes.

## Table of Contents

- [Overview](#overview)
- [Quick Start](#quick-start)
- [Available Functions](#available-functions)
- [Configuration](#configuration)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage Examples](#usage-examples)
- [Library Structure](#library-structure)
- [Contributing](#contributing)
- [License](#license)

## Overview

This Jenkins Shared Library contains modular, reusable pipeline functions designed specifically for microservice architectures. It supports the complete CI/CD lifecycle from code checkout to production deployment, with built-in support for:

- **Automated Testing**: Unit testing with pytest and coverage reporting
- **Code Quality**: SonarQube integration for static analysis
- **Containerization**: Docker image building and registry pushing
- **Infrastructure as Code**: Helm chart management and updates
- **Cloud Deployment**: Kubernetes (AKS) deployment with monitoring

## Quick Start

### Basic Usage

```groovy
@Library('jenkins-shared-library') _

pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                checkRepo()
            }
        }
        stage('Test') {
            steps {
                runUnitTests()
            }
        }
        stage('Build') {
            steps {
                build()
            }
        }
        stage('SonarQube') {
            steps {
                runSonarQubeAnalysis()
            }
        }
        stage('Docker Build & Push') {
            steps {
                buildandPushDockerImage('docker-hub-credentials', 'my-app')
            }
        }
        stage('Update Helm Values') {
            steps {
                editHelmValues('my-app', env.BUILD_NUMBER)
            }
        }
        stage('Deploy to AKS') {
            steps {
                deployOnAks('kubeconfig', 'default', './helm-chart')
            }
        }
    }
}
```

### Complete Pipeline

```groovy
@Library('jenkins-shared-library') _

microservicePipeline(
    dockerImage: 'my-microservice-app',
    helmChartPath: './helm-chart'
)
```

## Available Functions

### Core Pipeline Functions

| Function | Description | Parameters |
|----------|-------------|------------|
| `microservicePipeline()` | Complete CI/CD pipeline | `dockerImage`, `helmChartPath` |
| `checkRepo()` | Git repository checkout | None |
| `runUnitTests()` | Execute pytest with JUnit reporting | None |
| `build()` | Python application build | None |
| `runSonarQubeAnalysis()` | SonarQube static analysis | None |
| `buildandPushDockerImage()` | Docker build and registry push | `credentialsId`, `imageName` |
| `editHelmValues()` | Update Helm chart values | `imageName`, `tag` |
| `deployOnAks()` | Deploy to Azure Kubernetes Service | `credentialsId`, `namespace`, `chartPath` |
| `deployMonitoring()` | Deploy monitoring stack | `credentialsId`, `namespace`, `chartPath` |

### Function Details

#### microservicePipeline(Map config)

The main pipeline function that orchestrates the entire CI/CD process.

**Parameters:**
- `dockerImage` (optional): Docker image name (default: 'my-microservice-app')
- `helmChartPath` (optional): Path to Helm chart (default: './helm-chart')

**Stages:**
1. **Repo Checkout**: Checks out source code from SCM
2. **Running Test**: Installs dependencies and runs pytest
3. **Build App**: Builds Python application
4. **SonarQube Analysis**: Performs code quality analysis
5. **Build & Push Docker Image**: Creates and pushes Docker image
6. **Edit Helm Chart**: Updates image references in Helm values
7. **Deploy on AKS**: Deploys application to Kubernetes

#### checkRepo()

Performs a standard Git checkout using Jenkins' built-in SCM configuration.

#### runUnitTests()

Executes comprehensive unit testing with the following features:
- Installs Python dependencies globally (PEP 668 compliant)
- Runs pytest with JUnit XML output
- Sets up proper PYTHONPATH for testing

**Commands executed:**
```bash
pip install --break-system-packages --upgrade pip
pip install --break-system-packages --force-reinstall -r requirements.txt pytest
pytest --junitxml=test-results.xml
```

#### build()

Builds the Python application using setup.py.

**Command:** `python3 setup.py build`

#### runSonarQubeAnalysis()

Integrates with SonarQube for static code analysis.

**Requires:** SonarQube server configuration in Jenkins

#### buildandPushDockerImage(String credentialsId, String imageName)

Builds Docker image and pushes to registry.

**Parameters:**
- `credentialsId`: Jenkins credentials ID for registry authentication
- `imageName`: Base name for the Docker image

**Process:**
1. Builds image with build number tag
2. Pushes both specific version and 'latest' tags
3. Uses Docker Hub registry by default

#### editHelmValues(String imageName, String tag)

Updates Helm chart values.yaml with new image information.

**Parameters:**
- `imageName`: Docker image repository name
- `tag`: Image tag/version

**Updates:**
- `repository` field in values.yaml
- `tag` field in values.yaml

#### deployOnAks(String credentialsId, String namespace, String chartPath)

Deploys application to Azure Kubernetes Service using Helm.

**Parameters:**
- `credentialsId`: Jenkins credentials ID for kubeconfig
- `namespace`: Kubernetes namespace for deployment
- `chartPath`: Path to Helm chart directory

**Command:** `helm upgrade --install my-microservice <chartPath> --namespace <namespace>`

#### deployMonitoring(String credentialsId, String namespace, String chartPath)

Deploys monitoring stack (Prometheus, Grafana, Alertmanager) to Kubernetes.

**Parameters:** Same as `deployOnAks()`

**Command:** `helm upgrade --install monitoring <chartPath> --namespace <namespace>`

## Configuration

### Jenkins Credentials Required

| Credential ID | Type | Purpose |
|---------------|------|---------|
| `docker-hub-credentials` | Username/Password | Docker Hub authentication |
| `kubeconfig` | File | Kubernetes cluster access |
| `SonarQube` | SonarQube Token | Code analysis server |

### Environment Variables

The library uses the following Jenkins environment variables:
- `BUILD_NUMBER`: Used for Docker image tagging
- `WORKSPACE`: Current job workspace path

### SonarQube Configuration

Ensure SonarQube scanner is configured in Jenkins global tools and a server connection is established.

## Prerequisites

### System Requirements

- **Jenkins**: Version 2.0 or higher
- **Java**: JDK 8 or higher
- **Groovy**: Version 2.4+ (included with Jenkins)

### Required Tools

- **Python 3**: For application building and testing
- **Docker**: For containerization
- **Helm**: For Kubernetes package management
- **kubectl**: For Kubernetes cluster interaction
- **SonarQube Scanner**: For code quality analysis

### Kubernetes Requirements

- **Azure Kubernetes Service (AKS)** cluster
- **Helm** installed on the cluster
- **kubectl** configured with appropriate permissions
- **Monitoring namespace** (if deploying monitoring stack)

## Installation

### Method 1: Global Pipeline Library (Recommended)

1. **Access Jenkins Configuration**
   ```
   Jenkins Dashboard → Manage Jenkins → Configure System
   ```

2. **Configure Global Pipeline Libraries**
   - Scroll to "Global Pipeline Libraries" section
   - Click "Add"

3. **Library Configuration**
   ```
   Name: jenkins-shared-library
   Default version: main
   Retrieval method: Modern SCM
   Source Code Management: Git
   Repository URL: https://github.com/your-org/jenkins-shared-library.git
   Credentials: (configure if private repository)
   ```

4. **Save Configuration**

### Method 2: Local Filesystem (Development)

For local Jenkins instances, load the library from the filesystem:

```groovy
library identifier: 'jenkins-shared-library@master',
        retriever: modernSCM([
            $class: 'GitSCMSource',
            remote: '/path/to/local/jenkins-shared-library'
        ])
```

## Usage Examples

### Basic Microservice Pipeline

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
                script {
                    def imageTag = "${env.BUILD_NUMBER}"
                    buildandPushDockerImage('docker-hub-credentials', 'my-app')
                    editHelmValues('my-app', imageTag)
                    deployOnAks('kubeconfig', 'default', './helm-chart')
                }
            }
        }
    }

    post {
        always {
            junit 'test-results.xml'
        }
    }
}
```

### Multi-Environment Deployment

```groovy
@Library('jenkins-shared-library') _

pipeline {
    agent any

    parameters {
        choice(name: 'ENVIRONMENT', choices: ['dev', 'staging', 'prod'], description: 'Deployment environment')
    }

    stages {
        stage('Build & Test') {
            steps {
                checkRepo()
                runUnitTests()
                build()
            }
        }

        stage('Deploy') {
            steps {
                script {
                    def namespace = params.ENVIRONMENT
                    def imageTag = "${env.BUILD_NUMBER}-${params.ENVIRONMENT}"

                    buildandPushDockerImage('docker-hub-credentials', 'my-app')
                    editHelmValues('my-app', imageTag)
                    deployOnAks('kubeconfig', namespace, './helm-chart')
                }
            }
        }
    }
}
```

### Monitoring Stack Deployment

```groovy
@Library('jenkins-shared-library') _

pipeline {
    agent any

    stages {
        stage('Deploy Monitoring') {
            steps {
                deployMonitoring('kubeconfig', 'monitoring', './monitoring')
            }
        }
    }
}
```

## Library Structure

```
jenkins-shared-library/
├── README.md                           # This documentation
└── vars/
    ├── microservicePipeline.groovy     # Main pipeline orchestration
    ├── checkRepo.groovy                # SCM checkout
    ├── runUnitTests.groovy             # Unit testing with pytest
    ├── build.groovy                    # Python application build
    ├── runSonarQubeAnalysis.groovy     # Code quality analysis
    ├── buildandPushDockerImage.groovy  # Docker operations
    ├── editHelmValues.groovy           # Helm chart updates
    ├── deployOnAks.groovy              # AKS deployment
    └── deployMonitoring.groovy         # Monitoring stack deployment
```

## Troubleshooting

### Common Issues

1. **PEP 668 Error in Testing**
   ```
   Solution: Use --break-system-packages flag in pip install
   ```

2. **Docker Build Failures**
   ```
   Check: Docker daemon is running and accessible
   Check: Jenkins user has Docker permissions
   ```

3. **Helm Deployment Issues**
   ```
   Check: kubeconfig credentials are valid
   Check: Namespace exists in cluster
   Check: Helm is installed on Jenkins agent
   ```

4. **SonarQube Connection**
   ```
   Check: SonarQube server configuration in Jenkins
   Check: sonar-project.properties file exists
   ```

### Debug Mode

Enable verbose logging by adding to your pipeline:

```groovy
environment {
    DEBUG = 'true'
}
```

## Contributing

1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/amazing-feature
   ```
3. **Make your changes**
4. **Test thoroughly**
5. **Commit your changes**
   ```bash
   git commit -m 'Add amazing feature'
   ```
6. **Push to the branch**
   ```bash
   git push origin feature/amazing-feature
   ```
7. **Open a Pull Request**

### Development Guidelines

- Follow Groovy naming conventions
- Add comprehensive documentation for new functions
- Include error handling and logging
- Test functions in isolation before integration
- Update README.md for any new features

## License

This project is licensed under the MIT License - see the [LICENSE](../LICENSE) file for details.

## Support

For support and questions:

- **Documentation**: Check this README and inline code comments
- **Issues**: Create an issue in the repository
- **Jenkins Community**: Jenkins user mailing list or Stack Overflow

---

**Note**: This library is designed for Jenkins Pipeline jobs. Ensure your Jenkins instance supports Declarative Pipelines for full compatibility.
