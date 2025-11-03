# TODO: Switch to Built-in Grafana in kube-prometheus-stack

## Tasks
- [x] Remove the separate 'grafana' dependency from monitoring/Chart.yaml
- [x] Update monitoring/values.yaml to enable Grafana under kube-prometheus-stack and move configurations
- [x] Verify datasource URL in values.yaml for built-in Prometheus service
- [x] Update Chart.lock after dependency changes (run helm dependency update)
