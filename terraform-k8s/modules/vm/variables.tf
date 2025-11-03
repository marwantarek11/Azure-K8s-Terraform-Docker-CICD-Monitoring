variable "vm_name" {
  description = "Name of the Azure Virtual Machine"
  type        = string
  default     = "ubuntu-vm"
}

variable "vm_size" {
  description = "VM size for the Azure VM (free tier eligible)"
  type        = string
  default     = "Standard_B1ls"
}

variable "vm_size_standalone" {
  description = "VM size for the standalone VM"
  type        = string
  default     = "Standard_F2s_v2"
}

variable "admin_username" {
  description = "Admin username for the VM"
  type        = string
  default     = "azureuser"
}

variable "location" {
  description = "Azure region for the VM"
  type        = string
}

variable "resource_group_name" {
  description = "Name of the Azure Resource Group"
  type        = string
}
