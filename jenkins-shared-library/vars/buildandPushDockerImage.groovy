def call(String credentialsId, String imageName) {
    script {
        def fullImageName = "${imageName}:${env.BUILD_NUMBER}"
        docker.build(fullImageName)
        docker.withRegistry('https://registry.hub.docker.com', credentialsId) {
            docker.image(fullImageName).push()
            docker.image(fullImageName).push('latest')
        }
    }
}
