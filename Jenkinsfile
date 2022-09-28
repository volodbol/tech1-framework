pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr:'5'))
    }
    triggers {
        pollSCM('H */1 * * *')
    }
    tools {
        jdk 'Java 11'
        maven 'Maven 3.6.3'
    }
    stages {
        stage('dev build') {
            when {
                branch 'dev'
            }
            steps {
                sh 'mvn clean install'
            }
        }
        stage('dev sonar') {
            when {
                branch 'dev'
            }
            steps {
                script {
                    def scannerHome = tool 'Tech1 SonarQube Scanner';
                    withSonarQubeEnv('Tech1 SonarQube') {
                        sh "${scannerHome}/bin/sonar-scanner";
                    }
                }
            }
        }
    }
    post {
        always {
            script {
                // https://issues.jenkins-ci.org/browse/JENKINS-46325, https://jenkins.io/doc/book/pipeline/jenkinsfile
                if (currentBuild.result == null) {
                    currentBuild.result = 'SUCCESS'
                }
            }
            alwaysNotifications()
        }
    }
}

def alwaysNotifications() {
    emailext (
        to: "$TECH1_CEO, $TECH1_CTO",
        subject: '${DEFAULT_SUBJECT}',
        body: '''${SCRIPT, template="pipeline-changes.template"}'''
    )
}
