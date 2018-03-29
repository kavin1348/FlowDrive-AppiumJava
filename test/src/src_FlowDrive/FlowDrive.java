package src_FlowDrive;

import static org.testng.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.html5.Location;
import org.testng.annotations.*;

//import com.thoughtworks.selenium.Wait;
//import com.thoughtworks.selenium.webdriven.commands.WaitForCondition;
//import com.thoughtworks.selenium.webdriven.commands.WaitForPageToLoad;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import pageObjects.LoginPage;
public class FlowDrive 
{
	WebDriver driver;
	//AppiumDriver driver1;      //For future work when using mock location to record journey

@BeforeClass 	//Here we set the desired capabilities and pass the app's package and activity to Appium
public void setUp() throws MalformedURLException{

	DesiredCapabilities capabilities = new DesiredCapabilities();
	capabilities.setCapability("BROWSER_NAME", "Android");
	capabilities.setCapability("VERSION", "4.4.2"); 
	capabilities.setCapability("deviceName","Emulator");
	capabilities.setCapability("platformName","Android");
    capabilities.setCapability("appPackage", "com.thefloow.flo");               // Package name of FlowDrive app 
	capabilities.setCapability("appActivity","com.thefloow.flo.activity.LauncherActivity"); // Launcher activity of FlowDrive app 
  
	driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);      // Creating a new web driver instance to connect to the Appium server and launch the app with above mentioned capabilities
	driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);                           // Setting a time for the driver to wait when searching for an element if it is not immediately present
//   driver1 = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
   
}

@Test(priority=0)    //Test whether the user can agree to terms and conditions

public void AgreeTermsAndConditions() throws Exception{
	if	(driver.findElements( By.name("Terms and Conditions") ).size() != 0)  // Reason to use a 'if' statement here is that, 'Terms and conditions' will appear only when you are opening the app for first time after installation
	{
		WebElement agree=driver.findElement(By.name("I Agree"));
		agree.click();
		assert((driver.findElement(By.name("Please log in"))!= null ));
	}
	else 
	{
		System.out.println("Skipped this test case as the user has already accepted the EULA");
	}
}
@Test(priority=1)    //Test whether the user can login to the app 
public void testLogin() throws Exception{
	if	(driver.findElements( By.name("Please log in") ).size() != 0)     // proceed with the login only if the log in option is available
	{
		//WebElement username=driver.findElement(By.id("edit_text_email"));
		//username.sendKeys("kavin1349@gmail.com");
		LoginPage.username(driver).sendKeys("kavin1347@gmail.com");
	
		WebElement password=driver.findElement(By.id("edit_text_password"));
		password.sendKeys("kavin123");
	
		WebElement login=driver.findElement(By.name("Log in"));
		login.click();
		assert((driver.findElement(By.name("Welcome to FlowDrive"))!= null ));         // assert the welcome page which shows up once the login is successful
		
		WebElement welcomeclose=driver.findElement(By.id("btn_welcome_close"));
		welcomeclose.click();
	
// Below code is to close the "Manage Battery usage" alert message which shows up only when you login to the app for the first time 	
		if	(driver.findElements( By.id("alertTitle") ).size() != 0)
		{
			WebElement ok=driver.findElement(By.name("OK"));
			ok.click();
			driver.navigate().back();
		}
	}
	else 																							// if the user has already logged in to the app previously, then we don't get to see the login page
	{                                                                    
		System.out.println("Skipped this test case as the user has already logged in");
	}
}

@Test(priority=2)  //Test whether contents of the tab are displayed when clicking the tab
public void testTabNavigation() throws Exception{
	
	//Below two statements will close the app and reopen it every time before a test, so that the test run is not affected by the previous test
	teardown();
	setUp();
	
	WebElement journeys=driver.findElement(By.id("tab_journeys"));
	journeys.click();
	
	// Assert whether the contents of the tab such as date, distance, duration and score are displayed
	assert((driver.getPageSource().contains("Date")) && (driver.getPageSource().contains("Distance")) && (driver.getPageSource().contains("Duration")) && (driver.getPageSource().contains("Score")));
}

@Test(priority=3)  //Test whether clicking on tab enables the tab
public void testTabNavigation2() throws Exception{
	teardown();
	setUp();
	String[] Tabs = {"journeys","score","social","help"};             // Storing all the tab names in an array and looping them using for loop, in order to test all the tabs
	  int size = Tabs.length;
      for (int i=0; i<size; i++)
      {
    		WebElement tabName=driver.findElement(By.id("tab_"+Tabs[i]));
    		tabName.click();
    		assert(tabName.isSelected());							//check whether the tab is selected in the tab navigation
      }
}

