package com.uscold.ui.pageobject.customer;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CustomerManagementPage {

    private static WebElement elemnent = null;


        public void navigateToCustomerManagementPage (WebDriver driver){
        driver.get(System.getProperty("ewm.url")+"/customer/searchRecords/?showAdvSearchPannel=false");
    }

    public WebElement clickOnCreateNewButton(WebDriver driver) {
        elemnent = driver.findElement(By.id("createNewBtn"));
        return elemnent;
    }
}
