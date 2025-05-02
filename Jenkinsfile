pipeline {
    agent any
    
    triggers {
        // Run tests every day at 5 PM
        cron('0 17 * * *')
    }
    
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
                bat '''
                if not exist "target\\allure-results" mkdir "target\\allure-results"
                '''
                
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
                bat 'mvn clean test'
            }
        }
        
        stage('Allure Report') {
            steps {
                allure includeProperties: false,
                       jdk: '',
                       results: [[path: 'target/allure-results']]
            }
        }
        
        stage('Deploy to Stage') {
            when {
                branch 'main'
                expression { currentBuild.result == null || currentBuild.result == 'SUCCESS' }
            }
            steps {
                bat '''
                echo Deploying to staging environment...
                # Add your deployment commands here
                '''
            }
        }
        
        stage('Deploy to Production') {
            when {
                branch 'main'
                expression { currentBuild.result == null || currentBuild.result == 'SUCCESS' }
            }
            input message: 'Deploy to production?', ok: 'Deploy'
            steps {
                bat '''
                echo Deploying to production environment...
                # Add production deployment commands here
                '''
            }
        }
    }
    
    post {
        always {
            junit '**/target/surefire-reports/TEST-*.xml'
        }
        success {
            emailext (
                subject: "SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                body: """<p>SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
                         <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
                to: 'Kashan5@verizon.net'
            )
        }
        failure {
            emailext (
                subject: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                body: """<p>FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
                         <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
                to: 'Kashan5@verizon.net'
            )
        }
    }
}