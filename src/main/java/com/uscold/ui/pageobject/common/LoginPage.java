package com.uscold.ui.pageobject.common;

import com.uscold.ui.pageobject.PageObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends PageObject {

    @FindBy(name = "j_username")
    private WebElement username;

    @FindBy(name="j_password")
    private WebElement password;

    @FindBy(name="btn_login")
    private WebElement loginButton;



    public void enterUsername(String user){
        this.username.clear();
        this.username.sendKeys(user);
    }

    public void enterPassword(String pass){
        this.password.clear();
        this.password.sendKeys(pass);
    }

    public void loginButton(){
        this.loginButton.click();
    }

    public LoginPage(WebDriver driver) {
        super(driver);
    }
}
