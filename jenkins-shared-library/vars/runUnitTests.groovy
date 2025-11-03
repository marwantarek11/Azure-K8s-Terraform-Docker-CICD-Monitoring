def call() {
    sh '''
        # Ensure Python & pip exist
        if ! command -v python3 >/dev/null 2>&1; then
            apt update && apt install -y python3 python3-pip
        fi

        # Use user install directory to avoid permission errors
        pip3 install --user --upgrade pip
        pip3 install --user -r requirements.txt

        # Add local bin path to PATH
        export PATH="$HOME/.local/bin:$PATH"

        # Run pytest
        python3 -m pytest --junitxml=test-results.xml
    '''
}
