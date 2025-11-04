# Ansible CI/CD Infrastructure Setup

This Ansible project automates the deployment and configuration of a complete CI/CD infrastructure on Azure VMs, including Jenkins, SonarQube, and Docker for containerized application development and deployment.

## Overview

The project consists of Ansible playbooks and roles that provision and configure CI/CD tools on Ubuntu-based Azure Virtual Machines. It supports both dynamic inventory (Azure Resource Manager) and static inventory configurations.

## Project Structure

```
ansible/
├── ansible.cfg                 # Ansible configuration file
├── inventory.ini              # Static inventory file (generated)
├── playbook.yml               # Main playbook for CI/CD setup
├── generate_inventory.py      # Python script to generate inventory from Terraform outputs
├── README.md                  # This documentation file
└── roles/
    ├── docker/
    │   ├── handlers/
    │   │   └── main.yml       # Docker service restart handler
    │   ├── tasks/
    │   │   └── main.yml       # Docker installation tasks
    │   └── vars/
    │       └── main.yml       # Docker configuration variables
    ├── jenkins/
    │   ├── handlers/
    │   │   └── main.yml       # Jenkins service restart handler
    │   ├── tasks/
    │   │   └── main.yml       # Jenkins installation and configuration tasks
    │   └── vars/
    │       └── main.yml       # Jenkins configuration variables
    └── sonarqube/
        ├── handlers/
        │   └── main.yml       # SonarQube service restart handler
        ├── tasks/
        │   └── main.yml       # SonarQube installation and configuration tasks
        └── vars/
            └── main.yml       # SonarQube configuration variables
```

## Prerequisites

### System Requirements
- **Ansible**: Version 2.9+ installed on control machine
- **Python**: Version 3.6+ with required modules (`subprocess`, `json`, `os`)
- **SSH Access**: SSH key-based authentication to target VMs
- **Sudo Privileges**: Administrative access on target VMs

### Infrastructure Requirements
- **Azure Subscription**: Active Azure account with appropriate permissions
- **Azure CLI**: Installed and authenticated (`az login`)
- **Terraform**: For infrastructure provisioning (optional, if using static inventory)
- **Target VMs**: Ubuntu-based Azure Virtual Machines

### Dependencies
- **Azure Collection**: `ansible-galaxy collection install azure.azcollection`
- **Azure CLI**: `az login` for dynamic inventory authentication

## Configuration Files

### ansible.cfg
```ini
[defaults]
inventory = inventory.ini
collections_paths = ~/.ansible/collections
host_key_checking = False

[privilege_escalation]
become = true
become_method = sudo
become_user = root
become_ask_pass = false
```

### inventory.ini (Generated)
```ini
[ci_cd_servers]
ci-cd-vm ansible_host=<VM_PUBLIC_IP> ansible_user=azureuser ansible_ssh_private_key_file=~/.ssh/id_rsa

[all:vars]
ansible_python_interpreter=/usr/bin/python3
```

## Installation Steps

### Step 1: Infrastructure Provisioning
Deploy Azure resources using Terraform (from the `terraform-k8s` directory):
```bash
cd ../terraform-k8s
terraform init
terraform plan
terraform apply
```

### Step 2: Generate Inventory (Optional)
If using static inventory, generate the inventory file from Terraform outputs:
```bash
python3 generate_inventory.py
```

This script:
- Executes `terraform output -json` in the `../terraform-k8s` directory
- Extracts VM public IP and SSH key information
- Generates `inventory.ini` with the retrieved values

### Step 3: Install Azure Collection (for Dynamic Inventory)
```bash
ansible-galaxy collection install azure.azcollection
```

### Step 4: Authenticate with Azure
```bash
az login
```

### Step 5: Execute Playbook
Run the main playbook using dynamic inventory:
```bash
ansible-playbook -i inventory.azure_rm.yml playbook.yml
```

Or using static inventory:
```bash
ansible-playbook -i inventory.ini playbook.yml
```

## Roles and Components

### Docker Role
- **Purpose**: Installs Docker Community Edition
- **Key Tasks**:
  - Updates package cache
  - Installs required dependencies (`apt-transport-https`, `ca-certificates`, `curl`, `gnupg`, `lsb-release`)
  - Adds Docker's official GPG key
  - Configures Docker repository
  - Installs Docker CE
  - Starts and enables Docker service
  - Adds specified user to `docker` group
- **Variables**:
  - `docker_packages`: List of required packages
  - `docker_gpg_key_url`: Docker GPG key URL
  - `docker_repo`: Docker repository configuration
  - `docker_package`: Docker package name
  - `docker_service`: Docker service name
  - `docker_user`: User to add to docker group (default: `azureuser`)

### Jenkins Role
- **Purpose**: Installs and configures Jenkins CI server
- **Key Tasks**:
  - Updates package cache
  - Installs Java 17 (OpenJDK)
  - Adds Jenkins APT key and repository
  - Installs Jenkins
  - Starts and enables Jenkins service
  - Retrieves initial admin password
  - Creates CRUMB authentication request
  - Sets up Jenkins administration account
  - Installs specified Jenkins plugins
  - Skips initial setup wizard
  - Restarts Jenkins service
