package at.fhv.cucumber.stepdefs;

import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import at.fhv.web.rest.UserResource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

public class LoginStepDefs extends StepDefs {

    @Autowired
    private UserResource userResource;

    private MockMvc restUserMockMvc;
    WebDriver dr;

    @Before
    public void setup() {
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource).build();
        WebDriver driver = new FirefoxDriver();
    }

    @When("I login as user {username} and password {password}")
    public void login(String username,String password){
        dr.findElement(By.xpath("//*[@id='username']")).sendKeys(username);
        dr.findElement(By.xpath("//*[@id='password']")).sendKeys(password);
        dr.findElement(By.xpath("//*[@id='loginBtn']")).click();
        dr.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    }
    @Then("the user is found")
    public void the_user_is_found() throws Throwable {
        actions
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }
    @Then("the home page opens")
    public void verifySuccessful(){
        String expectedText="You are now logged in as user \"admin\".";
        String actualText=         dr.findElement(By.xpath("//*[@id='home-logged-message']")).getText();
        Assert.assertTrue("Login not successful",expectedText.equals(actualText));
    }

}
