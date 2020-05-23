pipeline {
  agent any
  tools {
          maven 'Maven'
          jdk 'openjdk-11'
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