@Test(priority=4)  //Test whether Journey can be started and stopped
public void recordJourney() throws Exception{
	
	teardown();											
	setUp();
	
	WebElement home=driver.findElement(By.id("tab_home"));
	home.click();
	
    WebElement startJourney=driver.findElement(By.id("btn_start"));
    startJourney.click();
    
    assert((driver.findElement(By.id("btn_acquiring_gps"))!= null ));

/*  Ignore this section. I couldn't get location mocking work in physical device due to Appium limitation
    Location loc = new Location(3.0843796, 101.6710152, 1000);  // latitude, longitude, altitude
    driver1.setLocation(loc);
*/
    
    Thread.sleep(30000);															// wait for up to 30 seconds for the gps to acquire the device location     
    if	(driver.findElements( By.id("btn_stop") ).size() != 0)
    {
    	WebElement stopJourney=driver.findElement(By.id("btn_stop"));
    	stopJourney.click();
    	assert((driver.findElement(By.name("This journey will be recorded because it was less than 0.5 mile."))!= null ));         // Default warning message when the journey does not meet the requirements
    }
    else
    {
    	System.out.println("Cannot perform this test at the moment as the gps connection is weak");
    }
}

@Test(priority=5)  //Test whether Emergency help options are accessible
public void emergencyHelpOptions() throws Exception{
	
	teardown();
	setUp();
	WebElement home=driver.findElement(By.id("tab_home"));
	home.click();
	
	WebElement emergencyButton=driver.findElement(By.id("btn_emergency"));
    emergencyButton.click();
    assert((driver.findElement(By.name("You selected the Emergency option."))!= null ));
    		
    WebElement breakdown=driver.findElement(By.id("emergency_btn_breakdown"));
    breakdown.click();
    assert((driver.findElement(By.id("btn_call_now"))!= null ));
    		
    WebElement callNow=driver.findElement(By.id("btn_call_now"));
    callNow.click();
    assert((driver.getPageSource().contains("FlowDrive is a demo app and as a result this button does nothing. It can be configured to dial a number or send an email/other digital message as required.")));
}

/*Below test is an attempt to perform data driven test, where by the data on the app screen should match the expected data
  The test data is set to match the data pre-populated in the account used for testing
 */
@Test(priority=6)  //Test whether saved journey details can be viewed
public void viewSavedjourneydetails() throws Exception{
	
	teardown();
	setUp();
	WebElement journeys=driver.findElement(By.id("tab_journeys"));
	journeys.click();
	
	WebElement journey1=driver.findElement(By.name("3"));					// Selecting a specific journey with distance as 3 miles
	journey1.click();
	Thread.sleep(2000);														// wait for the journey details to load
	
	// The below data is a sample test data as mentioned in the beginning of the test
	assert((driver.getPageSource().contains("21:16")) && (driver.getPageSource().contains("21:23")) && (driver.getPageSource().contains("06/11/2017")) && (driver.getPageSource().contains("3")));
}

@Test(priority=7)  //Test whether saved journey details can be modified
public void modifySavedjourneydetails() throws Exception{
	
	teardown();
	setUp();
	WebElement journeys=driver.findElement(By.id("tab_journeys"));
	journeys.click();
	
	WebElement journey2=driver.findElement(By.id("image_view_journey_type"));
	journey2.click();
	WebElement modifyJourney=driver.findElement(By.id("btn_tag_journey"));
	modifyJourney.click();
	WebElement bus=driver.findElement(By.id("btn_bus"));
	bus.click();																// changing the journey type from train to bus
	WebElement ok=driver.findElement(By.name("OK"));
	ok.click();
	WebElement save=driver.findElement(By.id("btn_save"));
	save.click();
	modifyJourney.click();
	assert((driver.getPageSource().contains("We detected this journey type as:")) && (driver.getPageSource().contains("BUS")) );
	
	// Below steps will reset the journey back to original journey type in order for the future tests to work
	WebElement train=driver.findElement(By.id("btn_train"));
	train.click();
	ok.click();
	save.click();
}

