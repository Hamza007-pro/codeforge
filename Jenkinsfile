pipeline {
    agent any

    stages {
        stage('Checkout Code') {
            steps {
                git url: 'https://github.com/Hamza007-pro/codeforge.git', branch: 'main'
            }
        }
        stage('Build Backend') {
            steps {
                sh 'docker-compose -f docker-compose.yml build backend'
            }
        }
        stage('Build Frontend') {
            steps {
                sh 'docker-compose -f docker-compose.yml build frontend'
            }
        }
        stage('Deploy') {
            steps {
                sh 'docker-compose -f docker-compose.yml up -d'
            }
        }
    }
}
