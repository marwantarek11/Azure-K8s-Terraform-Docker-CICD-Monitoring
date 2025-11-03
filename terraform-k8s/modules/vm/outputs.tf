output "vm_public_ip" {
  description = "Public IP address of the VM"
  value       = azurerm_public_ip.pip.ip_address
}

output "vm_private_ip" {
  description = "Private IP address of the VM"
  value       = azurerm_network_interface.nic.private_ip_address
}

output "vm_id" {
  description = "ID of the VM"
  value       = azurerm_linux_virtual_machine.vm.id
}

output "vm_name" {
  description = "Name of the VM"
  value       = azurerm_linux_virtual_machine.vm.name
}

output "ssh_private_key" {
  description = "Private SSH key for connecting to the VM"
  value       = tls_private_key.ssh.private_key_pem
  sensitive   = true
}

output "ssh_public_key" {
  description = "Public SSH key for the VM"
  value       = tls_private_key.ssh.public_key_openssh
}
