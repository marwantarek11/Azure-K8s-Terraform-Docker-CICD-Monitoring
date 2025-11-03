@Library('jenkins-shared-library') _

pipeline {
    agent any

    environment {
        dockerHubCredentialsID = 'docker-hub-credentials'
        imageName = 'marwantarek11/microservice-app'
        kubeconfigCredentialsID = 'kubeconfig'
        nameSpace = 'default'
        sonarqubeUrl = "http://${env.VM_PUBLIC_IP}:9000/"
        sonarTokenCredentialsID = 'sonarqube'
        helmChartPath = './helm-chart'
        dockerTag = "${env.BUILD_NUMBER}"
    }

    stages {
        stage('Repo Checkout') {
            steps {
                script {
                    checkRepo()
                }
            }
        }

        stage('Running Test') {
            steps {
                script {
                    runUnitTests()
                }
            }
        }

        stage('Build App') {
            steps {
                script {
                    build()
                }
            }
        }

        stage('Sonarqube Analysis') {
            steps {
                script {
                    runSonarQubeAnalysis()
                }
            }
        }

        stage('Build & Push Docker Image') {
            steps {
                script {
                    buildandPushDockerImage(dockerHubCredentialsID, imageName)
                }
            }
        }

        stage('Edit Helm Values with Image Name and Tag') {
            steps {
                script {
                    editHelmValues(imageName, dockerTag)
                }
            }
        }

        stage('Deploy on AKS') {
            steps {
                script {
                    deployOnAks(kubeconfigCredentialsID, nameSpace, helmChartPath)
                }
            }
        }

        stage('Deploy Monitoring Stack') {
            steps {
                script {
                    deployMonitoring(kubeconfigCredentialsID, nameSpace, './monitoring')
                }
            }
        }
    }

    post {
        success {
            echo "${JOB_NAME}-${BUILD_NUMBER} pipeline succeeded"
        }
        failure {
            echo "${JOB_NAME}-${BUILD_NUMBER} pipeline failed"
        }
    }
}
