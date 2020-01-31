package at.fhv.selenium;// Generated by Selenium IDE
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;
public class CmTest {
  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;
  @Before
  public void setUp() {
//      /usr/bin/google-chrome-stable
//      System.setProperty("webdriver.chrome.driver","C:\\Users\\Computer\\Desktop\\chromedriver.exe");
//      System.setProperty("webdriver.chrome.driver","/usr/bin/google-chrome-stable");
//      ChromeOptions options = new ChromeOptions();
//      options.addArguments("--headless");
//      options.addArguments("--no-sandbox");
//      options.addArguments("--disable-dev-shm-usage");
//      options.setBinary("/usr/bin/google-chrome-stable");
    driver = new FirefoxDriver();
    js = (JavascriptExecutor) driver;
    vars = new HashMap<String, Object>();
  }
  @After
  public void tearDown() {
    driver.quit();
  }
  @Test
  public void cm() {
    driver.get("http://localhost:8080/");
    driver.manage().window().setSize(new Dimension(1900, 1020));
    driver.findElement(By.cssSelector("#account-menu > span")).click();
    driver.findElement(By.id("login")).click();
    {
      WebElement element = driver.findElement(By.id("account-menu"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).perform();
    }
    driver.findElement(By.id("username")).sendKeys("admin");
    driver.findElement(By.id("password")).sendKeys("admin");
    driver.findElement(By.id("loginBtn")).click();
  }
}
