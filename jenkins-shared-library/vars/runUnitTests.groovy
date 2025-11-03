def call() {
    sh '''
        set -e
        echo "🔹 Checking if python3-venv is available..."
        if ! python3 -m venv --help >/dev/null 2>&1; then
            echo "⚠️  python3-venv missing. Trying to install..."
            if [ "$(id -u)" -eq 0 ]; then
                apt update && apt install -y python3-venv
            else
                echo "❌ Cannot install python3-venv — not running as root."
                echo "👉 Run with 'args \"-u root\"' in Jenkinsfile or use custom image."
                exit 1
            fi
        fi

        echo "🔹 Creating virtual environment..."
        python3 -m venv venv

        echo "🔹 Activating venv and installing dependencies..."
        . venv/bin/activate
        pip install --upgrade pip
        pip install -r requirements.txt

        echo "🔹 Running tests..."
        pytest --junitxml=test-results.xml
    '''
}
