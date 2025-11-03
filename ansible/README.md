# Ansible CI/CD Setup

This Ansible role installs Jenkins, SonarQube, and Docker on an Ubuntu VM for CI/CD pipeline execution.

## Prerequisites

- Ansible installed on your local machine
- SSH access to the target VM
- Sudo privileges on the target VM

## Azure Dynamic Inventory

This setup uses Ansible's Azure Resource Manager dynamic inventory plugin to automatically discover and manage Azure VMs.

## Prerequisites

- Azure CLI installed and authenticated (`az login`)
- Ansible Azure collection installed
- Azure VMs deployed via Terraform

## Usage

1. **Deploy Infrastructure**: Run Terraform to create your Azure resources
2. **Authenticate Azure**: Ensure you're logged in to Azure CLI
3. **Run Playbook**: Use the dynamic inventory directly:
   ```bash
   ansible-playbook -i inventory.azure_rm.yml playbook.yml
   ```

## Inventory Configuration

The `inventory.azure_rm.yml` file:
- Targets VMs in the `my-k8s-rg` resource group
- Uses automatic authentication
- Groups hosts by location and tags
- Configures SSH connection details automatically

## Alternative: Static Inventory

If you prefer static inventory, you can still use the generated `inventory.ini`:
```bash
python3 generate_inventory.py
ansible-playbook -i inventory.ini playbook.yml
```

## What gets installed

- **Jenkins**: Latest stable version from the official repository
- **SonarQube**: Version 10.3.0.82913 (Community Edition)
- **Docker**: Latest CE version from Docker's official repository
- **Java 17**: Required for Jenkins and SonarQube

## Services

After installation, the following services will be running:
- Jenkins: http://your-vm-ip:8080
- SonarQube: http://your-vm-ip:9000
- Docker: Available via command line

## Initial Setup

### Jenkins
1. Access Jenkins at http://your-vm-ip:8080
2. Retrieve the initial admin password:
   ```bash
   sudo cat /var/lib/jenkins/secrets/initialAdminPassword
   ```
3. Complete the setup wizard and install suggested plugins

### SonarQube
1. Access SonarQube at http://your-vm-ip:9000
2. Default credentials: admin/admin
3. Change the default password after first login

## Notes

- The `azureuser` is added to the `docker` group for Docker access without sudo
- All services are configured to start automatically on boot
- Firewall rules may need to be adjusted for external access
