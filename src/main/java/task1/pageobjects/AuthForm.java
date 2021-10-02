package task1.pageobjects;

import aquality.selenium.elements.interfaces.IButton;
import aquality.selenium.elements.interfaces.ITextBox;
import aquality.selenium.forms.Form;
import org.openqa.selenium.By;

public class AuthForm extends Form {
    ITextBox txbEmail = getElementFactory().getTextBox(By.xpath("//input[@data-at-selector='emailInput']"),"Email input");
    ITextBox txbPassword = getElementFactory().getTextBox(By.xpath("//input[@data-at-selector='passwordInput']"),"Password input");
    IButton btnSignIn = getElementFactory().getButton(By.xpath("//button[@data-at-selector='welcomeSignInBtn']"), "Sign in button");

    public AuthForm() {
        super(By.xpath("//form[@data-at-selector='signInContent']"), "Authorization form");
    }

    public void signIn(String email,String password){
        txbEmail.clearAndType(email);
        txbPassword.clearAndTypeSecret(password);
        btnSignIn.click();
    }
}
