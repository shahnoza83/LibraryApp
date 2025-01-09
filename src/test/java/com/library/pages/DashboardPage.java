package com.library.pages;

import com.library.utilities.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DashboardPage extends BasePage{

    @FindBy(id = "borrowed_books")
    public WebElement borrowedBooksNumber;

    @FindBy(xpath = "//a[@id='navbarDropdown']")
    public WebElement usersName;

    @FindBy(id = "book_count")
    public WebElement booksNumber;





    public String getModuleCount(String module){
        //h6[normalize-space(.)='Users']//..//h2

        String locator="//h6[normalize-space(.)='"+module+"']//..//h2";

        WebElement elementOfModule = Driver.getDriver().findElement(By.xpath(locator));

        return elementOfModule.getText();
    }


    public String getDisplayedUserName() {
        return Driver.getDriver().findElement(By.xpath("//div[@class='dropdown-menu dropdown-menu-right']//a")).getText();
    }
}
