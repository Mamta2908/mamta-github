package acceptance.step.ClientStepDef;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

import myBBC_Common.dynamoDB.IntegrationDbClient;
import myBBC_Common.sqs.IntegrationSqsClient;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.fail;

/**
 * Created by IntelliJ IDEA.
 * User: Vinod Kumar M
 * Date: 29/03/12
 * Time: 14:34
 * To change this template use File | Settings | File Templates.
 */
public class SharedDriver extends WebDriverException {

    private static WebDriver driver;
    private static ChromeDriverService _chrome;
    private static DesiredCapabilities  _capabilities;
    public static String directory = System.getProperty("user.dir");
    private static String drivers = directory + File.separator + "src" + File.separator + "test"
            + File.separator+"resources"+File.separator+"drivers";

    private static String phantomJsDriver = directory + File.separator +"src" + File.separator + "test"
            + File.separator + "resources"+File.separator+"drivers";

    private static String phantomJsDriver4Linux = directory + File.separator + "target"
            + File.separator + "phantomJs" + File.separator +"phantomjs-1.9.1-linux-x86_64"
            + File.separator + "bin";

    private static final String FIREFOX_LOCATION="C:\\Users\\chelln01\\AppData\\Local\\Mozilla Firefox\\firefox.exe";
    private static final String CHROME_LOCATION="C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe";

    private static String browserCapabilities = "Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0";

    String getSessionId;

    @Before("@WebDriver")
    public static void setUp() throws Exception {
        String browser  =  System.getProperty("browser");
        String key = browser;    //browser;
        System.out.println("The Browser used is: "+key);
        if (key.equalsIgnoreCase("chrome")){
            setChromeDriver();
        } else if (key.equalsIgnoreCase("firefox")){
            setFirefoxDriver();
        } else if(key.equalsIgnoreCase("phantomJs")){
            setPhantomJs();
        } else {
            setInternetExplorerDriver();
        }
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }


