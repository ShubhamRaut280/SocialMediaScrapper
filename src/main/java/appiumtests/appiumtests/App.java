package appiumtests.appiumtests;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.logging.Handler;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import static appiumtests.appiumtests.UserModel.saveUserModelListAsJson;

public class App 
{
	
    static  AppiumDriver<MobileElement> driver;

	
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
        driver = new AppiumDriver<MobileElement>(url, capabilities);
        

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
        MobileElement profiletab = driver.findElement(By.id("com.instagram.android:id/profile_tab"));
        profiletab.click();

        MobileElement followers = driver.findElement(By.id("com.instagram.android:id/row_profile_header_followers_container"));
        followers.click();

        // Scroll and capture all loaded list items
        List<UserModel> allItems = new ArrayList<>();
        boolean canScrollMore = true;
        UserModel lastItem = new UserModel();

        while (canScrollMore) {
            List<MobileElement> listItems = driver.findElements(By.xpath("//android.widget.ListView/android.widget.LinearLayout"));

            for (MobileElement listItem : listItems) {
                String name = "", username = "";
                MobileElement image ;
                try {
                    try {
                        username = listItem.findElement(By.id("com.instagram.android:id/follow_list_username")).getText();
                    } catch (Exception e) {
                        // Handle exceptions
                    }

                    try {
                        name = listItem.findElement(By.id("com.instagram.android:id/follow_list_subtitle")).getText();
                    } catch (Exception e) {
        
                        // Handle exceptions
                    }
        
                    image = listItem.findElement(By.id("com.instagram.android:id/follow_list_user_imageview"));
                    String imagestring = captureElementScreenshotAsBase64(image);
                    
                    
                    
                    String userDetails = "Instagram username: " + username + " | Name: " + name + " | Image url : "+ imagestring;
                    UserModel user = new UserModel(username, name, getUrl(imagestring));

                    if (!allItems.contains(user)) {
                        allItems.add(user);
                        print(userDetails);
                    }
                } catch (Exception e) {
                    // Handle exceptions
                }
            }

            // Try to scroll
            UserModel currentLastItem = allItems.get(allItems.size() - 1);
            canScrollMore = scrollDown();

            if (lastItem.equals(currentLastItem)) {
                print("End of list reached.");
                canScrollMore = false; 
            } else {
                lastItem = currentLastItem;
            }
        }

        System.out.println("Total loaded items: " + allItems.size());

        saveUserModelListAsJson(allItems, "UserDetails.json");
    }

    private static String getUrl(String imagestring) {
        return "data:image/jpeg;base64,"+imagestring;
    }

    public static  String captureElementScreenshotAsBase64(MobileElement element) {
        // Capture the screenshot of the element
        File screenshot = element.getScreenshotAs(OutputType.FILE);

        try {
            // Convert the screenshot file to a byte array
            byte[] fileContent = FileUtils.readFileToByteArray(screenshot);

            // Encode the byte array to a Base64 string
            String base64Image = Base64.getEncoder().encodeToString(fileContent);

            // Optionally delete the file after conversion
            screenshot.delete();

            return base64Image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean scrollDown() {
        int startX = driver.manage().window().getSize().width / 2;
        int startY = (int) (driver.manage().window().getSize().height * 0.8);
        int endY = (int) (driver.manage().window().getSize().height * 0.2);

        // Perform the swipe action
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), startX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(List.of(swipe));

        // Check if new items are loaded after scrolling
        List<MobileElement> newListItems = driver.findElements(By.xpath("//android.widget.ListView/android.widget.LinearLayout"));
        return !newListItems.isEmpty(); // Continue scrolling if more items are loaded
    }


    	
        
        
    	
    
    
    public static void print(String s){
    	System.out.println(s);
    }
}
