pipeline {
    agent any

    tools {
        jdk 'jdk-21'
        maven 'maven1'
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/your-repo/Billing-software-E2E.git' // replace with your actual repo
            }
        }

        stage('Prepare Metadata') {
            steps {
                // Create Allure environment files
                writeFile file: 'Billing-software-E2E/target/allure-results/environment.properties', text: '''
OS=Windows 11 Home
Java.Version=21
Browser=Chrome 124
Browser.Version=124.0.6367.60
Tester=Kasha
Build.Version=1.0.0
Environment=QA
                '''.stripIndent()

                writeFile file: 'Billing-software-E2E/target/allure-results/executor.json', text: """
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
                dir('Billing-software-E2E') {
                    bat 'mvn clean test'
                }
            }
        }

        stage('Allure Report') {
            steps {
                allure includeProperties: false,
                       jdk: '',
                       results: [[path: 'Billing-software-E2E/target/allure-results']]
            }
        }
    }

    post {
        always {
            junit '**/target/surefire-reports/*.xml'
        }
    }
}