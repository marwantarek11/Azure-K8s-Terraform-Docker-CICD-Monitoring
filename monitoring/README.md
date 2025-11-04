# Kubernetes Monitoring Stack Helm Chart

[![Helm](https://img.shields.io/badge/Helm-3.0+-blue.svg)](https://helm.sh/)
[![Kubernetes](https://img.shields.io/badge/Kubernetes-1.19+-326ce5.svg)](https://kubernetes.io/)
[![Prometheus](https://img.shields.io/badge/Prometheus-E6522C.svg)](https://prometheus.io/)
[![Grafana](https://img.shields.io/badge/Grafana-F46800.svg)](https://grafana.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A comprehensive Helm chart for deploying a production-ready monitoring stack on Kubernetes using the kube-prometheus-stack (formerly prometheus-operator). This chart provides Prometheus, Grafana, Alertmanager, and ServiceMonitor configurations optimized for microservices monitoring.

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Accessing the Services](#accessing-the-services)
- [Monitoring Your Applications](#monitoring-your-applications)
- [Dashboards](#dashboards)
- [Alerting](#alerting)
- [Scaling and High Availability](#scaling-and-high-availability)
- [Troubleshooting](#troubleshooting)
- [Uninstallation](#uninstallation)
- [Contributing](#contributing)
- [License](#license)

## Overview

This Helm chart deploys a complete monitoring solution for Kubernetes clusters, specifically designed for microservices architectures. It leverages the kube-prometheus-stack which includes:

- **Prometheus**: Metrics collection and storage
- **Grafana**: Visualization and dashboards
- **Alertmanager**: Alert routing and management
- **Node Exporter**: Host system metrics
- **kube-state-metrics**: Kubernetes object metrics
- **ServiceMonitor**: Automatic service discovery for metrics scraping

### Key Features

- **Production Ready**: Configured with persistent storage, LoadBalancer services, and proper resource limits
- **Azure Integration**: Optimized for Azure Kubernetes Service (AKS) with load balancer annotations
- **Microservice Monitoring**: Includes ServiceMonitor for automatic application metrics discovery
- **Pre-configured Dashboards**: Kubernetes, Node, and Prometheus overview dashboards
- **Alerting**: Built-in alerting rules for common Kubernetes issues
- **Scalability**: Configurable retention, storage, and resource allocation

## Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Applications  │───▶│  ServiceMonitor │───▶│   Prometheus    │
│   (/metrics)    │    │                 │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                                        │
                                                        ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Alertmanager  │◀───│   Alert Rules   │    │     Grafana     │
│                 │    │                 │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                                        ▲
                                                        │
┌─────────────────┐    ┌─────────────────┐              │
│  Node Exporter  │───▶│kube-state-metrics│─────────────┘
│                 │    │                 │
└─────────────────┘    └─────────────────┘
```

## Prerequisites

### System Requirements

- **Kubernetes**: Version 1.19 or higher
- **Helm**: Version 3.0 or higher
- **Cluster Access**: Administrative access to deploy cluster-level resources

### Cluster Requirements

- **Storage Class**: Default storage class supporting `ReadWriteOnce` access mode
- **Load Balancer**: Support for LoadBalancer service type (available in AKS, EKS, GKE)
- **Network Policies**: Optional but recommended for security

### Resource Requirements

| Component | CPU Requests | Memory Requests | CPU Limits | Memory Limits |
|-----------|--------------|-----------------|------------|---------------|
| Prometheus | 500m | 1Gi | 1 | 2Gi |
| Grafana | 100m | 128Mi | 200m | 256Mi |
| Alertmanager | 100m | 128Mi | 200m | 256Mi |
| Node Exporter | 10m | 32Mi | 50m | 64Mi |
| kube-state-metrics | 100m | 128Mi | 200m | 256Mi |

## Installation

### Method 1: Direct Installation

1. **Clone or download the chart files**

```bash
# Navigate to the monitoring directory
cd monitoring
```

2. **Update dependencies** (if needed)

```bash
# Update Helm dependencies
helm dependency update
```

3. **Install the chart**

```bash
# Install with default values
helm install monitoring .

# Install in a specific namespace
helm install monitoring . --namespace monitoring --create-namespace

# Install with custom values
helm install monitoring . -f my-values.yaml
```

### Method 2: Repository Installation (if published)

```bash
# Add repository (if available)
helm repo add my-repo https://my-helm-repo.com
helm repo update

# Install from repository
helm install monitoring my-repo/monitoring
```

### Verify Installation

```bash
# Check pod status
kubectl get pods -n monitoring

# Check services
kubectl get svc -n monitoring

# Check persistent volumes
kubectl get pvc -n monitoring
```

## Configuration

The chart is highly configurable through the `values.yaml` file. Below are the key configuration sections:

### Global Parameters

| Parameter | Description | Default |
|-----------|-------------|---------|
| `serviceMonitor.enabled` | Enable ServiceMonitor for application metrics | `true` |
| `serviceMonitor.namespace` | Namespace to monitor for services | `default` |
| `serviceMonitor.interval` | Metrics scraping interval | `30s` |
| `serviceMonitor.scrapeTimeout` | Metrics scraping timeout | `10s` |

### Prometheus Configuration

```yaml
kube-prometheus-stack:
  prometheus:
    service:
      type: LoadBalancer
      port: 9090
      loadBalancerIP: 52.149.54.251  # Your static IP
    prometheusSpec:
      retention: 30d
      storageSpec:
        volumeClaimTemplate:
          spec:
            accessModes: ["ReadWriteOnce"]
            resources:
              requests:
                storage: 50Gi
```

### Grafana Configuration

```yaml
kube-prometheus-stack:
  grafana:
    enabled: true
    adminPassword: admin  # Change in production!
    service:
      type: LoadBalancer
      port: 3000
      loadBalancerIP: 52.149.54.251  # Your static IP
    persistence:
      enabled: true
      size: 10Gi
```

### Alertmanager Configuration

```yaml
kube-prometheus-stack:
  alertmanager:
    enabled: true
    service:
      type: LoadBalancer
      port: 9093
      loadBalancerIP: 52.149.54.251  # Your static IP
```

### Example Custom Values

Create a `custom-values.yaml` file:

```yaml
# Custom configuration for production
kube-prometheus-stack:
  prometheus:
    prometheusSpec:
      retention: 90d  # Longer retention for production
      storageSpec:
        volumeClaimTemplate:
          spec:
            storageClassName: fast-ssd  # Use SSD storage
            resources:
              requests:
                storage: 100Gi  # More storage

  grafana:
    adminPassword: "your-secure-password"
    persistence:
      size: 20Gi  # Larger Grafana storage

# ServiceMonitor configuration
serviceMonitor:
  namespace: production  # Monitor production namespace
  interval: 15s  # More frequent scraping
```

## Accessing the Services

After installation, access the monitoring services through their LoadBalancer IPs:

### Prometheus

- **URL**: `http://<PROMETHEUS_IP>:9090`
- **Default Credentials**: None required
- **Purpose**: Metrics querying and visualization

### Grafana

- **URL**: `http://<GRAFANA_IP>:3000`
- **Username**: `admin`
- **Password**: `admin` (change immediately!)
- **Purpose**: Dashboard visualization and management

### Alertmanager

- **URL**: `http://<ALERTMANAGER_IP>:9093`
- **Purpose**: Alert management and routing

### Finding Service IPs

```bash
# Get service external IPs
kubectl get svc -n monitoring

# Example output:
# NAME                                    TYPE           CLUSTER-IP      EXTERNAL-IP      PORT(S)          AGE
# monitoring-grafana                     LoadBalancer   10.0.123.45     52.149.54.251    3000:31234/TCP   5m
# monitoring-kube-prometheus-alertmanager LoadBalancer  10.0.67.89      52.149.54.252    9093:31235/TCP   5m
# monitoring-kube-prometheus-prometheus   LoadBalancer  10.0.101.112    52.149.54.253    9090:31236/TCP   5m
```

## Monitoring Your Applications

### Automatic Service Discovery

The chart includes a ServiceMonitor that automatically discovers services with the label `app.kubernetes.io/name: my-microservice`.

Ensure your application deployment has the correct labels:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: my-microservice
spec:
  template:
    metadata:
      labels:
        app.kubernetes.io/name: my-microservice
        app.kubernetes.io/instance: my-release
    spec:
      containers:
      - name: my-app
        ports:
        - name: http
          containerPort: 5000
        # Expose /metrics endpoint for Prometheus
```

### Metrics Endpoint

Your application should expose metrics at the `/metrics` endpoint. For Python applications:

```python
from prometheus_client import make_wsgi_app, Gauge, Counter
from flask import Flask

app = Flask(__name__)

# Example metrics
REQUEST_COUNT = Counter('app_requests_total', 'Total requests', ['method', 'endpoint'])
RESPONSE_TIME = Gauge('app_response_time_seconds', 'Response time')

@app.route('/metrics')
def metrics():
    return make_wsgi_app()

@app.route('/')
def hello():
    REQUEST_COUNT.labels(method='GET', endpoint='/').inc()
    return 'Hello World!'
```

### Custom ServiceMonitor

For applications with different labels or configurations, create additional ServiceMonitors:

```yaml
apiVersion: monitoring.coreos.com/v1
kind: ServiceMonitor
metadata:
  name: custom-app-monitor
  namespace: monitoring
spec:
  selector:
    matchLabels:
      app: custom-app
  endpoints:
  - port: metrics
    path: /metrics
    interval: 30s
```

## Dashboards

The chart comes pre-configured with three essential dashboards:

### 1. Kubernetes Cluster Monitoring (ID: 3119)

- Cluster-level resource usage
- Node status and capacity
- Pod distribution across nodes
- API server metrics

### 2. Node Exporter Full (ID: 1860)

- Detailed node metrics
- CPU, memory, disk, and network usage
- System load and processes
- Hardware sensor data

### 3. Prometheus 2.0 Overview (ID: 3662)

- Prometheus server performance
- Target health and scrape statistics
- Storage and memory usage
- Alerting metrics

### Accessing Dashboards

1. Log in to Grafana (`http://<GRAFANA_IP>:3000`)
2. Navigate to **Dashboards** → **Browse**
3. Select from the available dashboards

### Adding Custom Dashboards

Add custom dashboards through the `values.yaml`:

```yaml
kube-prometheus-stack:
  grafana:
    dashboards:
      default:
        my-custom-dashboard:
          json: |
            {
              "dashboard": {
                "title": "My Custom Dashboard",
                "tags": ["custom"],
                "timezone": "browser",
                "panels": [...]
              }
            }
```

## Alerting

The monitoring stack includes pre-configured alerting rules for common Kubernetes issues:

### Default Alert Rules

- **KubePodCrashLooping**: Pods stuck in crash loops
- **KubePodNotReady**: Pods not ready for extended periods
- **KubeDeploymentReplicasMismatch**: Deployment replica mismatches
- **KubeStatefulSetReplicasMismatch**: StatefulSet replica issues
- **KubeNodeNotReady**: Unready nodes
- **KubeMemoryOvercommit**: Memory overcommitment
- **KubeCPUOvercommit**: CPU overcommitment

### Alertmanager Configuration

Alerts are routed through Alertmanager. Default configuration sends alerts to the Alertmanager UI only. For production, configure receivers:

```yaml
kube-prometheus-stack:
  alertmanager:
    config:
      global:
        smtp_smarthost: 'smtp.example.com:587'
        smtp_from: 'alerts@example.com'
      route:
        group_by: ['alertname']
        group_wait: 10s
        group_interval: 10s
        repeat_interval: 1h
        receiver: 'email'
      receivers:
      - name: 'email'
        email_configs:
        - to: 'team@example.com'
```

### Viewing Alerts

- **Prometheus UI**: `http://<PROMETHEUS_IP>:9090/alerts`
- **Alertmanager UI**: `http://<ALERTMANAGER_IP>:9093`

## Scaling and High Availability

### Prometheus High Availability

For production environments, enable multiple Prometheus replicas:

```yaml
kube-prometheus-stack:
  prometheus:
    prometheusSpec:
      replicas: 2
      retention: 90d
      storageSpec:
        volumeClaimTemplate:
          spec:
            resources:
              requests:
                storage: 100Gi
```

### Grafana High Availability

Enable multiple Grafana replicas (requires external database):

```yaml
kube-prometheus-stack:
  grafana:
    replicas: 2
    persistence:
      enabled: true
      type: pvc
      size: 20Gi
```

### Storage Considerations

- **Prometheus**: Requires significant storage for metrics retention
- **Grafana**: Needs storage for dashboards and configuration
- Use SSD storage classes for better performance
- Monitor storage usage and plan for growth

## Troubleshooting

### Common Issues

1. **Pods Not Starting**
   ```bash
   kubectl describe pod <pod-name> -n monitoring
   kubectl logs <pod-name> -n monitoring
   ```

2. **Service External IP Pending**
   ```bash
   kubectl get svc -n monitoring
   # Check cloud provider load balancer status
   ```

3. **Metrics Not Appearing**
   ```bash
   # Check ServiceMonitor status
   kubectl get servicemonitor -n monitoring
   kubectl describe servicemonitor monitoring-servicemonitor -n monitoring
   ```

4. **Grafana Login Issues**
   ```bash
   # Reset admin password
   kubectl exec -it deployment/monitoring-grafana -n monitoring -- grafana-cli admin reset-admin-password newpassword
   ```

5. **Storage Issues**
   ```bash
   kubectl get pvc -n monitoring
   kubectl describe pvc <pvc-name> -n monitoring
   ```

### Debug Commands

```bash
# Check all monitoring resources
kubectl get all -n monitoring

# View Prometheus targets
kubectl port-forward svc/monitoring-kube-prometheus-prometheus 9090:9090 -n monitoring
# Then visit http://localhost:9090/targets

# Check Grafana logs
kubectl logs -f deployment/monitoring-grafana -n monitoring

# Validate Helm release
helm status monitoring -n monitoring
```

### Performance Tuning

- **Prometheus**: Adjust `scrape_interval` and `evaluation_interval`
- **Storage**: Use faster storage classes
- **Resources**: Monitor and adjust CPU/memory limits
- **Retention**: Balance retention period with storage capacity

## Uninstallation

### Remove the Release

```bash
# Uninstall the chart
helm uninstall monitoring

# Remove namespace (if created)
kubectl delete namespace monitoring
```

### Clean Up Persistent Data

```bash
# Delete persistent volume claims
kubectl delete pvc -l app.kubernetes.io/instance=monitoring

# Delete persistent volumes (if needed)
kubectl delete pv <pv-names>
```

### Verify Cleanup

```bash
# Check for remaining resources
kubectl get all -n monitoring
kubectl get pvc -l app.kubernetes.io/instance=monitoring
```

## Upgrading

### Chart Updates

```bash
# Update dependencies
helm dependency update

# Upgrade the release
helm upgrade monitoring .

# Check release status
helm status monitoring
```

### Version Compatibility

- Check kube-prometheus-stack release notes for breaking changes
- Test upgrades in staging environment first
- Backup Grafana dashboards and Prometheus configuration

## Contributing

1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/enhanced-monitoring
   ```
3. **Make your changes**
4. **Test thoroughly**
5. **Update documentation**
6. **Commit your changes**
   ```bash
   git commit -m 'Add enhanced monitoring features'
   ```
7. **Push to the branch**
   ```bash
   git push origin feature/enhanced-monitoring
   ```
8. **Open a Pull Request**

### Development Guidelines

- Follow Helm chart best practices
- Test charts with `helm lint` and `helm template`
- Include comprehensive documentation
- Ensure backward compatibility
- Update version numbers appropriately

## License

This project is licensed under the MIT License - see the [LICENSE](../LICENSE) file for details.

## Support

For support and questions:

- **Documentation**: Check this README and inline comments
- **Issues**: Create an issue in the repository
- **Kubernetes Community**: Kubernetes Slack or Stack Overflow
- **Prometheus Community**: Prometheus mailing lists

---

**Note**: This monitoring stack is designed for production use. Always configure proper security, backup strategies, and monitoring for the monitoring system itself.
