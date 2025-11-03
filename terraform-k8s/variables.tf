variable "resource_group_name" {
  description = "Name of the Azure Resource Group"
  type        = string
  default     = "k8s-rg"
}

variable "location" {
  description = "Azure region for resources"
  type        = string
  default     = "East US"
}

variable "cluster_name" {
  description = "Name of the AKS cluster"
  type        = string
  default     = "my-aks-cluster"
}

variable "node_count" {
  description = "Number of nodes in the AKS cluster"
  type        = number
  default     = 1
}

variable "vm_size" {
  description = "VM size for AKS nodes"
  type        = string
  default     = "Standard_DS2_v2"
}

variable "kubernetes_version" {
  description = "Kubernetes version for AKS"
  type        = string
  default     = "1.33.0"
}

variable "vm_name" {
  description = "Name of the Azure Virtual Machine"
  type        = string
  default     = "ubuntu-vm"
}

variable "vm_size_standalone" {
  description = "VM size for the standalone Azure VM (free tier eligible)"
  type        = string
  default     = "Standard_B1s"
}

variable "admin_username" {
  description = "Admin username for the VM"
  type        = string
  default     = "azureuser"
}
