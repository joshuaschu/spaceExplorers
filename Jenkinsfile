pipeline {
  agent any
  env.Java_HOME="${tool 'openjdk-11'}"
  env.PATH="${env.Java_HOME}/bin:${env.PATH}"
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