    @SuppressWarnings("finally")
    public static WebDriver getDriver() {


        if (driver == null) {
            throw new IllegalStateException("Selenium client is not initialised.");
        }
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }finally {
            return driver.switchTo().defaultContent();
        }

    }

    public static void setFirefoxDriver(){


        //_capabilities = DesiredCapabilities.firefox();
        FirefoxProfile _profile = new FirefoxProfile();
        File f =  new File(FIREFOX_LOCATION);
        FirefoxBinary _ffbinary = new FirefoxBinary(f);
        _profile.setPreference("network.proxy.type", 1);
        _profile.setPreference("network.proxy.http", "www-cache.reith.bbc.co.uk");
        _profile.setPreference("network.proxy.http_port", 80);
        _profile.setPreference("network.proxy.ssl", "www-cache.reith.bbc.co.uk");
        _profile.setPreference("network.proxy.ssl_port", 80);
        driver = new FirefoxDriver(_ffbinary,_profile);
    }
        
    /*public static void setChromeDriver(){

        String OSIAmIn = System.getProperty("os.name").toLowerCase();
        System.out.println("The OS i am using is :"+OSIAmIn);
             
        
        if (OSIAmIn.contains("windows")){
            _chrome = new ChromeDriverService.Builder()
                    .usingDriverExecutable(new File(drivers +File.separator+"win" + File.separator + "chromedriver.exe"))
                    .usingAnyFreePort()
                    .build();

            try {
                _chrome.start();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            _capabilities = DesiredCapabilities.chrome();
            _capabilities.setCapability("chrome.switches", Arrays.asList("--start-maximized"));
            Proxy proxy = new Proxy();
            proxy.setProxyType(Proxy.ProxyType.SYSTEM);
            driver = new RemoteWebDriver(_chrome.getUrl(), _capabilities);
           
            


            
         This is intentionally commented    
            
        }else if (OSIAmIn.contains("linux")){
            _chrome = new ChromeDriverService.Builder()
                    .usingDriverExecutable(new File(drivers +File.separator+"linux"))
                    .usingPort(9515)
                    .build();
            //.usingDriverExecutable(new File(drivers +File.separator+"linux" + File.separator + "chromedriver"))
        
            HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_17);
            driver.setJavascriptEnabled(true);
         This is intentionally commented 

        }


        //ChromeOptions options = new ChromeOptions();
    }

    public static kid setPhantomjsDriver(){
    	
    	caps = new DesiredCapabilities();
    
    	
    	
    }*/

    public static void setChromeDriver(){

        System.setProperty("webdriver.chrome.driver", drivers+File.separator+"chromedriver"+File.separator+"chromedriver.exe");
        ChromeOptions browser_setup = new ChromeOptions();
        browser_setup.setBinary(new File(CHROME_LOCATION));
        driver = new ChromeDriver(browser_setup);

    }


    public static void setPhantomJs(){

        String OSIAmIn = System.getProperty("os.name").toLowerCase();
        System.out.println("The OS i am using is :"+OSIAmIn);

        _capabilities = DesiredCapabilities.phantomjs();
        _capabilities.setJavascriptEnabled(true);
        _capabilities.setCapability("takesScreenshot", true);
        _capabilities.setCapability("phantomjs.page.settings.userAgent", browserCapabilities);

        if (OSIAmIn.contains("windows")) {
            _capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, drivers+File.separator+"phantomJs"+File.separator+"phantomjs.exe");
        } else {
            //_capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, phantomJsDriver4Linux + File.separator + "phantomjs");
        }


        try {
            driver = new PhantomJSDriver(_capabilities);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void setInternetExplorerDriver(){

        _capabilities = DesiredCapabilities.internetExplorer();
        _capabilities.setCapability("ignoreProtectedModeSettings", true);

        driver = new InternetExplorerDriver(_capabilities);
    }

    /*public static void setSafariDriver(){

        _capabilities = DesiredCapabilities.safari();
        driver = new SafariDriver(_capabilities);

    }*/


    private void saveSessionIdToSomeStorage(String session){

    }

    private String getPreviousSessionIdFromStorage(){
        return getSessionId;
    }



    @After("@WebDriver")
    public static void close(Scenario scenario)  {

    	/*StringBuffer verificationErrors = new StringBuffer();
        System.out.println("hello, I'm running closeBrowser");
        if (driver != null) {
            if (_capabilities.getBrowserName().equalsIgnoreCase("chrome")){
               if (scenario.isFailed()){
                    driver = new Augmenter().augment(driver);
                    try {
                        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                        scenario.embed(screenshot, "image/png");
                    } catch (WebDriverException somePlatformsDontSupportScreenshots) {
                        System.out.println("the screenshot embed failed !!");
                        System.err.println(somePlatformsDontSupportScreenshots.getMessage());
                    }
                }
                _chrome.stop();

            } else {
            	if (_capabilities.getBrowserName().equalsIgnoreCase("phantomJs")) {
			    if (scenario.isFailed()){
			    	System.out.println("Verifying i went here");


			    	try {

                    	System.out.println("I am entering here ");
                    	driver = new Augmenter().augment(driver);
                    	System.out.println("Did Error happen here ?? ");
                    	byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                        scenario.embed(screenshot, "image/png");

                    } catch (WebDriverException somePlatformsDontSupportScreenshots) {
                        System.out.println("the screenshot embed failed !!");
                        System.err.println(somePlatformsDontSupportScreenshots.getMessage());
                        driver.quit();

                }
            	}
                    driver.quit();
            }

            String verificationErrorString = verificationErrors.toString();
            if (!"".equals(verificationErrorString)) {
                fail(verificationErrorString);
            }
        }

    }
    */
        StringBuffer verificationErrors = new StringBuffer();
        if (scenario.isFailed()){
            try {

                System.out.println("I am entering here ");
                //driver = new Augmenter().augment(driver);
                System.out.println("Did Error happen here ?? ");
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.embed(screenshot, "image/png");

            } catch (WebDriverException somePlatformsDontSupportScreenshots) {
                System.out.println("the screenshot embed failed !!");
                System.err.println(somePlatformsDontSupportScreenshots.getMessage());


            }
        }
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
        driver.quit();


    }

}


