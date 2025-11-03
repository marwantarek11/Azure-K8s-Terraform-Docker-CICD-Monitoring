def call() {
    sh '''
        # Ensure Python and pip are installed
        if ! command -v python3 >/dev/null 2>&1; then
            echo "Python3 not found. Installing..."
            sudo apt update && sudo apt install -y python3 python3-pip
        fi

        if ! command -v pip3 >/dev/null 2>&1; then
            echo "pip3 not found. Installing..."
            sudo apt update && sudo apt install -y python3-pip
        fi

        # Install dependencies
        pip3 install --upgrade pip
        pip3 install -r requirements.txt

        # Run tests
        python3 -m pytest --junitxml=test-results.xml
    '''
}
