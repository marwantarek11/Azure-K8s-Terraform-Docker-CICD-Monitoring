def call() {
    sh '''
        set -e
        echo "🔹 Installing dependencies globally (CI-safe)..."
        pip install --upgrade pip
        pip install -r requirements.txt

        echo "🔹 Running tests..."
        pytest --junitxml=test-results.xml
    '''
}