- **Variables**:
  - `java_version`: Java version to install
  - `jenkins_user`: Jenkins admin username
  - `jenkins_password`: Jenkins admin password
  - `jenkins_fullname`: Jenkins admin full name
  - `jenkins_email`: Jenkins admin email
  - `jenkins_plugins`: List of plugins to install
- **Installed Plugins**:
  - `workflow-aggregator`
  - `credentials`
  - `credentials-binding`
  - `pipeline`
  - `github-branch-source`
  - `pipeline-build-step`
  - `pipeline-stage-step`
  - `pipeline-stage-view`
  - `pipeline-utility-steps`
  - `workflow-cps-global-lib`
  - `pipeline-github-lib`
  - `git`
  - `github`
  - `docker-plugin`
  - `sonar`
  - `kubernetes`
  - `kubernetes-cli`
  - `maven-plugin`
  - `pipeline-maven`

### SonarQube Role
- **Purpose**: Installs and configures SonarQube code quality analysis server
- **Key Tasks**:
  - Updates package cache
  - Installs Java 17 (OpenJDK)
  - Creates SonarQube system user
  - Downloads and extracts SonarQube
  - Renames SonarQube directory
  - Sets proper ownership and permissions
  - Creates systemd service file
  - Starts and enables SonarQube service
  - Downloads and extracts SonarScanner CLI
  - Configures SonarScanner properties
  - Sets execute permissions on SonarScanner binary
  - Creates symlink for SonarScanner
- **Variables**:
  - `java_version`: Java version to install
  - `sonarqube_user`: SonarQube system user
  - `sonarqube_zip_url`: SonarQube download URL
  - `sonarqube_dir`: SonarQube installation directory
  - `sonar_scanner_version`: SonarScanner version
  - `sonar_scanner_zip_url`: SonarScanner download URL

## Services Configuration

### Jenkins
- **URL**: `http://<VM_PUBLIC_IP>:8080`
- **Initial Setup**:
  1. Access Jenkins web interface
  2. Retrieve initial admin password: `sudo cat /var/lib/jenkins/secrets/initialAdminPassword`
  3. Complete setup wizard
  4. Install suggested plugins

### SonarQube
- **URL**: `http://<VM_PUBLIC_IP>:9000`
- **Default Credentials**: `admin` / `admin`
- **Initial Setup**:
  1. Access SonarQube web interface
  2. Login with default credentials
  3. Change default password
  4. Configure projects and quality profiles

### Docker
- **Service**: Available via command line
- **User Access**: `azureuser` added to `docker` group for sudo-less Docker commands

## Security Considerations

- **SSH Keys**: Uses key-based authentication (no password authentication)
- **Firewall**: Ensure Azure NSG rules allow access to ports 22 (SSH), 8080 (Jenkins), 9000 (SonarQube)
- **Service Accounts**: Dedicated system users for Jenkins and SonarQube
- **Password Changes**: Change default passwords after initial setup
- **Updates**: Regularly update packages and services

## Troubleshooting

### Common Issues

1. **SSH Connection Failures**:
   - Verify SSH key permissions: `chmod 600 ~/.ssh/id_rsa`
   - Check Azure NSG rules for SSH access
   - Ensure VM is running and accessible

2. **Package Installation Errors**:
   - Update package cache: `sudo apt update`
   - Check network connectivity
   - Verify Ubuntu version compatibility

3. **Service Startup Failures**:
   - Check service status: `sudo systemctl status <service>`
   - Review service logs: `sudo journalctl -u <service>`
   - Verify Java installation and version

4. **Ansible Execution Errors**:
   - Test connectivity: `ansible -i inventory.ini ci_cd_servers -m ping`
   - Check Ansible version and Python interpreter
   - Verify inventory file syntax

### Logs and Debugging

- **Ansible Logs**: Use `-v`, `-vv`, or `-vvv` for verbose output
- **Service Logs**: `sudo journalctl -u jenkins`, `sudo journalctl -u sonarqube`
- **Application Logs**: `/var/log/jenkins/jenkins.log`, `/opt/sonarqube/logs/`

## Maintenance

### Updates
- **Jenkins**: Update via web interface or CLI
- **SonarQube**: Download and replace installation directory
- **Docker**: `sudo apt update && sudo apt upgrade docker-ce`
- **OS**: Regular system updates: `sudo apt update && sudo apt upgrade`

### Backups
- **Jenkins**: Backup `/var/lib/jenkins` directory
- **SonarQube**: Backup database and configuration files
- **Docker**: Backup container images and volumes

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make changes following Ansible best practices
4. Test changes thoroughly
5. Submit a pull request with detailed description

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For issues and questions:
1. Check the troubleshooting section
2. Review Ansible documentation
3. Check service-specific documentation (Jenkins, SonarQube, Docker)
4. Create an issue in the repository with detailed information
