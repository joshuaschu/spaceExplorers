pipeline {
  agent any
  tools {
          maven 'Maven'
          jdk 'openjdk-11'
      }
  stages {
    stage('prep'){
        env.JAVA_HOME="${tool 'openjdk-11'}"
        env.PATH="${env.JAVA_HOME}/bin:${env.PATH}"
        sh 'java -version'
    }
    stage('Build') {
      steps {
        sh 'mvn clean install'
      }
    }
  }
}