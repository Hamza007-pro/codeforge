# CodeForge Project

CodeForge is a comprehensive tool designed for generating mobile application code using design patterns. This repository includes a **Spring Boot backend** and a **Next.js frontend**. The project uses **Docker Compose** for containerized deployment and a **Jenkins pipeline** for automated CI/CD.

## Table of Contents

- [Project Overview](#project-overview)
- [Technologies Used](#technologies-used)
- [Setup Instructions](#setup-instructions)
  - [Prerequisites](#prerequisites)
  - [Running Locally](#running-locally)
  - [Running with Docker Compose](#running-with-docker-compose)
- [Jenkins CI/CD Pipeline](#jenkins-cicd-pipeline)
- [File Structure](#file-structure)
- [Environment Variables](#environment-variables)
- [Contributing](#contributing)
- [License](#license)
- [Videp](#video)
---

## Project Overview

CodeForge is designed to simplify the development of mobile applications by automating the implementation of design patterns. It integrates seamlessly with backend services via REST APIs and provides an intuitive frontend interface for interaction.

The key features include:
- Automated code generation based on MVC/MVVM patterns.
- Support for Android and iOS development.
- Backend implemented in **Spring Boot**.
- Frontend built with **Next.js**.

---

## Technologies Used

- **Frontend**: Next.js, React, Node.js
- **Backend**: Spring Boot, Java
- **Containerization**: Docker, Docker Compose
- **CI/CD**: Jenkins

---

## Setup Instructions

### Prerequisites

Ensure the following are installed on your system:
- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- [Node.js](https://nodejs.org/)
- [Java JDK 17+](https://adoptopenjdk.net/)
- [Maven](https://maven.apache.org/)

### Running Locally

1. **Backend**:
   ```bash
   cd codeforge-backend
   mvn clean install
   java -jar target/app.jar
   ```
   The backend will run on `http://localhost:8080`.

2. **Frontend**:
   ```bash
   cd codeforge-client
   npm install
   npm run dev
   ```
   The frontend will run on `http://localhost:3000`.

### Running with Docker Compose

1. Build and start the containers:
   ```bash
   docker-compose up --build
   ```
2. Access the services:
   - Frontend: `http://localhost:3000`
   - Backend: `http://localhost:8080`

---

## Jenkins CI/CD Pipeline

The Jenkins pipeline automates the build and deployment process. Below is an overview of the pipeline:

1. **Checkout Code**:
   Pulls the latest code from the repository.

2. **Build Backend**:
   Builds the Spring Boot backend using the `Dockerfile` and `docker-compose.yml`.

3. **Build Frontend**:
   Builds the Next.js frontend using the `Dockerfile` and `docker-compose.yml`.

4. **Deploy**:
   Starts the containers using `docker-compose up`.

### Pipeline Script
```groovy
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
```

---

## File Structure

```plaintext
.
├── codeforge-backend
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
├── codeforge-client
│   ├── Dockerfile
│   ├── package.json
│   └── pages/
├── docker-compose.yml
└── Jenkinsfile
```

---

## Environment Variables

### Backend
- `SPRING_PROFILES_ACTIVE`: Profile for Spring Boot (e.g., `dev`).

### Frontend
- `NEXT_PUBLIC_API_URL`: API URL for backend communication (e.g., `http://backend:8080`).
- `HOST`: Server host for Next.js (default: `0.0.0.0`).
- `NODE_ENV`: Environment mode (e.g., `development`).

---

## Contributing

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Commit your changes (`git commit -m 'Add feature'`).
4. Push to the branch (`git push origin feature-branch`).
5. Open a Pull Request.

---

## License

This project is licensed under the [MIT License](LICENSE).

---
## Video
https://github.com/user-attachments/assets/eb925b82-7648-4dcd-8f27-a08fd86b4b4d

Feel free to reach out for any queries or contributions!
```

This `README.md` provides a comprehensive guide on how to set up and use your project. It covers all the essential details like technologies used, setup instructions, the Jenkins pipeline, and environment variables. You can adjust any specific sections as needed for your project.
