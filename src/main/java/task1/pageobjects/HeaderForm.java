package task1.pageobjects;

import aquality.selenium.elements.interfaces.IButton;
import aquality.selenium.forms.Form;
import org.openqa.selenium.By;

public class HeaderForm extends Form {
    IButton btnDownloads = getElementFactory().getButton(By.xpath("//a[@data-at-menu='Downloads']"),"Downloads button");

    public HeaderForm(){
        super(By.id("site-header"),"Header form");
    }

    public void clickDownloadsBtn(){
        btnDownloads.click();
    }
}
