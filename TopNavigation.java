package Base;

import org.openqa.selenium.By;
import Pages.LandingPage;

public class TopNavigation 
{
	
	public void doSearch()
	{
		
		
	}
	
	public LandingPage goToHome()
	{
		
		return new LandingPage();
		
	}
	
	public void logOut()
	{	
		TestBase.click("userNav_xpath");
		TestBase.click("logOut_xpath");
	}
	
}
