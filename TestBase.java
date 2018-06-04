package Base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import Utilities.DbManager;
import Utilities.ExcelReader;
import Utilities.MonitoringMail;

public class TestBase 
{
	public static WebDriver driver;
	public static TopNavigation topnav;
	public static Properties Or=new Properties();
	public static Properties Config=new Properties();
	public static FileInputStream fis;
	public static Logger log = Logger.getLogger("devpinoyLogger");
	public static MonitoringMail mail =new MonitoringMail();
	public static WebDriverWait wait;
	public static WebElement dropdown;
	public static ExcelReader excel=new ExcelReader("D:\\PageObjectModel\\src\\test\\resources\\Excel\\Testng.xlsx");
	
	public TestBase()
	{
		if(driver==null)
		{
			try 
			{
				fis=new FileInputStream(System.getProperty("user.dir")+"\\src\\test\\resources\\Properties\\Config.properties");
			}  catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Config.load(fis);
				log.debug("Config properties loaded!!!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				fis=new FileInputStream(System.getProperty("user.dir")+"\\src\\test\\resources\\Properties\\OR.properties");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Or.load(fis);
				log.debug("OR properties loaded!!!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			
			
			if(Config.getProperty("browser").equals("firefox"))
			{
				driver =new FirefoxDriver();
				log.debug("Firfox browser launched");
			}
			
			else if(Config.getProperty("browser").equals("chrome"))
			{
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\src\\test\\resources\\Executables\\chromedriver.exe");
				driver =new ChromeDriver();
				log.debug("Chrome browser launched");
				
			}
			else if(Config.getProperty("browser").equals("ie"))
			{
				System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"\\src\\test\\resources\\Executables\\IEDriverServer.exe");
				driver =new InternetExplorerDriver();
				log.debug("Internet Explorer browser launched");
				
			}
			
			driver.get(Config.getProperty("testsiteurl"));
			log.debug("Navigated to"+Config.getProperty("testsiteurl"));
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(Integer.parseInt(Config.getProperty("implicit.wait")), TimeUnit.SECONDS);
			wait=new WebDriverWait(driver,Integer.parseInt(Config.getProperty("explicit.wait")));
			
			try {
				DbManager.setMysqlDbConnection();
				log.debug("Database Connection established");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			topnav=new TopNavigation();
			
		}
			
			
		}
		
		public static void click(String key)
		{
			
			if(key.endsWith("_xpath"))
			{
			driver.findElement(By.xpath(Or.getProperty(key))).click();
			}
			
			else if(key.endsWith("_css"))
			{
			driver.findElement(By.cssSelector(Or.getProperty(key))).click();
			}
			else if(key.endsWith("_id"))
			{
			driver.findElement(By.id(Or.getProperty(key))).click();
			}
			
			log.debug("Clicking on an element"+key);
			
		}
		
		public static String getText(String key)
		{
			String text="";
			if(key.endsWith("_xpath"))
			{
			 text=driver.findElement(By.xpath(Or.getProperty(key))).getText();
			}
			
			else if(key.endsWith("_css"))
			{
			driver.findElement(By.cssSelector(Or.getProperty(key))).click();
			}
			else if(key.endsWith("_id"))
			{
			driver.findElement(By.id(Or.getProperty(key))).click();
			}
			
			
			log.debug("Clicking on an element"+key);
			return text;
			
		}
		
		public static String getTitle()
		{
			String actualTitle="";
			
				actualTitle=driver.getTitle();
			
		
			
			log.debug("Obtained the title of the page:-");
			return actualTitle;
			
		}
		
		public static void type(String key, String value)
		{		
			if(key.endsWith("_xpath"))
			{
			driver.findElement(By.xpath(Or.getProperty(key))).sendKeys(value);
			}
			
			else if(key.endsWith("_css"))
			{
			driver.findElement(By.cssSelector(Or.getProperty(key))).sendKeys(value);
			}
			else if(key.endsWith("_id"))
			{
			driver.findElement(By.id(Or.getProperty(key))).sendKeys(value);
			}
			
			log.debug("Typing on an element"+key+"Entered value"+value);
			
		}
		
		public static void select(String key, String value)
		{		
			
			if(key.endsWith("_xpath"))
			{
				dropdown=driver.findElement(By.xpath(Or.getProperty(key)));
			}
			
			else if(key.endsWith("_css"))
			{
				dropdown=driver.findElement(By.cssSelector(Or.getProperty(key)));
			}
			else if(key.endsWith("_id"))
			{
				dropdown=driver.findElement(By.id(Or.getProperty(key)));
			}
			
			Select select =new Select(dropdown);
			select.selectByVisibleText(value);
			
			log.debug("Selecting value from a dropdown"+key+"Value selected as "+value);
			
		}
		
		public static boolean isElementPresent(String key)
		{
		
			try{
				if(key.endsWith("_xpath"))
				{
				driver.findElement(By.xpath(Or.getProperty(key)));
				}
				
				else if(key.endsWith("_css"))
				{
				driver.findElement(By.cssSelector(Or.getProperty(key)));
				}
				else if(key.endsWith("_id"))
				{
				driver.findElement(By.id(Or.getProperty(key)));
				}
				
				log.debug("Finding element with locator"+key);
							
				return true;
				
			}
			
			catch(Throwable t)
			{
				log.debug("Element not found"+key+" "+t.getMessage());
				return false;
			}
		}
		@AfterSuite
		public void tearDown()
		{
			driver.quit();
			log.debug("Test execution completed");
		}
		
	}
