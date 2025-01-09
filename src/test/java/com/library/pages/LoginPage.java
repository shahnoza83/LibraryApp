package com.library.pages;

import com.library.utilities.BrowserUtils;
import com.library.utilities.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage{

    public LoginPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(id = "inputEmail")
    public WebElement emailBox;

    @FindBy(id = "inputPassword")
    public WebElement passwordBox;

    @FindBy(tagName = "button")
    public WebElement loginButton;





    public boolean login(String email, String password){

        emailBox.sendKeys(email);
        passwordBox.sendKeys(password);
        loginButton.click();
        return false;
    }

    public void login(String userType){


        String username = System.getenv(userType+"_username");
        String password = System.getenv(userType+"_password");

        emailBox.sendKeys(username);
        passwordBox.sendKeys(password);
        BrowserUtils.waitFor(2);
        loginButton.click();
        BrowserUtils.waitFor(2);

    }




}
