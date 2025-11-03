def call() {
    sh '''
        set -e
        echo "🔹 Checking if python3-venv is installed..."
        if ! dpkg -s python3-venv >/dev/null 2>&1; then
            echo "Installing python3-venv..."
            apt update && apt install -y python3-venv
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
