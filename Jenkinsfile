pipeline {
    agent any

    environment {
        // Example of environment variables you can define for your pipeline
        DOCKER_COMPOSE_FILE = 'docker-compose.yml'
    }

    stages {
        stage('Checkout Code') {
            steps {
                script {
                    echo "Checking out code from repository..."
                }
                git url: 'https://github.com/Hamza007-pro/codeforge.git', branch: 'main'
            }
        }

        stage('Build Backend') {
            steps {
                script {
                    echo "Building the backend using Docker Compose..."
                }
                sh '''
                    docker-compose -f $DOCKER_COMPOSE_FILE build --no-cache backend
                '''
            }
        }

        stage('Build Frontend') {
            steps {
                script {
                    echo "Building the frontend using Docker Compose..."
                }
                sh '''
                    docker-compose -f $DOCKER_COMPOSE_FILE build --no-cache frontend
                '''
            }
        }

        stage('Deploy') {
            steps {
                script {
                    echo "Deploying containers using Docker Compose..."
                }
                sh '''
                    docker-compose -f $DOCKER_COMPOSE_FILE up -d
                '''
            }
        }
    }

    post {
        success {
            echo "Pipeline executed successfully! All services are up and running."
        }
        failure {
            echo "Pipeline failed. Please check the error logs for details."
            cleanUp()
        }
    }

    // Clean-up stage to remove containers and images if needed
    cleanup {
        script {
            echo "Cleaning up any remaining Docker containers..."
            sh '''
                docker-compose -f $DOCKER_COMPOSE_FILE down
            '''
        }
    }
}
