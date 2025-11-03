def call() {
    sh '''
        set -e
        echo "🔹 Installing dependencies globally (bypassing PEP 668)..."
        pip install --break-system-packages --upgrade pip
        pip install --break-system-packages -r requirements.txt pytest

        # Add user bin path so pytest and flask can be found
        export PATH=$PATH:/var/lib/jenkins/.local/bin

        # Add current workspace to PYTHONPATH so 'app' package is discoverable
        export PYTHONPATH=$PYTHONPATH:$(pwd)

        echo "🔹 Running tests..."
        pytest --junitxml=test-results.xml
    '''
}
