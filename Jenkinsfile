pipeline {
  agent any
  tools {
          maven 'Maven 3.3.9'
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