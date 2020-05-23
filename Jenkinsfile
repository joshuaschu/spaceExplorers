pipeline {
  agent any
  tools {
          maven 'Maven'
          jdk 'openjdk-11'
      }
   node('vagrant-slave') {
          env.JAVA_HOME="${tool 'openjdk-11'}"
          env.PATH="${env.JAVA_HOME}/bin:${env.PATH}"
          sh 'java -version'
    }
  stages {
    stage('Build') {
      steps {
        sh 'mvn clean install'
      }
    }
  }
}