import java.io.File;
import java.time.Duration;
 
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
 
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
 
import io.github.bonigarcia.wdm.WebDriverManager;
 
public class CheckMultipleLoginScenarios {
	
	 WebDriver driver;
	 static ExtentReports extent;
	 ExtentTest test; 	
	 String validUsername = "student243@vrittechnologies.com";
	 String validPassword = "student243";
	 String invalidUsername = "abcd@gmail.com";
	 String invalidPassword = "abcc";
	
	
	 static {
	        extent = new ExtentReports();
	        ExtentSparkReporter spark = new ExtentSparkReporter(getExtentReportFilePath());
	        extent.attachReporter(spark);
	    }
	
		
	
	
	 @AfterSuite
	    public void tearDown() {
	        // Flush the reports after your test suite execution
	        extent.flush();
	    }
	
	
	  @DataProvider(name ="loginTestData")
	  
	  public  Object[][] loginFormTestData() {
	  
		  Object[][] obj = {
	  
	  // Test data for valid login
		  {"Valid",validUsername, validPassword},
	  
	  // Test data for negative testing - incorrect credentials
		  {"Invalid",invalidUsername, invalidPassword},
		  {"Invalid",invalidUsername, validPassword},
		  {"Invalid", validUsername, invalidPassword}
					
	  
	  };
	  return obj;
	  
	  }
	  
	  
	  
	  @Test(dataProvider="loginTestData")
		public void checkMultipleLoginScenarios(String type, String username, String password) throws InterruptedException {
			
 
			
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
			driver.get("https://cloud-frontend-topaz.vercel.app");
			String title = driver.getTitle();
			System.out.println(title);
			WebElement usernameField = driver.findElement(By.xpath("//input[@name='emailOrPhone']"));
			usernameField.sendKeys(username);
			WebElement passwordField = driver.findElement(By.xpath("//input[@name='password']"));
			passwordField.sendKeys(password);
			WebElement signInBtn = driver.findElement(By.xpath("//button[@type='submit']"));
			signInBtn.click();
			
			
			if(type.equals("Valid")) {
				
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(),'Total Applicants')]")));
				test= extent.createTest("Valid Login Case");
				test.log(Status.PASS, "Test Passed with successful Login");
			}else {
				
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@role='status']")));
				test= extent.createTest("Invalid Login Case");
				test.log(Status.PASS, "Test Passed with invalid Login");
			}
			
			Thread.sleep(2000);
			
			
			
			driver.quit();
			
		}
	  
	  
	  private static String getExtentReportFilePath() {
	        String reportFileName = "ExtentReport.html";
	        return new File("test-output", reportFileName).getAbsolutePath();
	    }
	  
	
 
}