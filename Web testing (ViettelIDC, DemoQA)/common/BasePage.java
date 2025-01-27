package lession12.common;


import lession14.common.DriverManager;
import lession14.common.LogType;
import lession14.report.ExtentReportManager;
import lession14.report.ExtentTestManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.Objects;

public class BasePage {

    private static WebDriver mWebDriver;
    private static WebDriverWait mWebDriverWait;
    private JavascriptExecutor javascriptExecutor;


    protected BasePage(WebDriver driver) {
        mWebDriver = Objects.isNull(driver) ? DriverManager.getWebDriver() : driver;
        mWebDriverWait = new WebDriverWait(mWebDriver, Duration.ofSeconds(30));
    }

    public BasePage() {
    }

    /**
     * Create a web wait driver
     *
     * @param secondTimeOuts : timeout
     * @return Object WebWaitDriver
     */
    public static WebDriverWait getWebWaitDriver(long... secondTimeOuts) {
        Duration timeOut = secondTimeOuts.length > 0 ? Duration.ofSeconds(secondTimeOuts[0]) : Duration.ofSeconds(10);
        mWebDriverWait = new WebDriverWait(mWebDriver, timeOut);
        return mWebDriverWait;
    }

    /**
     * Create a javascript executor
     */
    public JavascriptExecutor getJavascriptExecutor() {
        return Objects.nonNull(javascriptExecutor) ? javascriptExecutor : (JavascriptExecutor) mWebDriver;
    }

    /**
     * Input the text to web element
     *
     * @param webObject
     * @param value
     */
    public void inputTextTo(Object webObject, String value, String title) {
        WebElement element = findElement(webObject);

        element.clear();
        element.sendKeys(value);

        // Add Report
        addReportInfo(LogType.INFO, MessageFormat.format("Input text {0} to {1}", value, title));
    }

    /**
     * Click to element
     *
     * @param webObject
     */
    public static void clickElement(Object webObject, String title) {
        WebElement element = findElement(webObject);

        getWebWaitDriver().until(ExpectedConditions.elementToBeClickable(element));
        element.click();

        // Add report
        addReportInfo(LogType.INFO, MessageFormat.format("Clicked  {0}", title));
    }

    /**
     * Find the web element by object
     * If WebElement Object : Return
     * If By Object : Wait and finding via Selenium
     *
     * @param webObject : Object
     * @return : WebElement or Null
     */
    public static WebElement findElement(Object webObject) {
        // Find - WebElement Object -> return
        if (webObject instanceof WebElement) return (WebElement) webObject;

        // Find - By object -> Wait and return a WebElement
        if (webObject instanceof By) {
            return getWebWaitDriver().until(ExpectedConditions.visibilityOfElementLocated((By) webObject));
        }
        return null;
    }

    /**
     * Add more information for Report: Including Extent
     *
     * You can add more report at this function.
     *
     * @param extMsg
     */
    public static void addReportInfo(LogType logType, String extMsg) {
        // Add for Extent Report
        if (ExtentTestManager.getExtentTest() != null) {
            if (logType.equals(LogType.INFO)) ExtentReportManager.info(extMsg);
            else ExtentReportManager.pass(extMsg);
        }
    }

    /* Assert true */
    public static void assertTrue(boolean isResult, String msg) {
        if (isResult) {
            ExtentReportManager.pass(String.format("%s -> PASS", msg));
        } else ExtentReportManager.fail(String.format("%s -> FAIL", msg));
    }

    /* Assert fail */
    public void assertFail(boolean isResult, String msg) {
        if (!isResult) {
            ExtentReportManager.pass(String.format("%s -> PASS", msg));
        } else ExtentReportManager.fail(String.format("%s -> FAIL", msg));
    }

    /* Assert Equals */
    public void assertEqual(Object actual, Object expected, String msg) {
        boolean isResult = Objects.equals(actual, expected);
        String message = String.format("%s\nVerify Object: Actual: %s Expected: %s", msg, actual, expected);
        if (isResult) {
            ExtentReportManager.pass(message);
        } else ExtentReportManager.fail(message);
    }
}
