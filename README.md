# Multimodule Kafka Application with Gradle Build

This repository hosts a multimodule application designed to work with Kafka. The application is built using Gradle and is organized into several distinct modules for enhanced modularity and easy scalability.

## 📂 Project Structure:

- **`application`**: Contains the main class and configuration files necessary for the startup and operation of the application. It houses the `main` class and the `application.yaml` file.
- **`api`**: This module is responsible for receiving and processing RESTful requests.
- **`handler`**: It encapsulates the logic for handling various events within the application.
- **`kafka`**: Dedicated to interfacing with Kafka. It includes Kafka producers and Kafka consumers, providing seamless integration with the Kafka ecosystem.
- **`buildSrc`**: Helps maintain project conventions and aims to reduce the number of dependencies in the package. It ensures a standardized way of managing and organizing the build logic.


## 🛠️ Build:

Before deploying the application, it's essential to build the project using Gradle, as the generated JAR file will be copied into the Docker container.

To build the project:

```bash
./gradlew clean build
```

## 🚀 Deployment:

The application is containerized and can be easily deployed using Docker. All necessary configurations are provided in the `docker-compose.yml` file located in the `docker-compose` directory.

To get the application up and running:

1. Navigate to the `docker-compose` directory.
2. Run the following command:
   ```bash
   docker-compose up -d
