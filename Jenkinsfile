pipeline {
    environment {
        repository = 'gcu-profit-dev.kr-central-2.kcr.dev/arcadia-nextjs/springboot-login'
        DOCKERHUB_CREDENTIALS = credentials('kicToken')
        dockerImage = ''
        gitlaburl = 'http://172.16.212.109/kakaoprofit/Arcadia_BE'
        gitlabbranch = 'main'
        githuburl = 'https://github.com/kakaoProFit/arcadia-manifest'
        githubbranch = 'main'
        SLACK_CHANNEL = '#jenkins-alert'
        SLACK_CREDENTIALS = credentials('slack_alert_token')
        COMMIT_MESSAGE = ''
    }

    agent any 	// 사용 가능한 에이전트에서 이 파이프라인 또는 해당 단계를 실행
    stages {
        stage('Prepare') {
            steps {
                script {
                    git branch: "$gitlabbranch", credentialsId: 'gitlabToken', url: "$gitlaburl"
                    COMMIT_MESSAGE = sh(script: 'git log -1 --pretty=%B', returnStdout: true).trim()
                    slackSend (
                        channel: SLACK_CHANNEL,
                        color: '#FFFF00',
                        message: "SpringBoot Build Started: ${env.JOB_NAME} - ${env.BUILD_NUMBER}\nCommit Message: ${COMMIT_MESSAGE}"
                    )
                }
            }
        }

        stage('Gradlew Build') {
            steps {
                script {
                    try {
                        sh 'chmod +x gradlew'
                        sh './gradlew clean build'
                    } catch (Exception e) {
                        slackSend (
                            channel: SLACK_CHANNEL,
                            color: 'danger',
                            message: "Gradlew Build Failed: ${env.JOB_NAME} - ${env.BUILD_NUMBER}"
                        )
                        throw e
                    }
                }
            }
        }

        stage('Docker build') {
            steps {
                script {
                    try {
                        dockerImage = docker.build repository + ":$BUILD_NUMBER"
                        sh 'docker image tag $repository:$BUILD_NUMBER $repository:latest'
                    } catch (Exception e) {
                        slackSend (
                            channel: SLACK_CHANNEL,
                            color: 'danger',
                            message: "Docker Build Failed: ${env.JOB_NAME} - ${env.BUILD_NUMBER}"
                        )
                        throw e
                    }
                    slackSend (
                        channel: SLACK_CHANNEL,
                        color: 'good',
                        message: "Docker Build Success: ${env.JOB_NAME} - ${env.BUILD_NUMBER}"
                    )
                }
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

        stage('Checkout_to_github_manifest') {
            steps {
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
    post {
        success {
            slackSend (
                channel: SLACK_CHANNEL,
                color: 'good',
                message: "Build Successful: ${env.JOB_NAME} - ${env.BUILD_NUMBER}"
            )
        }
        
        failure {
            slackSend (
                channel: SLACK_CHANNEL,
                color: 'danger',
                message: "Build Failed: ${env.JOB_NAME} - ${env.BUILD_NUMBER}"
            )
        }
    }
}
