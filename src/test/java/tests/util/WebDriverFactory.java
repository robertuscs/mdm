package tests.util;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static com.uscold.ui.utils.TestLogger.LOGGER;


/**
 * ***********************************************************************************************************************
 * Uscold.MDM
 * User: fwrmoral
 * 2/24/19
 * ***********************************************************************************************************************
 **/

public class WebDriverFactory {

    /*public static synchronized WebDriver getDriver() {
        return getDriver(new ChromeOptions());
    }*/

    public static WebDriver getDriver(ChromeOptions chromeOptions) {
        WebDriver driver;
        driver = new ChromeDriver(chromeOptions);
        configureCommonParams(driver);
        return driver;
    }


    private static void configureCommonParams(WebDriver driver) {
        driver.manage().window().setSize(new Dimension(1920, 1080));
        driver.manage().window().fullscreen();
        Dimension windowDimensions = driver.manage().window().getSize();
        LOGGER.info(String.format("Browser window dimensions are %d x %d", windowDimensions.getWidth(), windowDimensions.getHeight()));
    }
}
