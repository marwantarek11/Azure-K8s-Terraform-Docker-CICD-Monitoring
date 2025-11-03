def call() {
    docker.image('python:3.11').inside {
        sh '''
            pip install -r requirements.txt
            python -m pytest --junitxml=test-results.xml
        '''
    }
}
