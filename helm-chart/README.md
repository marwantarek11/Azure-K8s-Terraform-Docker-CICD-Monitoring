# My Microservice Helm Chart

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Helm: v3](https://img.shields.io/badge/Helm-v3-blue.svg)](https://helm.sh/)

A production-ready Helm chart for deploying a Flask-based microservice application on Kubernetes. This chart provides a complete deployment solution with health checks, resource management, and monitoring integration.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Chart Details](#chart-details)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [Monitoring](#monitoring)
- [Development](#development)
- [Contributing](#contributing)
- [License](#license)

## Prerequisites

- Kubernetes 1.19+
- Helm 3.0+
- A container registry with your microservice image

## Chart Details

This Helm chart deploys a Flask microservice application with the following components:

- **Deployment**: Manages the application pods with configurable replicas
- **Service**: Exposes the application via LoadBalancer service type
- **Health Checks**: Readiness and liveness probes for application health monitoring
- **Resource Management**: Configurable CPU and memory limits/requests
- **Labels**: Standard Kubernetes labels for better resource management

### Key Features

- **Health Monitoring**: Built-in readiness and liveness probes
- **Scalability**: Configurable replica count
- **Resource Control**: CPU and memory resource management
- **Service Exposure**: LoadBalancer service for external access
- **Monitoring Ready**: Labels configured for Prometheus ServiceMonitor integration

## Installation

### Add Repository (if applicable)

```bash
# If this chart is hosted in a Helm repository
helm repo add your-repo https://your-helm-repo-url
helm repo update
```

### Install the Chart

1. **Clone or download the chart files**

```bash
# Navigate to the helm-chart directory
cd helm-chart
```

2. **Customize values (optional)**

```bash
# Edit values.yaml to customize your deployment
vim values.yaml
```

3. **Install the chart**

```bash
# Install with default values
helm install my-microservice-release .

# Install with custom values
helm install my-microservice-release . -f my-values.yaml

# Install in a specific namespace
helm install my-microservice-release . -n my-namespace --create-namespace
```

### Verify Installation

```bash
# Check pod status
kubectl get pods -l app.kubernetes.io/name=my-microservice

# Check service status
kubectl get svc -l app.kubernetes.io/name=my-microservice

# Check deployment status
kubectl get deployments -l app.kubernetes.io/name=my-microservice
```

## Configuration

The following table lists the configurable parameters of the my-microservice chart and their default values.

### Global Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `replicaCount` | Number of application replicas | `1` |

### Image Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `image.repository` | Container image repository | `my-microservice-app` |
| `image.tag` | Container image tag | `"latest"` |
| `image.pullPolicy` | Image pull policy | `IfNotPresent` |

### Service Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `service.type` | Kubernetes service type | `LoadBalancer` |
| `service.port` | Service port | `5000` |

### Resource Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `resources.limits.cpu` | CPU limit | `100m` |
| `resources.limits.memory` | Memory limit | `128Mi` |
| `resources.requests.cpu` | CPU request | `100m` |
| `resources.requests.memory` | Memory request | `128Mi` |

### Health Check Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `probe.enabled` | Enable health checks | `true` |
| `probe.path` | Health check endpoint path | `/health` |
| `probe.port` | Health check port | `5000` |
| `probe.initialDelaySeconds.readiness` | Readiness probe initial delay | `5` |
| `probe.initialDelaySeconds.liveness` | Liveness probe initial delay | `10` |
| `probe.periodSeconds` | Probe check interval | `10` |
| `probe.timeoutSeconds` | Probe timeout | `3` |
| `probe.failureThreshold` | Probe failure threshold | `3` |

### Node Scheduling Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `nodeSelector` | Node labels for pod assignment | `{}` |
| `tolerations` | Tolerations for node taints | `[]` |
| `affinity` | Affinity rules for pod scheduling | `{}` |

### Example Custom Values

```yaml
# custom-values.yaml
replicaCount: 3

image:
  repository: my-registry/my-microservice
  tag: "v1.2.0"

service:
  type: ClusterIP
  port: 8080

resources:
  limits:
    cpu: 500m
    memory: 512Mi
  requests:
    cpu: 250m
    memory: 256Mi

probe:
  enabled: true
  path: /api/health
  port: 8080
```

## Usage

### Accessing the Application

After installation, the application will be accessible via the LoadBalancer service:

```bash
# Get the external IP
kubectl get svc my-microservice-release

# Access the application
curl http://<EXTERNAL-IP>:5000/health
```

### Scaling the Application

```bash
# Scale to 3 replicas
helm upgrade my-microservice-release . --set replicaCount=3

# Or edit values.yaml and upgrade
helm upgrade my-microservice-release . -f updated-values.yaml
```

### Updating the Application

```bash
# Update with new image tag
helm upgrade my-microservice-release . --set image.tag=v1.1.0

# Update with new configuration
helm upgrade my-microservice-release . -f new-values.yaml
```

## Monitoring

This chart is designed to work seamlessly with Prometheus and Grafana for monitoring:

### Prometheus Integration

The deployment includes labels that allow Prometheus ServiceMonitor to automatically discover and scrape metrics from the application. Ensure your application exposes metrics at the `/metrics` endpoint.

### Health Checks

The chart includes both readiness and liveness probes that check the `/health` endpoint of your application.

### Resource Monitoring

Resource requests and limits are configured to ensure proper resource allocation and monitoring.

## Development

### Local Development

1. **Install Helm locally**

```bash
# Install Helm (Linux example)
curl https://get.helm.sh/helm-v3.12.0-linux-amd64.tar.gz -o helm.tar.gz
tar -zxvf helm.tar.gz
sudo mv linux-amd64/helm /usr/local/bin/helm
```

2. **Validate the chart**

```bash
# Lint the chart
helm lint .

# Template the chart
helm template my-microservice-release .

# Dry-run installation
helm install my-microservice-release . --dry-run --debug
```

3. **Test the chart**

```bash
# Run unit tests (if available)
helm test my-microservice-release
```

### Chart Structure

```
helm-chart/
├── Chart.yaml          # Chart metadata and dependencies
├── values.yaml         # Default configuration values
├── templates/          # Kubernetes manifest templates
│   ├── _helpers.tpl    # Template helper functions
│   ├── deployment.yaml # Application deployment
│   └── service.yaml    # Service definition
└── README.md          # This documentation
```

### Template Functions

The chart uses several helper functions defined in `_helpers.tpl`:

- `my-microservice.name`: Generates the application name
- `my-microservice.fullname`: Generates the fully qualified name
- `my-microservice.labels`: Common Kubernetes labels
- `my-microservice.selectorLabels`: Selector labels for services/deployments

## Troubleshooting

### Common Issues

1. **Pods not starting**
   ```bash
   kubectl describe pod <pod-name>
   kubectl logs <pod-name>
   ```

2. **Service not accessible**
   ```bash
   kubectl get svc
   kubectl describe svc <service-name>
   ```

3. **Health checks failing**
   ```bash
   kubectl exec -it <pod-name> -- curl http://localhost:5000/health
   ```

### Logs and Debugging

```bash
# View pod logs
kubectl logs -f deployment/my-microservice-release

# View events
kubectl get events --sort-by=.metadata.creationTimestamp

# Debug with temporary pod
kubectl run debug-pod --image=busybox --rm -it -- sh
```

## Upgrading

### Chart Version Updates

```bash
# Check for updates
helm repo update

# Upgrade the release
helm upgrade my-microservice-release .

# Check release status
helm status my-microservice-release
```

### Application Updates

```bash
# Update image version
helm upgrade my-microservice-release . --set image.tag=v2.0.0

# Rolling restart (if needed)
kubectl rollout restart deployment/my-microservice-release
```

## Uninstalling

```bash
# Uninstall the release
helm uninstall my-microservice-release

# Clean up PVCs if needed
kubectl delete pvc -l app.kubernetes.io/instance=my-microservice-release
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines

- Follow Helm chart best practices
- Update documentation for any configuration changes
- Test chart installation and upgrades
- Ensure backward compatibility

## License

This project is licensed under the MIT License - see the [LICENSE](../LICENSE) file for details.

## Support

For support and questions:

- Create an issue in the repository
- Check the troubleshooting section
- Review Kubernetes and Helm documentation

---

**Note**: This chart is designed for production use. Always test in a development environment before deploying to production.
