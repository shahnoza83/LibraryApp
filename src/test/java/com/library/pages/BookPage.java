package com.library.pages;

import com.library.utilities.BrowserUtils;
import com.library.utilities.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BookPage extends BasePage{

    @FindBy(xpath = "//table/tbody/tr")
    public List<WebElement> allRows;

    @FindBy(xpath = "//a[@class='btn btn-primary btn-sm']")
    public WebElement editBookButton;

    @FindBy(xpath = "//input[@type='search']")
    public WebElement search;

    @FindBy(id = "book_categories")
    public WebElement mainCategoryElement;

    @FindBy(name = "name")
    public WebElement bookName;

    @FindBy(xpath = "(//input[@type='text'])[4]")
    public WebElement author;

    @FindBy(name = "year")
    public WebElement year;

    @FindBy(name = "isbn")
    public WebElement isbn;

    @FindBy(id = "description")
    public WebElement description;

    @FindBy(xpath = "//div[@class='portlet-title']//a")
    public WebElement addBook;

    @FindBy(xpath = "//button[@type='submit']")
    public WebElement saveChanges;

    @FindBy(xpath = "//div[@class='toast-message']")
    public WebElement toastMessage;

    @FindBy(id = "book_group_id")
    public WebElement categoryDropdown;
    



    public WebElement editBook(String book) {
        String xpath = "//td[3][.='" + book + "']/../td/a";
        return Driver.getDriver().findElement(By.xpath(xpath));
    }

            /**
            * Searches for a book by name and retrieves its details.
            **/

    public Map<String, String> searchBookInUI(String bookName) {
        // Enter the book name in the search box
        search.clear();
        search.sendKeys(bookName);

        // Wait for the search results to update
        BrowserUtils.waitFor(2);

        // Locate the first row of the result
        WebElement firstRow = allRows.get(0);
        List<WebElement> cells = firstRow.findElements(By.tagName("td"));

        // Map the book details
        Map<String, String> bookDetails = new HashMap<>();
        bookDetails.put("isbn", cells.get(1).getText());
        bookDetails.put("name", cells.get(2).getText());
        bookDetails.put("author", cells.get(3).getText());
        bookDetails.put("category", cells.get(4).getText());
        bookDetails.put("year", cells.get(5).getText());
        //bookDetails.put("borrowed_by", cells.get(6).getText());

        return bookDetails;
    }

//    public String convertBookCategoryNameToId (String categoryBook) {
//
//
//        for (WebElement eachBookCategory : bookCategories) {
//            if (categoryBook.equalsIgnoreCase(eachBookCategory.getText())) {
//                return  eachBookCategory.getAttribute("value");
//            }
//        }
//
//        throw new RuntimeException("Category not found: " + categoryBook);
//    }


}
