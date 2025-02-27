pipeline {
  agent any
  stages {
    stage('checkout') {
      steps {
        git(url: 'https://github.com/bottle-note/bottle-note-admin-server', branch: 'dev')
      }
    }

  }
}