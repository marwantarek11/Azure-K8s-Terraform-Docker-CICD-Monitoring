def call() {
    withSonarQubeEnv('SonarQube') {
        sh 'sonar-scanner'
    }
}
