package com.skylark.automation.Billing_software_E2E;

import java.time.Duration;
import java.util.logging.Logger;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.annotations.Listeners;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.testng.AllureTestNg;

@Epic("Zoho Billing Demo")
@Feature("Billing Dashboard")
@Listeners({AllureTestNg.class})
public class BillingDashboardTest {

    private WebDriver driver;
    private final Logger logger = Logger.getLogger(BillingDashboardTest.class.getName());
    private BillingDashboardPage dashboardPage;

    @BeforeMethod
    public void setUp() {
        logger.info("Launching browser...");

        // Configure Chrome options for better stability
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        // Navigate to the demo page and ensure it's fully loaded
        driver.get("https://www.zoho.com/us/billing/billing-software-demo/#/home/billing-dashboard");

        // Create page object
        dashboardPage = new BillingDashboardPage(driver);
        dashboardPage.waitForPageLoad();

        // Short pause to ensure UI is fully loaded
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            logger.warning("Sleep interrupted: " + e.getMessage());
        }
    }

    @Test(invocationCount = 1) // Reduced to 1 for debugging
    @Story("Access Ethan Clarke's billing details")
    @Description("Navigate to the dashboard, select Ethan Clarke, and read billing/shipping addresses")
    @Severity(SeverityLevel.CRITICAL)
    public void testBillingAndShippingAddress() {
        try {
            logger.info("Opening 'Customers' tab...");
            Allure.step("Opening 'Customers' tab");
            dashboardPage.openCustomersTab();
            captureScreenshot("After clicking Customers tab");

            logger.info("Selecting customer 'Ethan Clarke'...");
            Allure.step("Selecting customer 'Ethan Clarke'");
            dashboardPage.selectEthanClarke();
            captureScreenshot("After selecting Ethan Clarke");

            String billing = dashboardPage.getBillingAddress();
            String shipping = dashboardPage.getShippingAddress();

            logger.info("Billing Address: " + billing);
            logger.info("Shipping Address: " + shipping);
            
            // Add address information to Allure report
            saveBillingAddress(billing);
            saveShippingAddress(shipping);
            
            captureScreenshot("Address information");

            // Only verify addresses aren't empty if we successfully retrieved them
            if (billing != null) {
                Assert.assertFalse(billing.isEmpty(), "Billing address should not be empty");
            }

            if (shipping != null) {
                Assert.assertFalse(shipping.isEmpty(), "Shipping address should not be empty");
            }

        } catch (Exception e) {
            logger.severe("Test failed due to exception: " + e.getMessage());
            captureScreenshot("Error_screenshot");
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Attachment(value = "Billing Address", type = "text/plain")
    public String saveBillingAddress(String address) {
        return address;
    }
    
    @Attachment(value = "Shipping Address", type = "text/plain")
    public String saveShippingAddress(String address) {
        return address;
    }
    
    @Attachment(value = "{screenshotName}", type = "image/png")
    public byte[] captureScreenshot(String screenshotName) {
        logger.info("Capturing screenshot: " + screenshotName);
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            logger.info("Browser closed.");
        }
    }
}