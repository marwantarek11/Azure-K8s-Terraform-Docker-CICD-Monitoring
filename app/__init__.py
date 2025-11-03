
from flask import Flask, request, g
from app.routes.user_routes import user_blueprint
from app.routes.product_routes import product_blueprint
from prometheus_client import Counter, Histogram, generate_latest, CONTENT_TYPE_LATEST
import time

# Define Prometheus metrics
REQUEST_COUNT = Counter('http_requests_total', 'Total HTTP Requests', ['method', 'endpoint', 'status'])
REQUEST_LATENCY = Histogram('http_request_duration_seconds', 'HTTP Request Latency', ['method', 'endpoint'])

def create_app():
    app = Flask(__name__)

    # Register blueprints
    app.register_blueprint(user_blueprint)
    app.register_blueprint(product_blueprint)

    # Add Prometheus metrics endpoint
    @app.route('/metrics')
    def metrics():
        return generate_latest(), 200, {'Content-Type': CONTENT_TYPE_LATEST}

    # Middleware to track requests
    @app.before_request
    def before_request():
        g.start_time = time.time()

    @app.after_request
    def after_request(response):
        if hasattr(g, 'start_time'):
            request_latency = time.time() - g.start_time
            REQUEST_COUNT.labels(request.method, request.path, response.status_code).inc()
            REQUEST_LATENCY.labels(request.method, request.path).observe(request_latency)
        return response

    # Add health endpoint
    @app.route('/health')
    def health():
        return "OK", 200

    return app
