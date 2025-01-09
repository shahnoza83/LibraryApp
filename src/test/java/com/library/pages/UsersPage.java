package com.library.pages;

import com.library.utilities.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class UsersPage extends BasePage{

    @FindBy(xpath = "(//tbody//a[@role='button'])[1]")
    public WebElement editUser;

    @FindBy(id = "status")
    public WebElement statusDropdown;

    @FindBy(name = "email")
    public WebElement email;

    @FindBy(xpath = " //button[@type=\"submit\"]")
    public WebElement saveChanges;

    @FindBy(css = " .toast-message")
    public WebElement toastMessage;

    @FindBy(id = "user_status")
    public WebElement userStatusDropdown;

    @FindBy(css = ".dataTables_info")
    private WebElement userCount;

    @FindBy(xpath = "//select[@name='tbl_users_length']\n")
    private WebElement NumberOfUserDropdown;

    @FindBy(xpath = " //input[@type='search']")
    public WebElement searchField;

    public WebElement editUser(String email) {
        String xpath = "//td[.='"+ email+ "']/..//a";
        return Driver.getDriver().findElement(By.xpath(xpath));
    }
    
}
