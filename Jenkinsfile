pipeline {
  agent any
  tools {
          maven 'Maven'
          jdk 'openjdk-8'
      }
  stages {
    stage('Build') {
      steps {
        sh 'echo $JAVA_HOME'
        sh 'mvn clean install'
      }
    }
  }
}