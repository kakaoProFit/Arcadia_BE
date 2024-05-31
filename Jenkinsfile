pipeline {
    environment{
        repository = 'gcu-profit-dev.kr-central-2.kcr.dev/arcadia-nextjs/springboot-login'
        DOCKERHUB_CREDENTIALS = credentials('kicToken')
        gitlaburl = 'http://172.16.212.109/kakaoprofit/Arcadia_BE'
        dockerImage = ''
        gitlabbranch = 'main'
        githuburl = 'https://github.com/kakaoProFit/arcadia-manifest'
        githubbranch = 'main'

    }
    agent any 	// 사용 가능한 에이전트에서 이 파이프라인 또는 해당 단계를 실행
    stages {
        stage('Prepare'){
            steps{
                git branch: "$gitlabbranch", credentialsId: 'gitlabToken', url: "$gitlaburl"
            }
        }
        stage('Gradlew  Build') {
            steps {
                // 	"빌드" 단계와 관련된 몇 가지 단계를 수행
                sh 'chmod +x gradlew'
                sh  './gradlew clean build'
            }
        }
        stage('Docker build') {
            steps {
                script {
                    dockerImage = docker.build repository + ":$BUILD_NUMBER"
                }
                sh 'docker image tag $repository:$BUILD_NUMBER $repository:latest'
            }
        }
        stage('Login') {
            steps {
                sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login gcu-profit-dev.kr-central-2.kcr.dev -u $DOCKERHUB_CREDENTIALS_USR --password-stdin' // docker hub 로그인
            }
        }
        stage('Deliver image') {
            steps {
                script {
                    sh 'docker push $repository:$BUILD_NUMBER' //docker push
                    sh 'docker push $repository:latest'
                }
            }
        }
        stage('Checkout_to_github_manifest'){
            steps{
                git branch: "${githubbranch}", credentialsId: 'githubToken', url: "${githuburl}"
            }
        }
        stage('Update rollout.yaml') {
            steps {
                script {
                    def rolloutFilePath = "${WORKSPACE}/arcadia-api/springLogin-rollout.yaml"
                    def newImageTag = "image: " + "$repository" + ":$BUILD_NUMBER"

                    // Read the file
                    def fileContent = readFile(rolloutFilePath)

                    // Modify the line with the new image tag
                    def modifiedContent = fileContent.replaceAll("image:.*", "${newImageTag}")

                    // Write the modified content back to the file
                    writeFile file: rolloutFilePath, text: modifiedContent

                    // GitHub 저장소에 변경사항 커밋 및 푸시
                    withCredentials([usernamePassword(credentialsId: 'githubToken', passwordVariable: 'GITHUB_PSW', usernameVariable: 'GITHUB_USR')]){
                        sh """
                        git config user.name 'mango0422'
                        git config user.email 'tom990422@gmail.com'
                        git add ${rolloutFilePath}
                        git commit -m 'Update image version in rollout.yaml'
                        git push https://${GITHUB_USR}:${GITHUB_PSW}@github.com/kakaoProFit/arcadia-manifest.git ${githubbranch}
                    """
                    }
                }
            }
        }
    }
}