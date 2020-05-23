pipeline {
  agent any
  tools {
          maven 'Maven'
          jdk 'openjdk-11'
      }
      environment {
                  JAVA_HOME = "${tool 'openjdk-11'}"
                  PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
       }
  stages {
    stage('Build') {
      steps {
        sh 'mvn clean install'
      }
    }
  }
}