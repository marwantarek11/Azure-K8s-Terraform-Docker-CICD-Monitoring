# run.py
from app.main import app

# Register the health route
@app.route('/health')
def health():
    return "OK", 200

# DO NOT call app.run() — let `flask run` handle it