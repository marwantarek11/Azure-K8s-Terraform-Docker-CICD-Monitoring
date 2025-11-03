def call(String credentialsId, String namespace, String chartPath) {
    withCredentials([file(credentialsId: credentialsId, variable: 'KUBECONFIG')]) {
        sh "helm upgrade --install monitoring ${chartPath} --namespace ${namespace} --kubeconfig \$KUBECONFIG"
    }
}
