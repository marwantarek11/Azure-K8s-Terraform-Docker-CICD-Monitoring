# Flask Microservice Application

This is a Flask-based microservice application that provides RESTful APIs for managing users and products. The application is designed with a modular architecture, includes Prometheus metrics for monitoring, and is containerized for easy deployment.

## Project Structure

```
app/
├── __init__.py              # Flask application factory and configuration
├── main.py                  # Application entry point
├── routes/                  # API route definitions
│   ├── __init__.py
│   ├── product_routes.py    # Product-related endpoints
│   └── user_routes.py       # User-related endpoints
└── services/                # Business logic layer
    ├── __init__.py
    ├── product_service.py   # Product service implementation
    └── user_service.py      # User service implementation
```

## Features

### Core Functionality
- **User Management**: RESTful endpoints for retrieving user information
- **Product Management**: RESTful endpoints for retrieving product information
- **Health Check**: Endpoint for application health monitoring
- **Prometheus Metrics**: Built-in metrics collection for monitoring and observability

### Technical Features
- **Modular Architecture**: Clean separation of concerns with routes and services
- **Blueprint Pattern**: Organized routing using Flask blueprints
- **Middleware Integration**: Request tracking and metrics collection
- **Container Ready**: Optimized for containerized deployments

## API Endpoints

### Health Check
- `GET /health` - Returns application health status
  - Response: `200 OK` with body "OK"

### User Endpoints
- `GET /users` - Retrieve all users
  - Response: `200 OK` with JSON array of users
- `GET /users/<int:user_id>` - Retrieve specific user by ID
  - Response: `200 OK` with user JSON or `404 Not Found`

### Product Endpoints
- `GET /products` - Retrieve all products
  - Response: `200 OK` with JSON array of products
- `GET /products/<int:product_id>` - Retrieve specific product by ID
  - Response: `200 OK` with product JSON or `404 Not Found`

### Monitoring Endpoints
- `GET /metrics` - Prometheus metrics endpoint
  - Response: `200 OK` with Prometheus-formatted metrics

## Sample API Responses

### Users
```json
[
  {"id": 1, "name": "John Doe"},
  {"id": 2, "name": "Jane Doe"}
]
```

### Products
```json
[
  {"id": 1, "name": "Laptop"},
  {"id": 2, "name": "Smartphone"}
]
```

## Architecture Overview

### Application Factory Pattern
The application uses Flask's application factory pattern (`create_app()` function) for better testability and configuration management.

### Blueprint Organization
- Routes are organized into blueprints for better code organization
- `user_blueprint`: Handles all user-related routes
- `product_blueprint`: Handles all product-related routes

### Service Layer
- Business logic is separated into service classes
- `UserService`: Manages user data operations
- `ProductService`: Manages product data operations

### Middleware and Metrics
- Request tracking middleware measures request latency
- Prometheus counters and histograms for metrics collection
- Automatic metrics exposure via `/metrics` endpoint

## Dependencies

### Core Dependencies
- **Flask**: Web framework for building the API
- **prometheus_client**: Client library for Prometheus metrics

### Development Dependencies
- **pytest**: Testing framework
- **Werkzeug**: WSGI utility library (included with Flask)

## Configuration

### Environment Variables
- `FLASK_APP`: Set to `run.py` for Flask CLI
- `FLASK_RUN_HOST`: Host to bind the server (default: 0.0.0.0)
- `FLASK_RUN_PORT`: Port to bind the server (default: 5000)
- `FLASK_DEBUG`: Debug mode flag (default: False)

### Application Settings
The application is configured for production use with:
- Debug mode disabled
- Binding to all interfaces (0.0.0.0)
- Port 5000 for container compatibility

## Running the Application

### Local Development
```bash
# Install dependencies
pip install -r requirements.txt

# Run the application
python run.py
# or
flask run --host=0.0.0.0 --port=5000
```

### Using Docker
```bash
# Build the image
docker build -t flask-microservice .

# Run the container
docker run -p 5000:5000 flask-microservice
```

### Health Check
```bash
curl http://localhost:5000/health
```

## Testing

### Running Tests
```bash
# Run all tests
pytest

# Run with coverage
pytest --cov=app
```

### Test Structure
Tests are located in the `tests/` directory:
- `test_app.py`: Application-level tests

## Monitoring and Observability

### Prometheus Metrics
The application exposes the following metrics:

- **http_requests_total**: Counter for total HTTP requests
  - Labels: method, endpoint, status
- **http_request_duration_seconds**: Histogram for request latency
  - Labels: method, endpoint

### Accessing Metrics
```bash
curl http://localhost:5000/metrics
```

### Integration with Monitoring Stack
The application is designed to work with:
- **Prometheus**: For metrics collection
- **Grafana**: For visualization
- **Alertmanager**: For alerting

## Deployment

### Container Configuration
- **Base Image**: `python:3.12-slim`
- **Health Check**: Built-in health check using curl
- **Non-root User**: Runs as `appuser` for security
- **Port**: Exposes port 5000

### Kubernetes Deployment
The application can be deployed using the provided Helm chart in the `helm-chart/` directory.

### CI/CD Integration
The application integrates with Jenkins pipelines defined in the `Jenkinsfile` and shared libraries in `jenkins-shared-library/`.

## Security Considerations

- **Input Validation**: Basic ID validation for route parameters
- **Error Handling**: Proper HTTP status codes and error messages
- **Container Security**: Non-root user execution
- **Dependency Management**: Pinned versions in requirements.txt

## Development Guidelines

### Code Organization
- Keep routes thin and delegate to services
- Use meaningful variable and function names
- Follow PEP 8 style guidelines
- Add docstrings for functions and classes

### Adding New Features
1. Create service methods in appropriate service class
2. Add route handlers in corresponding blueprint
3. Register new blueprints in `create_app()` if needed
4. Update this README with new endpoints

### Testing
- Write unit tests for service methods
- Write integration tests for API endpoints
- Ensure all tests pass before committing

## Troubleshooting

### Common Issues

1. **Port Already in Use**
   - Change the port in `main.py` or use a different port
   - Check for other processes using port 5000

2. **Import Errors**
   - Ensure all dependencies are installed: `pip install -r requirements.txt`
   - Check Python path and virtual environment

3. **Metrics Not Appearing**
   - Verify Prometheus is configured to scrape the `/metrics` endpoint
   - Check application logs for errors

### Logs
- Application logs are output to stdout/stderr
- Use `docker logs` for containerized deployments
- Check Flask debug logs when `FLASK_DEBUG=True`

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make changes following the development guidelines
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For issues and questions:
1. Check the troubleshooting section
2. Review Flask documentation
3. Check Prometheus client documentation
4. Create an issue in the repository with detailed information
