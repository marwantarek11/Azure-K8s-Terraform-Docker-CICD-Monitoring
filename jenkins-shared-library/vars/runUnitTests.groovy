def call() {
    sh '''
        set -e
        echo "🔹 Installing dependencies globally (bypassing PEP 668)..."
        pip install --break-system-packages --upgrade pip
        pip install --break-system-packages --force-reinstall -r requirements.txt pytest

        # Add user bin path and workspace to PYTHONPATH
        export PATH=$PATH:/var/lib/jenkins/.local/bin
        export PYTHONPATH=$(pwd)

        echo "🔹 Running tests..."
        pytest --junitxml=test-results.xml
    '''
}
