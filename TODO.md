# Monitoring Implementation TODO

- [x] Add prometheus_client to requirements.txt
- [x] Integrate Prometheus metrics in app/__init__.py to expose /metrics endpoint
- [x] Create monitoring/templates/servicemonitor.yaml for scraping microservice metrics
- [x] Update monitoring/values.yaml to add servicemonitor configuration
- [x] Update helm-chart/templates/deployment.yaml to add labels for ServiceMonitor matching
- [x] Install dependencies and test /metrics endpoint locally
- [x] Deploy monitoring stack and verify in Grafana
