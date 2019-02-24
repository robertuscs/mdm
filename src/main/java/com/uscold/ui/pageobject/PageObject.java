package com.uscold.ui.pageobject;

/**
 * ***********************************************************************************************************************
 * Uscold.MDM
 * User: fwrmoral
 * 2/24/19
 * ***********************************************************************************************************************
 **/

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class PageObject {
    protected WebDriver driver;

//    public PageObject(WebDriver driver){
//        this.driver = driver;
//        PageFactory.initElements(driver, this);
//    }

    public PageObject(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
}