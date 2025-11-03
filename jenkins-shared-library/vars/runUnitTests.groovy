def call() {
    sh 'pip install -r requirements.txt'
    sh 'python -m pytest --junitxml=test-results.xml'
}
