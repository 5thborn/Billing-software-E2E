pipeline {
    agent any

    tools {
        jdk 'JDK21'
        maven 'maven1'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/5thborn/Billing-software-E2E.git'
            }
        }

        stage('Prepare Metadata') {
            steps {
                // Ensure directories exist - note the removed Billing-software-E2E directory
                bat '''
                if not exist "target\\allure-results" mkdir "target\\allure-results"
                '''
                
                // Create Allure environment files - note the removed Billing-software-E2E directory
                writeFile file: 'target/allure-results/environment.properties', text: '''
OS=Windows 11 Home
Java.Version=21
Browser=Chrome 124
Browser.Version=124.0.6367.60
Tester=Kasha
Build.Version=1.0.0
Environment=QA
                '''.stripIndent()

                writeFile file: 'target/allure-results/executor.json', text: """
{
  "buildName": "${env.JOB_NAME} #${env.BUILD_NUMBER}",
  "buildOrder": "${env.BUILD_NUMBER}",
  "reportName": "Allure Report",
  "name": "Jenkins",
  "buildUrl": "${env.BUILD_URL}",
  "reportUrl": "${env.BUILD_URL}allure",
  "type": "jenkins",
  "url": "http://localhost:8080/"
}
"""
            }
        }

        stage('Build & Test') {
            steps {
                // No dir() wrapper needed - we're already in the project root
                bat 'mvn clean test'
            }
        }

        stage('Allure Report') {
            steps {
                allure includeProperties: false,
                       jdk: '',
                       results: [[path: 'target/allure-results']] // note the removed Billing-software-E2E directory
            }
        }
    }

    post {
        always {
            // Try a more general pattern for JUnit results
            junit '**/target/surefire-reports/TEST-*.xml'
        }
    }
}