@Test(priority=8)  //Test whether map view is loaded
public void Mapview() throws Exception{
	teardown();
	setUp();
	
	WebElement journeys=driver.findElement(By.id("tab_journeys"));
	journeys.click();
	
	WebElement journey2=driver.findElement(By.id("image_view_journey_type"));
	journey2.click();
	WebElement mapView=driver.findElement(By.id("btn_view_map"));
	mapView.click();
	//Thread.sleep(2000);
	
	assert ((driver.findElement(By.id("mapView")))) != null;												// check whether map view is loaded
	assertEquals(((driver.findElement(By.id("radio_mapbox_street"))).getAttribute("checked")), "true");		// check whether street Map view is selected by default
}


@Test(priority=9)  //Test whether overall score can be viewed
public void OverallScoreDetails() throws Exception{
	teardown();
	setUp();
	
	WebElement score=driver.findElement(By.id("tab_score"));
	score.click();
	Thread.sleep(2000);
//    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	assert((driver.getPageSource().contains("74")));
	assertEquals(((driver.findElement(By.id("text_view_score"))).getAttribute("name")), "74");				// check whether the score is 74
}
	
/*Below test is an attempt to perform data driven test, where by the data on the app screen should match the expected data
The test data is set to match the data pre-populated in the account used for testing
*/

@Test(priority=10)  //Test whether individual score can be viewed
public void IndividualScoreDetails() throws Exception{
	teardown();
	setUp();
	
	WebElement score=driver.findElement(By.id("tab_score"));
	score.click();
	WebElement scoreDetails=driver.findElement(By.id("btn_component_score"));
	scoreDetails.click();
//    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	Thread.sleep(2500);
	assert((driver.getPageSource().contains("97")) && (driver.getPageSource().contains("65")) && (driver.getPageSource().contains("93")) && (driver.getPageSource().contains("62")) && (driver.getPageSource().contains("75")));
}

@Test(priority=11)  //Test whether social profile can be edited
public void MySocialProfile() throws Exception{
	teardown();
	setUp();
	
	WebElement social=driver.findElement(By.id("tab_social"));
	social.click();
	
	WebElement myProfile=driver.findElement(By.id("social_menu_my_profile"));
	myProfile.click();
	
	WebElement editProfile=driver.findElement(By.id("btn_edit_my_profile"));
	editProfile.click();
	
	WebElement aboutMe=driver.findElement(By.id("my_profile_about_me_edit"));
	aboutMe.clear();																	// Clear the existing description
	aboutMe.sendKeys("This is Kavin R");
	
	WebElement saveProfile=driver.findElement(By.id("btn_save_my_profile"));
	saveProfile.click();																// save the changes to the profile
	
	assertEquals(((driver.findElement(By.id("my_profile_about_me"))).getAttribute("name")), "This is Kavin R");
}

@Test(priority=12)  //Test whether custom preferences can be set
public void changePreferences() throws Exception{
	teardown();
	setUp();
	
	WebElement help=driver.findElement(By.id("tab_help"));
	help.click();
	
	WebElement preferences=driver.findElement(By.name("Preferences"));
	preferences.click();
	
	WebElement twentyPercent=driver.findElement(By.id("index_1"));						
	twentyPercent.click();																// select 20%
	assertEquals((twentyPercent.getAttribute("checked")), "true");
	
	// Reset back to 30% for future test runs to pass
	WebElement thirtyPercent=driver.findElement(By.id("index_3"));
	thirtyPercent.click();
	driver.navigate().back();
}

@Test(priority=13)  //Test whether help can be searched using keywords
public void searchHelp() throws Exception{
	teardown();
	setUp();
	
	WebElement help=driver.findElement(By.id("tab_help"));
	help.click();
	
	WebElement FAQ=driver.findElement(By.name("FAQs"));
	FAQ.click();
	
	WebElement searchBox=driver.findElement(By.id("edit_text_faq_search"));
	searchBox.sendKeys("speed");														// Type the word 'speed' in search box in order to find matching help content
	
	assert(driver.getPageSource().contains("What is the Speed Score?"));				// Verify that the searched keyword returned the relevant help content
}


@Test(priority=14)  //Test whether version number is available in About page
public void appVersionInAboutPage() throws Exception{
	teardown();
	setUp();
	
	WebElement help=driver.findElement(By.id("tab_help"));
	help.click();
	
	WebElement about=driver.findElement(By.name("About"));
	about.click();
	
	assertEquals(((driver.findElement(By.id("text_view_app_version"))).getAttribute("name")), "1.7.8.13");			// checking whether the app's version number is 1.7.8.13
	
}
/*
@AfterTest
public void teardown() {
	driver.quit();
} */

@AfterClass						// Close the app after all the tests have finished 
public void teardown(){
	driver.quit();
}
}
