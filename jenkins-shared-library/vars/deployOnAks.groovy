def call(String credentialsId, String namespace, String chartPath) {
    withCredentials([file(credentialsId: credentialsId, variable: 'KUBECONFIG')]) {
        sh "helm upgrade --install my-microservice ${chartPath} --namespace ${namespace} --kubeconfig \$KUBECONFIG"
    }
}
