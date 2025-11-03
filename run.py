# run.py
from app.main import app

# Register the health route
@app.route('/health')
def health():
    return "OK", 200
