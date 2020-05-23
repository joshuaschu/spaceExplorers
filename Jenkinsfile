pipeline {
  agent any
  tools {
          maven 'Maven'
          jdk 'openjdk-11'
      }
  stages {
    stage('Build') {
      steps {
        sh 'mvn clean install'
      }
    }
  }
}