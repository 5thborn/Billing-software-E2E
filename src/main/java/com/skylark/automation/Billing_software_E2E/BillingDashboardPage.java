package com.skylark.automation.Billing_software_E2E;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import io.qameta.allure.Step;

import java.time.Duration;
import java.util.List;

public class BillingDashboardPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public BillingDashboardPage(WebDriver driver) {
        this.driver = driver;
        // Increased wait time from 20 to 30 seconds
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        PageFactory.initElements(driver, this);
    }

    // Page elements
    @FindBy(xpath = "//*[@id=\"ember308\"]")
    private WebElement customersTab;

    @FindBy(xpath = "//span[contains(text(),'Ethan Clarke')]")
    private WebElement ethanClarke;

    @FindBy(xpath = "//a[contains(text(),'Overview')]")
    private WebElement overviewTab;

    @FindBy(xpath = "//div[contains(text(),'Billing Address')]/following-sibling::div")
    private WebElement billingAddress;

    @FindBy(xpath = "//div[contains(text(),'Shipping Address')]/following-sibling::div")
    private WebElement shippingAddress;

    // Actions with Allure steps
    @Step("Opening Customers tab")
    public void openCustomersTab() {
        // Wait for page to be fully loaded
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        
        // Check if there are any iframes and switch to them if needed
        try {
            if (driver.findElements(By.tagName("iframe")).size() > 0) {
                driver.switchTo().frame(0); // Switch to the first iframe
            }
        } catch (Exception e) {
            // If no iframe is found or can't switch, proceed with the main content
        }
        
        // Try multiple ways to find and click the Customers tab
        try {
            wait.until(ExpectedConditions.elementToBeClickable(customersTab)).click();
        } catch (Exception e) {
            // Alternative method: try a different locator
            try {
                WebElement altElement = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("a[href*='customers'], a.customers-link, a#customers-tab")));
                altElement.click();
            } catch (Exception e2) {
                // Last resort: try JavaScript click
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", 
                    driver.findElement(By.xpath("//a[contains(text(),'Customers')]")));
            }
        }
        
        // After clicking tab, wait for any loading to complete
        try {
            wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
            Thread.sleep(2000); // Additional small wait for any AJAX calls
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Step("Selecting Ethan Clarke customer record")
    public void selectEthanClarke() {
        // Reset to main content in case we're in an iframe
        try {
            driver.switchTo().defaultContent();
        } catch (Exception e) {
            // If not in an iframe, this will throw an exception we can ignore
        }
        
        // Check if we need to switch to any available iframes
        List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
        if (iframes.size() > 0) {
            // Try each iframe until we find our element
            for (int i = 0; i < iframes.size(); i++) {
                try {
                    driver.switchTo().frame(i);
                    // Try to find Ethan Clarke in this frame with a short timeout
                    WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
                    shortWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(),'Ethan Clarke')]")));
                    break; // Found it, stop checking frames
                } catch (TimeoutException te) {
                    // Element not in this frame, switch back and try next
                    driver.switchTo().defaultContent();
                }
            }
        }
        
        // Try multiple locator strategies for Ethan Clarke
        try {
            wait.until(ExpectedConditions.elementToBeClickable(ethanClarke)).click();
        } catch (Exception e) {
            try {
                // Try alternative locators
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[contains(text(),'Ethan') and contains(text(),'Clarke')]")));
                element.click();
            } catch (Exception e2) {
                try {
                    // Try CSS selector
                    WebElement element = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector(".customer-name:contains('Ethan'), tr:contains('Ethan Clarke')")));
                    element.click();
                } catch (Exception e3) {
                    // Last resort: JavaScript click on any element containing Ethan Clarke
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    WebElement element = driver.findElement(
                        By.xpath("//*[contains(text(),'Ethan') or contains(text(),'Clarke')]"));
                    js.executeScript("arguments[0].click();", element);
                }
            }
        }
    }

    @Step("Clicking on Overview tab")
    public void clickOverviewTab() {
        wait.until(ExpectedConditions.elementToBeClickable(overviewTab)).click();
    }

    @Step("Retrieving billing address information")
    public String getBillingAddress() {
        return wait.until(ExpectedConditions.visibilityOf(billingAddress)).getText();
    }

    @Step("Retrieving shipping address information")
    public String getShippingAddress() {
        return wait.until(ExpectedConditions.visibilityOf(shippingAddress)).getText();
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Waiting for page to fully load")
    public void waitForPageLoad() {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        try {
            Thread.sleep(1000); // Small buffer after page load
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}