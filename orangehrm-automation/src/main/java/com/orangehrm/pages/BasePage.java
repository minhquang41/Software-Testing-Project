package com.orangehrm.pages;

import com.orangehrm.utils.ConfigReader;
import com.orangehrm.utils.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage - Lớp cha cho tất cả Page Object
 * Chứa các method dùng chung: click, type, wait, v.v.
 */
public abstract class BasePage {

    protected static final Logger logger = LogManager.getLogger(BasePage.class);
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected ConfigReader config;

    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.config = ConfigReader.getInstance();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitWait()));
        PageFactory.initElements(driver, this);
    }

    // ==================== WAIT METHODS ====================

    protected WebElement waitForVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected WebElement waitForClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected boolean waitForInvisible(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    protected WebElement waitForPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    // ==================== ACTION METHODS ====================

    protected void click(WebElement element) {
        try {
            waitForClickable(element).click();
            logger.debug("✅ Click vào: {}", getElementDescription(element));
        } catch (ElementClickInterceptedException e) {
            logger.warn("⚠️ Click bị chặn, thử JavascriptExecutor...");
            jsClick(element);
        }
    }

    protected void jsClick(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }

    protected void type(WebElement element, String text) {
        waitForVisible(element);
        element.clear();
        element.sendKeys(text);
        logger.debug("✅ Nhập '{}' vào field", text);
    }

    protected void clearAndType(WebElement element, String text) {
        waitForVisible(element);
        element.clear();
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(text);
    }

    protected String getText(WebElement element) {
        waitForVisible(element);
        return element.getText().trim();
    }

    protected String getAttribute(WebElement element, String attribute) {
        waitForVisible(element);
        return element.getAttribute(attribute);
    }

    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    protected void selectByVisibleText(WebElement element, String text) {
        Select select = new Select(waitForVisible(element));
        select.selectByVisibleText(text);
        logger.debug("✅ Chọn option: '{}'", text);
    }

    protected void selectByValue(WebElement element, String value) {
        Select select = new Select(waitForVisible(element));
        select.selectByValue(value);
    }

    protected void scrollToElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    protected void hoverOver(WebElement element) {
        org.openqa.selenium.interactions.Actions actions =
            new org.openqa.selenium.interactions.Actions(driver);
        actions.moveToElement(element).perform();
    }

    // ==================== NAVIGATION ====================

    public void navigateTo(String url) {
        driver.get(url);
        logger.info("🌐 Điều hướng đến: {}", url);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    // ==================== HELPER ====================

    private String getElementDescription(WebElement element) {
        try {
            String tag = element.getTagName();
            String text = element.getText();
            return tag + (text.isEmpty() ? "" : "[" + text + "]");
        } catch (Exception e) {
            return "element";
        }
    }

    /**
     * Chờ loading spinner biến mất
     */
    protected void waitForLoadingSpinner() {
        By spinner = By.cssSelector(".oxd-loading-spinner");
        try {
            waitForInvisible(spinner);
        } catch (TimeoutException e) {
            logger.debug("Spinner đã tắt hoặc không xuất hiện");
        }
    }
}
