package tests.util;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.log4testng.Logger;
import tests.BaseTest;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.uscold.ui.utils.TimeoutConstants.GET_ELEMENT_TIMEOUT;
import static com.uscold.ui.utils.TimeoutConstants.SHORT_TIMEOUT_MS;

public class Helper {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(BaseTest.class);
    private final static int maxWaitTimeMillisToBeUsedInChooseFunctions = 7000;

    static Workbook book;
    static Sheet sheet;
    public WebDriver driver;

    public static Object[][] getLabelMappingData(String testDataSheetPath, String sheetName) {

        FileInputStream file = null;
        try {

            file = new FileInputStream(testDataSheetPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {

            book = WorkbookFactory.create(file);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sheet = book.getSheet(sheetName);
        Object[][] data = new Object[sheet.getLastRowNum()][sheet.getRow(0).getLastCellNum()];
        // System.out.printIn(sheet.getLastRowNum() + "--------" +
        // sheet.getRow(0).getLastCellNum());
        for (int i = 0; i < sheet.getLastRowNum(); i++) {
            for (int k = 0; k < sheet.getRow(0).getLastCellNum();k++){
                data[i][k] = sheet.getRow(i + 1).getCell(k).toString();
                // System.out.printIn(data[i][k]);
            }
        }
        return data;
    }

    public static void click(WebElement el) {
        click(el, SHORT_TIMEOUT_MS);
    }
    static void click(WebElement el, int maxWaitTimeMillis) {
        long startedAt = System.currentTimeMillis();
        int step = 100;
        RuntimeException caughtEx = null;

        while (System.currentTimeMillis() - startedAt < maxWaitTimeMillis) {
            try {
                el.click();
                LOGGER.warn("was waiting for " + (System.currentTimeMillis() - startedAt) + " to click on " + el);
                return;
            } catch (WebDriverException ex) {
                caughtEx = ex;
            }
            try {
                Thread.sleep(step);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Waiting time is out for element to become clickable:" + el, caughtEx);
    }

    private static List<WebElement> clickOnDropDownLabel(WebDriver driver, String id, String textToFind) {
        WebDriverWait wait = new WebDriverWait(driver, GET_ELEMENT_TIMEOUT);
        WebElement accTypeContainer = driver.findElement(By.id(id));
        wait.until(ExpectedConditions.not(ExpectedConditions.attributeContains(By.id(id), "class", "chosen-disabled")));
        wait.until(ExpectedConditions.not(ExpectedConditions.attributeContains(By.id(id), "class", "chosen-disabled")));
        WebElement aElem = accTypeContainer.findElement(By.cssSelector("a.chosen-single"));
        //scrollTo(driver,aElem);
        //click(aElem);
        aElem.click();
        //accTypeContainer.findElement(By.xpath("//table[@id='list']//div[contains(@class, 'chosen-drop')]/div[contains(@class, 'chosen-search')]/input")).sendKeys(textToFind);
        return accTypeContainer.findElements(By.xpath(".//div[@class='chosen-drop']//ul[@class='chosen-results']/li"));
    }

    public static void chooseModule(WebDriver driver, String moduleName) {
        try {
            WebElement searchModule = driver.findElement(By.id("searchText"));
            scrollTo(driver, searchModule);
            click(searchModule);
            searchModule.clear();
            searchModule.sendKeys(moduleName);
            click(driver.findElement(By.linkText(moduleName)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void chooseWarehouse(WebDriver driver, final String substring) {

        WebElement whSelect = driver.findElement(By.id("globalWarehouseSelect_chosen"));
        whSelect.findElement(By.xpath(".//a[@class='chosen-single']")).click();
        List<WebElement> warehouses = whSelect.findElements(By.xpath(".//div[@class='chosen-drop']//ul[@class='chosen-results']/li"));
        try {
            WebElement dropDownElem = warehouses.stream().filter(wh -> wh.getText().contains(substring)).findFirst().get();
            click(dropDownElem, maxWaitTimeMillisToBeUsedInChooseFunctions);
//            if (isElementPresent(driver, By.id("warehouseOk")))
//                click(driver.findElement(By.id("warehouseOk")), maxWaitTimeMillisToBeUsedInChooseFunctions);
        } catch (NoSuchElementException e) {
            System.out.println("The whse could not be clicked!");
            e.printStackTrace();
        }

    }

    static void chooseCustomer(WebDriver driver, final String substring) {
        try {
            WebElement custSelect = driver.findElement(By.id("globalCustomerSelect_chosen"));
            click(custSelect.findElement(By.xpath(".//a[@class='chosen-single']")));
            List<WebElement> customers = custSelect.findElements(By.xpath(".//div[@class='chosen-drop']//ul[@class='chosen-results']/li"));
            WebElement chosenCustomerOption = customers.stream().filter(wh -> wh.getText().contains(substring)).findFirst().get();
            click(chosenCustomerOption, maxWaitTimeMillisToBeUsedInChooseFunctions);
            if (isElementPresent(driver, By.id("customerOk")))
                click(driver.findElement(By.id("customerOk")), maxWaitTimeMillisToBeUsedInChooseFunctions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isElementDisplayedOnPage(WebDriver driver, By by) {
        try {
            driver.findElement(by).isDisplayed();
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public static boolean isElementPresent(WebDriver driver, By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public static void chooseWarehouse(WebDriver driver, int number) {
        chooseWarehouse(driver, String.valueOf(number));
    }

    public static void chooseCustomer(WebDriver driver, int number) {
        chooseCustomer(driver, String.valueOf(number));
    }

    public static void maximizeWindow(WebDriver driver) {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("mac")) {
            java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            org.openqa.selenium.Point position = new org.openqa.selenium.Point(0, 0);
            driver.manage().window().setPosition(position);
            org.openqa.selenium.Dimension maximizedScreenSize =
                    new org.openqa.selenium.Dimension((int) screenSize.getWidth(), (int) screenSize.getHeight());
            driver.manage().window().setSize(maximizedScreenSize);
        }
        driver.manage().window().maximize();

    }

    public static boolean isAlertPresent(WebDriver driver) {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    public static void scrollTo(WebDriver driver, WebElement elem) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elem);
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    public static WebElement chooseValueFromStandardDropDownByTextMatch(WebDriver driver, String id, String value) throws InterruptedException {
        List<WebElement> values = clickOnDropDownLabel(driver, id, value);
        return values.stream().filter(el -> el.getAttribute("innerText").trim().equalsIgnoreCase(value)).findFirst().get();
    }

    public static WebElement chooseValueFromStandardDropDownBySubstring(WebDriver driver, String id, String substring) throws InterruptedException {
        List<WebElement> values = clickOnDropDownLabel(driver, id, substring);
        return values.stream().filter(el -> el.getAttribute("innerText").trim().toLowerCase().contains(substring.toLowerCase())).findFirst().get();
    }

    protected boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }
}
