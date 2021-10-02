import aquality.selenium.core.logging.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import task1.pageobjects.AuthForm;
import task1.pageobjects.DownloadForm;
import task1.pageobjects.HeaderForm;
import task1.utils.MailUtils;
import java.util.regex.Pattern;

public class KasperskyTest extends BaseTest{
    private AuthForm authForm;
    private HeaderForm headerForm;
    private DownloadForm downloadForm;
    private String regexpExpectedUrl = "https://my.kaspersky.com/.*Download";

    @BeforeMethod
    public void setUp() {
        authForm = new AuthForm();
        headerForm = new HeaderForm();
        downloadForm = new DownloadForm();
    }

    @Test
    @Parameters({"email","password","os","product"})
    public void getDownloadUrlTest(String email, String password, String os,String product){
        Logger.getInstance().info("Authorization in test web site");
        authForm.signIn(email,password);
        Logger.getInstance().info("Going to Downloads tab");
        headerForm.clickDownloadsBtn();
        Logger.getInstance().info("Choosing operation system: "+os);
        downloadForm.clickOsBtn(os);
        Logger.getInstance().info("Pressing download for "+product+" product");
        downloadForm.clickDownloadBtn(product);
        Assert.assertTrue(downloadForm.isOpenedDownloadDialog(), "Download dialog is not opened");
        Logger.getInstance().info("Choosing send link for download by email");
        downloadForm.clickOtherDownloadsBtn();
        downloadForm.clickSendToMeBtn();
        Assert.assertTrue(downloadForm.isOpenedInstallerSendSelfDialog(), "Installer send self dialog is not opened");
        Assert.assertEquals(downloadForm.getCurrentEmailForSend(),email, "Current email for send doesn't match with user's email");
        Logger.getInstance().info("Press 'Send' and checking email for new message with product's link");
        downloadForm.clickInstallerSendBtn();
        String emailText = MailUtils.getTextFromLatestMessage();
        Assert.assertTrue(emailText.contains(product), "Email text doesn't contain name of product");
        Assert.assertTrue(Pattern.compile(regexpExpectedUrl).matcher(emailText).find(), "Email text doesn't contain URL with 'Download' word");
    }
}
