def call() {
    sh '''
        apt update && apt install -y python3 python3-venv python3-pip

        python3 -m venv venv
        . venv/bin/activate

        pip install -r requirements.txt
        pytest --junitxml=test-results.xml
    '''
}
