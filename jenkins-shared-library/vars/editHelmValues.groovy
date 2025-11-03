def call(String imageName, String tag) {
    sh "sed -i 's|repository:.*|repository: ${imageName}|g' helm-chart/values.yaml"
    sh "sed -i 's|tag:.*|tag: \"${tag}\"|g' helm-chart/values.yaml"
}
