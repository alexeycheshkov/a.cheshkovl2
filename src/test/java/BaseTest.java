import aquality.selenium.browser.AqualityServices;
import aquality.selenium.browser.Browser;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import task1.utils.FileUtils;

import java.util.Iterator;

public abstract class BaseTest {
    protected Browser browser;

    @Parameters("test_url")
    @BeforeMethod
    public void setUp(String testUrl){
        browser = AqualityServices.getBrowser();
        browser.maximize();
        browser.goTo(testUrl);
        browser.waitForPageToLoad();
    }

    @AfterMethod
    public void tearDown(){
        browser.quit();
    }

    @DataProvider(name = "provider")
    public Iterator<Object[]> dataProvider(ITestContext context){
        return FileUtils.parseExelData(context.getCurrentXmlTest().getParameter("test_data_path")).iterator();
    }
}
