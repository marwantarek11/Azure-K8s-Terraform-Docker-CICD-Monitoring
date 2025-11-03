output "resource_group_name" {
  description = "Name of the created resource group"
  value       = module.resource_group.name
}

output "aks_cluster_name" {
  description = "Name of the AKS cluster"
  value       = module.aks.cluster_name
}

output "aks_cluster_id" {
  description = "ID of the AKS cluster"
  value       = module.aks.cluster_id
}

output "kube_config" {
  description = "Kubernetes configuration"
  value       = module.aks.kube_config
  sensitive   = true
}

output "vm_public_ip" {
  description = "Public IP address of the Ubuntu VM"
  value       = module.vm.vm_public_ip
}

output "vm_private_ip" {
  description = "Private IP address of the Ubuntu VM"
  value       = module.vm.vm_private_ip
}

output "vm_name" {
  description = "Name of the Ubuntu VM"
  value       = module.vm.vm_name
}

output "vm_ssh_private_key" {
  description = "Private SSH key for connecting to the VM"
  value       = module.vm.ssh_private_key
  sensitive   = true
}
