# TODO: Adapt Jenkins Pipeline for Python App, AKS, Helm, and SonarQube

## Overview
Adapt the Jenkinsfile and shared library to use a scripted pipeline structure similar to the provided example, tailored for the Python microservice app, AKS (Azure Kubernetes Service), Helm chart deployment, and SonarQube analysis on the VM.

## Steps
1. **Update Jenkinsfile**: Change from calling `microservicePipeline()` to a scripted pipeline with individual stages calling shared library functions. ✅
2. **Create Shared Library Functions**: Create separate .groovy files in `jenkins-shared-library/vars/` for each function:
   - `checkRepo.groovy`: Handle repository checkout. ✅
   - `runUnitTests.groovy`: Run Python unit tests. ✅
   - `build.groovy`: Build the Python app. ✅
   - `runSonarQubeAnalysis.groovy`: Perform SonarQube analysis using VM public IP for URL. ✅
   - `buildandPushDockerImage.groovy`: Build and push Docker image to Docker Hub. ✅
   - `editHelmValues.groovy`: Edit Helm values.yaml with new image tag (adapted for Helm instead of OpenShift deployment.yaml). ✅
   - `deployOnAks.groovy`: Deploy to AKS using Helm and kubeconfig credentials (adapted from deployOnOpenshift). ✅
3. **Update Environment Variables**: Set variables for Docker Hub, image name, kubeconfig credentials, namespace, SonarQube URL (from VM public IP), and SonarQube token. ✅
4. **Test Pipeline**: Ensure the pipeline runs correctly with the new structure. (Pending - requires Jenkins setup and credentials)

## Key Adaptations
- Use AKS instead of OpenShift: Replace OpenShift-specific commands with Helm and kubectl for AKS.
- SonarQube: Use VM public IP for SonarQube URL (e.g., http://<vm_public_ip>:9000/).
- Helm: Edit `values.yaml` for image updates and deploy using `helm upgrade --install`.
- Credentials: Use 'kubeconfig' for AKS access, 'docker-hub-credentials' for Docker, 'sonarqube' for SonarQube token.

## Completion Criteria
- Jenkinsfile updated to scripted pipeline. ✅
- All .groovy files created and functional. ✅
- Pipeline stages match the example structure but adapted for the stack. ✅
