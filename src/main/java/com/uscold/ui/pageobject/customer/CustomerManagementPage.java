package com.uscold.ui.pageobject.customer;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * ***********************************************************************************************************************
 * Uscold.MDM
 * User: fwrmoral
 * 2/24/19
 * ***********************************************************************************************************************
 **/

public class CustomerManagementPage {

//    private String CUSTOMER_PAGE_URL = System.getProperty("ewm.url")+"/product/productSearch";
    private static WebElement elemnent = null;


        public void navigateToCustomerManagementPage (WebDriver driver){
        driver.get(System.getProperty("ewm.url")+"/customer/searchRecords/?showAdvSearchPannel=false");
    }

    public WebElement clickOnCreateNewButton(WebDriver driver) {
        elemnent = driver.findElement(By.id("createNewBtn"));
        return elemnent;
    }
}
