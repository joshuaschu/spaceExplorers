pipeline {
  agent any
  tools {
          maven 'Maven'
          jdk 'openjdk-11'
      }
   node {
     jdk = tool name: 'openjdk-11'
     env.JAVA_HOME = "${jdk}"

     echo "jdk installation path is: ${jdk}"

     // next 2 are equivalents
     sh "${jdk}/bin/java -version"

     // note that simple quote strings are not evaluated by Groovy
     // substitution is done by shell script using environment
     sh '$JAVA_HOME/bin/java -version'
   }
  stages {
    stage('Build') {
      steps {
        sh 'mvn clean install'
      }
    }
  }
}