# Terraform Azure Kubernetes Service (AKS) Infrastructure

[![Terraform](https://img.shields.io/badge/Terraform-1.0+-623CE4.svg)](https://www.terraform.io/)
[![Azure](https://img.shields.io/badge/Azure-Resource_Manager-0078D4.svg)](https://azure.microsoft.com/)
[![Kubernetes](https://img.shields.io/badge/Kubernetes-1.19+-326ce5.svg)](https://kubernetes.io/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A comprehensive Terraform configuration for deploying Azure Kubernetes Service (AKS) clusters with supporting infrastructure including resource groups, virtual machines, and networking components. This infrastructure-as-code solution is designed for microservices development and deployment.

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Deployment](#deployment)
- [Outputs](#outputs)
- [Modules](#modules)
- [Security](#security)
- [Cost Optimization](#cost-optimization)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [License](#license)

## Overview

This Terraform project creates a complete Azure infrastructure for running microservices on Kubernetes. It provisions:

- **Azure Resource Group**: Logical container for all resources
- **Azure Kubernetes Service (AKS)**: Managed Kubernetes cluster with auto-scaling
- **Azure Virtual Machine**: Ubuntu VM for CI/CD tools (Jenkins, SonarQube)
- **Networking**: Virtual networks, subnets, security groups, and load balancers
- **Security**: SSH keys, network security groups, and Azure AD integration

### Key Features

- **Modular Design**: Reusable Terraform modules for different components
- **Production Ready**: Configured with proper security, monitoring, and scalability
- **Cost Effective**: Uses Azure Free Tier eligible resources where possible
- **CI/CD Ready**: Includes VM with pre-configured security groups for Jenkins and SonarQube
- **Microservices Optimized**: Network configuration suitable for containerized applications

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Azure Subscription                       │
│  ┌─────────────────────────────────────────────────────────┐  │
│  │                Resource Group                           │  │
│  │  ┌─────────────────────────────────────────────────────┐  │  │
│  │  │              Virtual Network                        │  │  │
│  │  │  ┌─────────────────┐    ┌─────────────────────────┐  │  │  │
│  │  │  │   AKS Cluster   │    │   Ubuntu VM (CI/CD)     │  │  │  │
│  │  │  │                 │    │                         │  │  │  │
│  │  │  │ • Control Plane │    │ • Jenkins (8080)       │  │  │  │
│  │  │  │ • Node Pools    │    │ • SonarQube (9000)     │  │  │  │
│  │  │  │ • Load Balancer │    │ • Microservice (5000)  │  │  │  │
│  │  │  │ • Auto-scaling  │    │ • SSH Access            │  │  │  │
│  │  │  └─────────────────┘    └─────────────────────────┘  │  │  │
│  │  └─────────────────────────────────────────────────────┘  │  │
│  └─────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

## Prerequisites

### System Requirements

- **Terraform**: Version 1.0 or higher
- **Azure CLI**: Version 2.0 or higher
- **Git**: For version control

### Azure Requirements

- **Azure Subscription**: Active subscription with sufficient credits
- **Azure AD Permissions**: Contributor or Owner role on the subscription
- **Resource Quotas**: Sufficient quotas for AKS, VMs, and networking resources

### Authentication

```bash
# Login to Azure CLI
az login

# Set the subscription (if multiple subscriptions)
az account set --subscription "your-subscription-id"
```

## Installation

### Clone the Repository

```bash
git clone <repository-url>
cd terraform-k8s
```

### Initialize Terraform

```bash
# Initialize Terraform providers and modules
terraform init

# Validate the configuration
terraform validate

# Format the configuration files
terraform fmt -recursive
```

### Plan the Deployment

```bash
# Generate and review the execution plan
terraform plan -out=tfplan

# Review the plan output for resources to be created
```

## Configuration

### Variables

The configuration uses variables defined in `variables.tf` and `terraform.tfvars`. Key variables include:

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `resource_group_name` | Name of the Azure Resource Group | `k8s-rg` | No |
| `location` | Azure region for resources | `East US` | No |
| `cluster_name` | Name of the AKS cluster | `my-aks-cluster` | No |
| `node_count` | Number of nodes in AKS | `1` | No |
| `vm_size` | VM size for AKS nodes | `Standard_DS2_v2` | No |
| `kubernetes_version` | Kubernetes version | `1.33.0` | No |
| `vm_name` | Name of the Ubuntu VM | `ubuntu-vm` | No |
| `vm_size_standalone` | VM size for standalone VM | `Standard_B1s` | No |
| `admin_username` | Admin username for VM | `azureuser` | No |

### Custom Configuration

Create a `terraform.tfvars` file to override defaults:

```hcl
# Custom configuration for production
resource_group_name = "prod-microservices-rg"
location            = "West US 2"
cluster_name        = "prod-microservices-aks"
node_count          = 3
vm_size             = "Standard_DS3_v2"
kubernetes_version  = "1.33.3"
vm_name             = "prod-ci-cd-vm"
vm_size_standalone  = "Standard_D2as_v5"
admin_username      = "azureadmin"
```

### Environment-Specific Configurations

For different environments, create separate `.tfvars` files:

```bash
# Development environment
terraform plan -var-file="dev.tfvars"

# Production environment
terraform plan -var-file="prod.tfvars"
```

## Deployment

### Apply the Configuration

```bash
# Apply the Terraform configuration
terraform apply tfplan

# Or apply directly (not recommended for production)
terraform apply
```

### Monitor Deployment

```bash
# Check deployment status
terraform show

# View resource outputs
terraform output

# Check Azure resources
az resource list --resource-group $(terraform output -raw resource_group_name)
```

### Post-Deployment Configuration

After deployment, configure kubectl to access the AKS cluster:

```bash
# Get AKS credentials
az aks get-credentials --resource-group $(terraform output -raw resource_group_name) --name $(terraform output -raw aks_cluster_name)

# Verify cluster access
kubectl get nodes
kubectl get pods -A
```

## Outputs

The Terraform configuration provides the following outputs:

| Output | Description |
|--------|-------------|
| `resource_group_name` | Name of the created resource group |
| `aks_cluster_name` | Name of the AKS cluster |
| `aks_cluster_id` | ID of the AKS cluster |
| `kube_config` | Kubernetes configuration (sensitive) |
| `vm_public_ip` | Public IP address of the Ubuntu VM |
| `vm_private_ip` | Private IP address of the Ubuntu VM |
| `vm_name` | Name of the Ubuntu VM |
| `vm_ssh_private_key` | Private SSH key for VM access (sensitive) |

### Accessing Outputs

```bash
# View all outputs
terraform output

# Get specific output value
terraform output vm_public_ip

# Save outputs to file
terraform output -json > outputs.json
```

## Modules

The configuration is organized into reusable modules:

### Resource Group Module (`modules/resource_group`)

Creates an Azure Resource Group with proper tagging.

**Inputs:**
- `name`: Resource group name
- `location`: Azure region

**Outputs:**
- `name`: Resource group name
- `location`: Resource group location

### AKS Module (`modules/aks`)

Provisions an Azure Kubernetes Service cluster.

**Inputs:**
- `resource_group_name`: Parent resource group name
- `location`: Azure region
- `cluster_name`: AKS cluster name
- `node_count`: Number of nodes
- `vm_size`: VM size for nodes
- `kubernetes_version`: Kubernetes version

**Outputs:**
- `cluster_name`: AKS cluster name
- `cluster_id`: AKS cluster ID
- `kube_config`: Kubernetes configuration

### VM Module (`modules/vm`)

Creates an Ubuntu Virtual Machine with networking and security.

**Inputs:**
- `vm_name`: VM name
- `vm_size_standalone`: VM size
- `admin_username`: Admin username
- `location`: Azure region
- `resource_group_name`: Parent resource group

**Outputs:**
- `vm_public_ip`: Public IP address
- `vm_private_ip`: Private IP address
- `vm_name`: VM name
- `ssh_private_key`: SSH private key

## Security

### Network Security

- **Network Security Groups (NSG)**: Configured with minimal required ports
- **SSH Access**: Restricted to generated key pairs only
- **Service Ports**: Open only for Jenkins (8080), SonarQube (9000), and microservices (5000)

### Authentication

- **Azure AD Integration**: AKS uses system-assigned managed identity
- **SSH Keys**: Automatically generated RSA key pairs
- **RBAC**: Kubernetes RBAC enabled for cluster access control

### Best Practices

```hcl
# Enable Azure AD integration (optional enhancement)
azure_active_directory {
  managed = true
  admin_group_object_ids = ["group-object-id"]
}
```

## Cost Optimization

### Free Tier Resources

The configuration uses Azure Free Tier eligible resources:

- **AKS**: Free tier with 1 node cluster
- **VM**: Standard_B1s (free tier eligible)
- **Storage**: Standard_LRS for cost efficiency

### Scaling Considerations

```hcl
# Enable cluster autoscaling for cost optimization
default_node_pool {
  name                = "default"
  node_count          = 1
  min_count           = 1
  max_count           = 5
  vm_size             = "Standard_DS2_v2"
  enable_auto_scaling = true
}
```

### Cost Monitoring

```bash
# Check Azure costs
az cost export --name "terraform-costs" --query "properties" --type "Usage"

# Monitor resource usage
az monitor metrics list --resource /subscriptions/... --metric "Percentage CPU"
```

## Troubleshooting

### Common Issues

1. **Authentication Errors**
   ```bash
   # Re-authenticate with Azure
   az login --use-device-code
   az account set --subscription "your-subscription-id"
   ```

2. **Quota Exceeded**
   ```bash
   # Check quotas
   az vm list-usage --location "West US 2"
   az aks list-usage --location "West US 2"
   ```

3. **Resource Conflicts**
   ```bash
   # Clean up resources
   terraform destroy
   ```

4. **Network Issues**
   ```bash
   # Check network security groups
   az network nsg list --resource-group $(terraform output -raw resource_group_name)
   ```

### Debug Commands

```bash
# Enable verbose logging
export TF_LOG=DEBUG
terraform apply

# Check Terraform state
terraform state list
terraform state show azurerm_kubernetes_cluster.aks

# Validate Azure resources
az aks show --resource-group $(terraform output -raw resource_group_name) --name $(terraform output -raw aks_cluster_name)
```

### Logs and Monitoring

```bash
# View AKS logs
az aks get-credentials --resource-group $(terraform output -raw resource_group_name) --name $(terraform output -raw aks_cluster_name)
kubectl logs -n kube-system deployment.apps/coredns

# Check VM logs
az vm run-command invoke --resource-group $(terraform output -raw resource_group_name) --name $(terraform output -raw vm_name) --command-id RunShellScript --scripts "tail -f /var/log/syslog"
```

## Upgrading

### Terraform Version Upgrades

```bash
# Update Terraform version in configuration
terraform version

# Update providers
terraform init -upgrade

# Plan with new version
terraform plan
```

### AKS Upgrades

```bash
# Check available versions
az aks get-upgrades --resource-group $(terraform output -raw resource_group_name) --name $(terraform output -raw aks_cluster_name)

# Upgrade cluster
az aks upgrade --resource-group $(terraform output -raw resource_group_name) --name $(terraform output -raw aks_cluster_name) --kubernetes-version 1.34.0
```

### Module Updates

```bash
# Update modules
terraform get -update

# Review changes
terraform plan
```

## Cleanup

### Destroy Resources

```bash
# Destroy all resources
terraform destroy

# Force destroy (if needed)
terraform destroy -auto-approve
```

### Verify Cleanup

```bash
# Check remaining resources
az resource list --resource-group $(terraform output -raw resource_group_name)

# Delete resource group (if needed)
az group delete --name $(terraform output -raw resource_group_name)
```

## Contributing

1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/enhanced-security
   ```
3. **Make your changes**
4. **Test thoroughly**
   ```bash
   terraform validate
   terraform plan
   ```
5. **Commit your changes**
   ```bash
   git commit -m 'Add enhanced security configurations'
   ```
6. **Push to the branch**
   ```bash
   git push origin feature/enhanced-security
   ```
7. **Open a Pull Request**

### Development Guidelines

- Follow Terraform best practices
- Use consistent naming conventions
- Include comprehensive documentation
- Test configurations in non-production environments
- Update variables and outputs as needed

## License

This project is licensed under the MIT License - see the [LICENSE](../LICENSE) file for details.

## Support

For support and questions:

- **Documentation**: Check this README and inline comments
- **Azure Documentation**: [AKS Documentation](https://docs.microsoft.com/en-us/azure/aks/)
- **Terraform Documentation**: [Terraform Azure Provider](https://registry.terraform.io/providers/hashicorp/azurerm/latest)
- **Issues**: Create an issue in the repository

---

**Note**: This infrastructure configuration creates billable Azure resources. Always review the Terraform plan before applying and monitor costs regularly. Use resource locks in production environments to prevent accidental deletions.
