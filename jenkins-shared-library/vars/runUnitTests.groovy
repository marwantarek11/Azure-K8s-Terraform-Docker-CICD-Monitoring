def call() {
    sh '''
        set -e
        echo "🔹 Installing dependencies globally (bypassing PEP 668)..."
        pip install --break-system-packages --upgrade pip
        pip install --break-system-packages -r requirements.txt

        echo "🔹 Running tests..."
        pytest --junitxml=test-results.xml
    '''
}
