def call(Map config = [:]) {
    pipeline {
        agent any

        environment {
            DOCKER_IMAGE = config.dockerImage ?: 'my-microservice-app'
            DOCKER_TAG = "${env.BUILD_NUMBER}"
            HELM_CHART_PATH = config.helmChartPath ?: './helm-chart'
            KUBECONFIG = credentials('kubeconfig')
        }

        stages {
            stage('Repo Checkout') {
                steps {
                    checkout scm
                }
            }

            stage('Running Test') {
                steps {
                    sh 'pip install -r requirements.txt'
                    sh 'python -m pytest --junitxml=test-results.xml'
                }
            }

            stage('Build App') {
                steps {
                    sh 'python setup.py build'
                }
            }

            stage('Sonarqube Analysis') {
                steps {
                    withSonarQubeEnv('SonarQube') {
                        sh 'sonar-scanner'
                    }
                }
            }

            stage('Build & Push Docker Image') {
                steps {
                    script {
                        docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
                        docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                            docker.image("${DOCKER_IMAGE}:${DOCKER_TAG}").push()
                            docker.image("${DOCKER_IMAGE}:${DOCKER_TAG}").push('latest')
                        }
                    }
                }
            }

            stage('Edit Helm Chart with Image Name and Tag') {
                steps {
                    script {
                        sh "sed -i 's|image:.*|image: ${DOCKER_IMAGE}:${DOCKER_TAG}|g' ${HELM_CHART_PATH}/values.yaml"
                    }
                }
            }

            stage('Deploy on AKS') {
                steps {
                    script {
                        sh "helm upgrade --install my-microservice ${HELM_CHART_PATH} --namespace default"
                    }
                }
            }
        }

        post {
            always {
                junit 'test-results.xml'
            }
            success {
                echo 'Pipeline succeeded!'
            }
            failure {
                echo 'Pipeline failed!'
            }
        }
    }
}
