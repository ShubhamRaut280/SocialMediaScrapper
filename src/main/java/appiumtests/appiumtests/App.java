package appiumtests.appiumtests;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Handler;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public class App 
{
	
    static  AppiumDriver driver;

	
    public static void main( String[] args )
    {
    	try {
            setCapabilities();

        } catch (MalformedURLException e) {
            System.out.println("Error : "+e.getMessage());
        }
    }
    
    public static void setCapabilities() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", "Redmi Note 8");
        capabilities.setCapability("udid", "179124d9");
//        capabilities.setCapability("udid", "emulator-5554");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", "9.0");
        capabilities.setCapability("appPackage" , "com.instagram.android");
        capabilities.setCapability("appActivity" ,"com.instagram.android.activity.MainTabActivity" );
//        capabilities.setCapability("appActivity" ,"com.instagram.mainactivity.LauncherActivity" );
//        capabilities.setCapability("appPackage" , "com.shubham.emergencyapplication");
//        capabilities.setCapability("appActivity" ,"com.shubham.emergencyapplication.Ui.Activities.LoginActivity" );
        capabilities.setCapability("noReset", "true");
        capabilities.setCapability("fullReset", "false");
//        capabilities.setCapability("automationName", "selendroid");
        URL url = new URL("http://127.0.0.1:4723/wd/hub");
        driver = new AppiumDriver(url, capabilities);
        

        System.out.println("Application started..");
        
        try {
            Thread.sleep(4000); // Delay for 5 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        gotoProfile();

    }
    
    
    /**
     * 
     */
    public static void gotoProfile() {
    	MobileElement profiletab = (MobileElement) driver.findElement(By.id("com.instagram.android:id/profile_tab"));
    	profiletab.click();
    	
    	MobileElement followers = (MobileElement) driver.findElement(By.id("com.instagram.android:id/row_profile_header_followers_container"));
    	followers.click();
    	
    	//follower list 
    	MobileElement list = (MobileElement) driver.findElement(By.id("android:id/list"));
    	
    	List<MobileElement> listItems = list.findElements(By.xpath("//android.widget.ListView/android.widget.LinearLayout/*"));
    


    	System.out.println("list size is : "+ listItems.size());
        for(int i = 1 ; i < listItems.size(); i++) {
        	MobileElement row = (MobileElement) driver.findElement(By.xpath(
        			"//android.widget.ListView[@resource-id=\"android:id/list\"]/android.widget.LinearLayout["+i+"]"));
        	
        	List<MobileElement> userid =  row.findElements(By.id("com.instagram.android:id/follow_list_username"));
        	List<MobileElement> name =  row.findElements(By.id("com.instagram.android:id/follow_list_subtitle"));
        	List<MobileElement> image =  row.findElements(By.id("com.instagram.android:id/follow_list_user_imageview"));
        	        	
        	for(int j = 0; j < name.size(); j++) {
        		print("Instagram username : "+ userid.get(j) .getText()+ " | " + "Name : "+  name.get(j).getText());
        	}

        	
    
        
        }
    	
        
        
    	
    }
    
    public static void print(String s){
    	System.out.println(s);
    }
}
