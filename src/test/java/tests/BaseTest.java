package tests;

/**
 * ***********************************************************************************************************************
 * Uscold.MDM
 * User: fwrmoral
 * 2/24/19
 * ***********************************************************************************************************************
 **/

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.common.io.Files;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;
import com.uscold.ui.pageobject.common.LoginPage;
import org.openqa.selenium.Cookie;

import com.relevantcodes.extentreports.ExtentTest;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.openqa.selenium.WebDriver;
import tests.util.WebDriverFactory;

import static com.uscold.ui.utils.TimeoutConstants.GET_ELEMENT_TIMEOUT;
import static jdk.nashorn.internal.runtime.JSType.toBoolean;


public class BaseTest extends PageFactory {

    private static final boolean IS_HEADLESS = toBoolean(System.getProperty("ui.headless"));
    private static Set<Cookie> cookies = new HashSet<>();
    private final static int maxWaitTimeMillisToBeUsedInChooseFunctions = 7000;
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(BaseTest.class);
    public static String EWM_URL = System.getProperty("ewm.url")+"/home.htm";
    public static String TEST_DATA = System.getProperty("user.dir") + "/src/test/resources/testdata";
    public static final String FAILED_TEST_SCREENSHOTS = System.getProperty("user.dir") + "/reports/extends/FailedTestsScreenshots/";
    public static final String REPORT_CONFIG = System.getProperty("user.dir") + "/reports/extendreportsconfig.xml";
    public static final String EXTEND_REPORT = System.getProperty("user.dir") + "/reports/extends/ExtentReport.html";
    protected static WebDriver driver;
    protected WebDriverWait wait;
    public ExtentReports extent;
    public ExtentTest extentTest;
    public WebDriver getDriver() {
        return driver;
    }

    @BeforeSuite
    public void beforeSuite() {
        LOGGER.info("EWM_URL:" + EWM_URL);
        LOGGER.info("HEADLESS:" + IS_HEADLESS);

        driver = initDriver();
        wait = new WebDriverWait(driver, GET_ELEMENT_TIMEOUT);
        driver.manage().timeouts().implicitlyWait(GET_ELEMENT_TIMEOUT, TimeUnit.SECONDS);
        loginWithCookies(driver, EWM_URL);
    }

    @BeforeClass
    public void beforeClassInit() {
        extent = new ExtentReports(EXTEND_REPORT, true);
        extent.loadConfig(new File(REPORT_CONFIG));
        LOGGER.info("Test is starting: " + getClass().getSimpleName());
        LOGGER.info(String.format("starting test %s", this.getClass().getSimpleName()));
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        extentTest = extent.startTest((this.getClass().getSimpleName()), //method.getName()+
                "Test Desc: "+method.getAnnotation(Test.class).description());
        LOGGER.info("Scenario is starting: " + method.getName());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) throws IOException {
        if (result.getStatus() == ITestResult.FAILURE) {
            extentTest.log(LogStatus.FAIL, "TEST CASE FAILED IS " + result.getName()); //to add name in extent report
            extentTest.log(LogStatus.FAIL, "TEST CASE FAILED IS " + result.getThrowable()); //to add error/exception in extent report
            String screenshotPath = getScreenshot(driver, result.getName());
            extentTest.log(LogStatus.FAIL, extentTest.addScreenCapture(screenshotPath)); //to add screenshot in extent report
            //extentTest.log(LogStatus.FAIL, extentTest.addScreencast(screenshotPath)); //to add screencast/video in extent report
        } else if (result.getStatus() == ITestResult.SKIP) {
            extentTest.log(LogStatus.SKIP, "Test Case SKIPPED IS " + result.getName());
            //logger.log(LogStatus.SKIP, this.getClass().getSimpleName() + " Test Case Skipped");
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            extentTest.log(LogStatus.PASS, "Test Case PASSED IS " + result.getName());
            //logger.log(LogStatus.PASS, this.getClass().getSimpleName() + " Test Case Success and Title Verified");
            //String screenshotPath = getScreenshot(driver, result.getName());
            //extentTest.log(LogStatus.PASS, extentTest.addScreenCapture(screenshotPath)); //to add screenshot in extent report
            //extentTest.log(LogStatus.PASS, extentTest.addScreencast(screenshotPath)); //to add screencast/video in extent report
        }
        extent.endTest(extentTest); //ending test and ends the current test and prepare to create html report
        extent.flush();
    }

    @AfterTest(alwaysRun = true)
    public void endReport() {
        extent.close();
    }


    @AfterClass(alwaysRun = true)
    public void afterTestconfig(final ITestContext testContext) {
        driver.quit();
        LOGGER.info(String.format("Stopping test %s", this.getClass().getSimpleName()));
    }

    public static void login(WebDriver driver, String url) {
        driver.get(url);
        if (driver.getCurrentUrl().endsWith("login.html")) {
            LoginPage loginPage = new LoginPage(driver);
            loginPage.enterUsername(System.getProperty("user.username"));
            // This method is to set Password in the password text box
            loginPage.enterPassword(System.getProperty("user.password"));
            // This method is to click on Login Button
            loginPage.loginButton();
        }
    }

    public static synchronized void loginWithCookies(WebDriver driver, String url) {
        if (cookies.isEmpty()) {
            try {
                login(driver, url);
                cookies = driver.manage().getCookies();
            } catch (Exception ex) {
                LOGGER.error(ex);
            }
        } else {
            driver.get(url);
            driver.manage().deleteAllCookies();
            cookies.forEach(cookie -> driver.manage().addCookie(cookie));
            driver.get(url);
        }
    }

    private WebDriver initDriver() {
        driver = WebDriverFactory.getDriver(getOptions(IS_HEADLESS));
        return driver;
    }

    private ChromeOptions getOptions(boolean isHeadless) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-infobars");
        if (isHeadless) {
            chromeOptions.addArguments("--headless");
            chromeOptions.addArguments("--test-type");
            chromeOptions.addArguments("--disable-gpu");
            chromeOptions.addArguments("--no-first-run");
            chromeOptions.addArguments("--no-default-browser-check");
            chromeOptions.addArguments("--ignore-certificate-errors");
            chromeOptions.addArguments("--start-maximized");
            chromeOptions.addArguments("--disable-infobars");
        }
        return chromeOptions;
    }

    public static String getScreenshot(WebDriver driver, String screenshotName) throws IOException {
        String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        // after execution, you could see a folder "FailedTestsScreenshots"
        // under src folder
        String destination = FAILED_TEST_SCREENSHOTS + screenshotName + dateName + ".png";
        File finalDestination = new File(destination);
        Files.copy(source, finalDestination);
        return destination;
    }
}