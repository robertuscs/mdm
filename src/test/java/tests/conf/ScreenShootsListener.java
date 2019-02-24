package tests.conf;


import com.google.common.io.Files;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.log4testng.Logger;
import tests.BaseTest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
/**
 * ***********************************************************************************************************************
 * Uscold.MDM
 * User: fwrmoral
 * 2/24/19
 * ***********************************************************************************************************************
 **/

public class ScreenShootsListener implements ITestListener {
    String outputPath = null;

    String filePath = "screens/";
    File outputFile = null;
    String screensDir = null;
    private static final Logger logger = Logger.getLogger(ScreenShootsListener.class);

    @Override
    public void onTestFailure(ITestResult result) {
        String methodName = result.getName().toString().trim();

        if (result.getInstance() instanceof BaseTest) {
            BaseTest testClass = (BaseTest) result.getInstance();
            WebDriver driver = testClass.getDriver();
            String screenName = testClass.getClass().getSimpleName() + ":" + methodName;
            takeScreenShot(driver, screenName);
        }
    }


    public void takeScreenShot(WebDriver driver, String screenName) {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

        File targetScreen = new File(screensDir, timeStamp + ".png");
        try {
            Files.copy(scrFile, targetScreen);
        } catch (IOException e) {
            logger.error(e);
            e.printStackTrace();
        }

        String path = String.format("<img src=\"screens/%s\">", targetScreen.getName());
        Reporter.log(path);
    }

    @Override
    public void onTestStart(ITestResult result) {

    }

    @Override
    public void onTestSuccess(ITestResult result) {

    }

    @Override
    public void onTestSkipped(ITestResult result) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public void onStart(ITestContext context) {
        outputFile = new File(context.getOutputDirectory());
        logger.warn("Going to put screen in: " + outputFile.getParentFile().getAbsolutePath());
        //File targetDir = new File(outputFile.getParentFile(), "target");
        File screensDirFile = new File(outputFile.getParentFile(), "screens");
        screensDirFile.mkdir();
        try {
            screensDir = screensDirFile.getCanonicalPath();
        } catch (IOException e) {
            logger.error(e);
            e.printStackTrace();
        }


    }

    @Override
    public void onFinish(ITestContext context) {

    }
}

