#!/usr/bin/env python3
import subprocess
import json
import os

def get_terraform_output():
    """Get Terraform outputs for VM IP and SSH key."""
    try:
        # Change to terraform directory
        terraform_dir = os.path.join(os.path.dirname(__file__), '..', 'terraform-k8s')

        # Run terraform output command
        result = subprocess.run(
            ['terraform', 'output', '-json'],
            cwd=terraform_dir,
            capture_output=True,
            text=True,
            check=True
        )

        outputs = json.loads(result.stdout)
        return outputs
    except subprocess.CalledProcessError as e:
        print(f"Error running terraform output: {e}")
        return None
    except json.JSONDecodeError as e:
        print(f"Error parsing terraform output: {e}")
        return None

def generate_inventory(outputs):
    """Generate Ansible inventory.ini from Terraform outputs."""
    if not outputs:
        return

    vm_public_ip = outputs.get('vm_public_ip', {}).get('value')
    vm_ssh_private_key = outputs.get('vm_ssh_private_key', {}).get('value')

    if not vm_public_ip:
        print("VM public IP not found in Terraform outputs")
        return

    # Generate inventory content
    inventory_content = f"""[ci_cd_servers]
ci-cd-vm ansible_host={vm_public_ip} ansible_user=azureuser ansible_ssh_private_key_file=~/.ssh/id_rsa

[all:vars]
ansible_python_interpreter=/usr/bin/python3
"""

    # Write to inventory.ini
    inventory_path = os.path.join(os.path.dirname(__file__), 'inventory.ini')
    with open(inventory_path, 'w') as f:
        f.write(inventory_content)

    print(f"Generated inventory.ini with VM IP: {vm_public_ip}")

if __name__ == "__main__":
    outputs = get_terraform_output()
    if outputs:
        generate_inventory(outputs)
    else:
        print("Failed to get Terraform outputs")
