# Billing-software-E2E

This project is an end-to-end automation framework for validating the **Zoho Billing Dashboard** using **Selenium WebDriver**, **TestNG**, **Allure Reports**, and **Jenkins**.

---

## 🔧 Tech Stack

- Java 17+
- Selenium WebDriver
- TestNG
- Maven
- Allure Reporting
- Jenkins (CI/CD)
- Git/GitHub

---

## 🚀 How to Run Locally

### Prerequisites

- Java JDK 21 (configured in `JAVA_HOME`)
- Apache Maven
- Chrome Browser
- Allure CLI (`allure --version` to confirm)
- Git

### Steps

```bash
git clone https://github.com/YOUR_USERNAME/Billing-software-E2E.git
cd Billing-software-E2E
mvn clean test

Generate Allure report:
allure serve target/allure-results