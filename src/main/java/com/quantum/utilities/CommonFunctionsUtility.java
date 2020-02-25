package com.quantum.utilities;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.core.QAFTestBase;
import com.qmetry.qaf.automation.core.TestBaseProvider;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebDriver;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebElement;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebElement;
import com.qmetry.qaf.automation.util.StringMatcher;
import com.quantum.steps.PerfectoApplicationSteps;
import com.quantum.steps.PerfectoDeviceSteps;
import com.quantum.utils.DeviceUtils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.WordUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.FindsByXPath;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.io.IOException;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;

public class CommonFunctionsUtility {
	
	public static String deviceAudioFile = null;

	/* Launch & Close Application Code */
	/**
	 * This method will launch the application which is specified in the appName
	 * property in application.properties file.
	 * 
	 */
	public static void launchApp(String name) {
		PerfectoApplicationSteps.startAppByName(name);
	}

	/**
	 * This method will launch the application if it is already closed and then
	 * close the application which is specified in te appName property in
	 * application.properties file.
	 */
	public static void closeApp(String name) {
		//launchApp(name);
		try {
			PerfectoApplicationSteps.closeAppByName(name);
		} catch (Exception e) {
		}
	}
	/* END of Launch & Close Application Code */


	
	public static void login(String email, String password) {

		//clearPopups();

		if (isLogoedIn()) {
			//signout();
		}  

//		DeviceUtils.startApp(name, "name");
		//clearPopups();

		//getDriver().findElement("loginVideo.button").waitForVisible(120);
		
		if(!isLogoedIn()){
			getDriver().findElement("loginVideo.button").click();
			System.out.println("testing1");
			
			
			//checking if we are at login page
				
			// implementing entering email for ipad and iphone
			try { 
				getDriver().findElement( "email.inputText").sendKeys(email);
			} catch (Exception e) {
				getDriver().findElement( "ipademail.inputText").sendKeys(email);
			}
			
			// implementing entering email for ipad and iphone
			try { 
				getDriver().findElement( "password.inputText").sendKeys(password);
			} catch (Exception e) {
				getDriver().findElement( "ipadpassword.inputText").sendKeys(password);
			}
			
						
			//Clicking on login button for ipad and ios
			try { 
				getDriver().findElement("done.button").click();
			} catch (Exception e) {
				getDriver().findElement("ipadlogin.button").click();
			}
			
			if (isAndroid())
				scrollToText("Confirm");
	
			getDriver().findElement("confirm.button").click();
			
			
			CommonFunctionsUtility.clearPopups(false);
		}

	}	

	public static void signout() {

		//clearPopups();

		if (isAndroid()){
			selectOptionMenu("Profile");
		}
		else {
			getDriver().findElement("profileMenu.button").click();
		}
		if (isAndroid()){
			scrollToText("Reset App");
		}
		getDriver().findElement("resetApp.button").click();
		getDriver().findElement("resetPopupOk.button").click();

	}

	public static boolean isLogoedIn() {

		boolean isLoggedin = false;

		if (isAndroid()){
			isLoggedin = getDriver().findElement("optionsMenu.button").isPresent();

		} else {
			isLoggedin = getDriver().findElement("profileMenu.button").isPresent();
		}

		return isLoggedin;

	}
	
	
	public static boolean isJabberLoggedIn() {

		boolean isLoggedin = false;

		if (isAndroid()){
			isLoggedin = getDriver().findElement("jabberOptions.menu").isPresent();

		} else {
			isLoggedin = getDriver().findElement("jabberOptions.menu").isPresent();
		}

		return isLoggedin;

	}

	public static void clearPopups(Boolean firsttime) {
		

		if (new QAFExtendedWebElement("updateAvailable.title").isPresent()) {
			getDriver().findElement("updateIgnore.button").click();
		}
//
//		//Required only when application installed first time
//		if (firsttime) {
//			while (new QAFExtendedWebElement("popupAllow.button").isPresent()) {
//				getDriver().findElement("popupAllow.button").click();
//			}
//	
//			if (new QAFExtendedWebElement("updateAvailable.title").isPresent()) {
//				getDriver().findElement("updateIgnore.button").click();
//			}
//		}
	}

	public static void selectOptionMenu(String optionName) {

		getDriver().findElement("optionsMenu.button").click();
		getDriver().findElement(String.format(ConfigurationManager.getBundle().getString("optionsMenu.option.button"),
				optionName)).click();
	}


	public static boolean isAndroid() {

		return getDriver().getCapabilities().getCapability("platformName").equals("Android");

	}
	
	
	
	public static boolean isIOS() {

		return getDriver().getCapabilities().getCapability("platformName").equals("iOS");
//		Map<String, Object> params = new HashMap();
//		params.put("property", "os");
//		String result1 = (String)getDriver().executeScript("mobile:handset:info", params);
//		return result1.equals("iOS");

	}
	
    public static void sendADBCommand(String cmd) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("command", cmd);

        String test = (String) getDriver().executeScript("mobile:handset:shell", params);
    }
    
	public static String getmodel(){
		return getDriver().getCapabilities().getCapability("model").toString();
	}

	public static String getCurrentDriver(){

		return ConfigurationManager.getBundle().getString("driver.name");
	}


	public static String getDefaultDriver() {

		return ConfigurationManager.getBundle().getString("default.driver.name");
	}

	public static void switchToDefaultDriver() {

		switchToDriver(getDefaultDriver());
	}


	public static void switchToDriver(String driverName) {

		//if (!getCurrentDriver().equals(driverName+"RemoteDriver")) {

		TestBaseProvider.instance().get().setDriver(driverName + "RemoteDriver");
		String envResources = ConfigurationManager.getBundle().getString(driverName + ".env.resources");
		ConfigurationManager.getBundle().setProperty("env.resources",envResources);
		//}

	}

	public static QAFExtendedWebDriver getDriver() {
		return new WebDriverTestBase().getDriver();
	}


	
	public static void scrollToText(String text) {
		Map<String, Object> params5 = new HashMap<>();
		params5.put("content", text);
		params5.put("scrolling", "scroll");
		params5.put("maxscroll", "7");
		params5.put("next", "SWIPE=(50%,80%),(50%,40%)");
		Object result5 = getDriver().executeScript("mobile:text:find", params5);
	}

	public static void scrollToElement(QAFExtendedWebElement element) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("direction", "down");
		params.put("element", element.getId());
		getDriver().executeScript("mobile: scroll", params);
	}


	public static void clickOnText(String text) {
		Map<String, Object> params = new HashMap<>();
		params.put("label", text);
		Object result1 = getDriver().executeScript("mobile:button-text:click", params);
	}


//	public static void validateImage(String content, String relationDirection) {
//		Map<String, Object> params = new HashMap<>();
//		params.put("content", content);
//		params.put("threshold", "85");
//		params.put("relation.direction", relationDirection);
//		Object result3 = getDriver().executeScript("mobile:image:find", params);
//
//	}
	
	
	public static String validateImage(String validation, String content) {
		
		String result = null;
		
		Map<String, Object> params = new HashMap<>();
		params.put("threshold", "85");
		params.put("match", "bounded");
		params.put("timeout", 30);
		
		if (validation.equalsIgnoreCase("find")){
			params.put("content", content);
			result = (String) getDriver().executeScript("mobile:checkpoint:image", params);
		}
		else if (validation.equalsIgnoreCase("click")){
			params.put("label", content);
			result = (String) getDriver().executeScript("mobile:button-image:click", params);
		}		
		return result;
	}
	
	
	public static String validateImage(String validation, String content, String timeout) {
		
		String result = null;
		
		Map<String, Object> params = new HashMap<>();
		params.put("threshold", "85");
		params.put("match", "bounded");
		params.put("timeout", timeout);
        params.put("measurement", "accurate");
        //params.put("source", "camera");
        //params.put("analysis", "automatic");
		if (validation.contentEquals("find")){
			params.put("content", content);
			result = (String) getDriver().executeScript("mobile:checkpoint:image", params);
		}
		else if (validation.contentEquals("click")){
			params.put("label", content);
			result = (String) getDriver().executeScript("mobile:button-image:click", params);
		}
		
		return result;
		
	}
	

	
	public static String OCRTextValidation(String operation, String content){
		
		String result = null;
		
		Map<String, Object> params1 = new HashMap<>();
		params1.put("timeout", "15");
		params1.put("threshold", "85");
		params1.put("words", "words");
		List<String> genericOptions1 = new ArrayList<>();
		genericOptions1.add("natural-language=true");
		params1.put("ocr", genericOptions1);
		
		if(operation.contentEquals("find")){
			params1.put("content", content);
			result = (String) getDriver().executeScript("mobile:checkpoint:text", params1);
		}
		else if (operation.contentEquals("click")){
			params1.put("label", content);
			result = (String) getDriver().executeScript("mobile:button-text:click", params1);
		}
		
		return result;
	}
	
	public static String OCRTextEntry(String operation, String label, String content){
		
		String result = null;
		
		Map<String, Object> params1 = new HashMap<>();
		params1.put("label", label);
		params1.put("text", content);
		params1.put("timeout", "20");
		params1.put("words", "words");
		result = (String) getDriver().executeScript("mobile:edit-text:set", params1);
		
		return result;
	}
	
	public static String OCRTextValidation(String operation, String content, String timeout){
		
		String result = null;
		
		Map<String, Object> params1 = new HashMap<>();
		params1.put("timeout", timeout);
		params1.put("threshold", "80");
		params1.put("words", "words");
        params1.put("measurement", "accurate");
        params1.put("source", "camera");
        params1.put("analysis", "automatic");	
		List<String> genericOptions1 = new ArrayList<>();
		genericOptions1.add("natural-language=true");
		params1.put("ocr", genericOptions1);
		
		if(operation.contentEquals("find")){
			params1.put("content", content);
			result = (String) getDriver().executeScript("mobile:checkpoint:text", params1);
		}
		else if (operation.contentEquals("click")){
			params1.put("label", content);
			result = (String) getDriver().executeScript("mobile:button-text:click", params1);
		}
		
		return result;
	}
	
	public static void SendMessage(String host, String ChatRandomMessage) throws InterruptedException {
		
		switchToDriver(host);
		closeApp("Messages");
		sendADBCommand("am start -a android.intent.action.SENDTO -d sms:+17814912444 --es sms_body"+ " \""+ ChatRandomMessage +"\""+" --ez exit_on_sent true\r\n");		    
		Thread.sleep(4000);
		//sendADBCommand("input keyevent 22");
		//sendADBCommand("input keyevent 66");
		DeviceUtils.switchToContext("NATIVE");
		//getDriver().findElement("btn.msgsend").click();
		OCRTextValidation("click", "SEND");
		launchApp("Messages");
	}
	
	public static void ValidateMsgContent(String driver, String ChatRandomMessage){
		
		switchToDriver(driver);
		closeApp("Messages");
		launchApp("Messages");
		getDriver().findElement("//*[@label=\"‪‭+1 (781) 491-4943‬‬\"]").click();
		String test = CommonFunctionsUtility.OCRTextValidation("find", ChatRandomMessage);
		assertEquals("true", test);
	}
	
	public static void InjectImageStop(){
		
		// Inject picture of QR Code
    	Map<String, Object> params = new HashMap<>();
    	params.clear();
    	Object res = getDriver().executeScript("mobile:image.injection:stop", params);
	}
	
	public static void InjectImageStart(String repositoryfile, String appIdentifier){
		
		// Inject picture of credit card
    	Map<String, Object> params = new HashMap<>();
    	params.put("repositoryFile", repositoryfile);
    	params.put("identifier", appIdentifier);
    	Object res = getDriver().executeScript("mobile:image.injection:start", params);
	}
	
	public static String randomString(String chars, int length) {
		
		  Random rand = new Random();
		  StringBuilder buf = new StringBuilder();
		  for (int i=0; i<length; i++) {
		    buf.append(chars.charAt(rand.nextInt(chars.length())));
		  }
		  return buf.toString();
		}





	    
		public static void InjectAndRecordAudio(String hostdevice, String recorddevice, String FileKey) throws InterruptedException {
			
			//stopRecording();
			
	        switchToDriver(hostdevice);
	        //Thread.sleep(1000);
	        injectAudio(FileKey);
	        
	        switchToDriver(recorddevice);
	        deviceAudioFile = startRecording();
	        //Thread.sleep(1000);

//	        switchToDriver(recorddevice);
//	        //Thread.sleep(1000);
//	        stopRecording();
//	        return audioFile;
			
		}



	    public static Map<String, Object> injectAudio(String fileKey) {
	        Map<String, Object> params = new HashMap<>();
	        params.put("key", fileKey);
	        //params.put("wait", "wait");
	        getDriver().executeScript("mobile:audio:inject", params);
	        return params;
	    }

	    public static String startRecording() {
	        Map<String, Object> params = new HashMap<>();
	        return (String)getDriver().executeScript("mobile:audio.recording:start", params);
	    }


	    public static String stopRecording() {
	        Map<String, Object> params = new HashMap<>();
	        getDriver().executeScript("mobile:audio.recording:stop", params);
	        return deviceAudioFile;
	    }
	    
	    public static void validateAudio(String deviceAudioFile, String REFERENCE_FILE_KEY) {
	        Map<String, Object> params = new HashMap<>();
	        params.put("deviceAudio", deviceAudioFile);
	        //params.put("key", REFERENCE_FILE_KEY);
	        String text = (String) getDriver().executeScript("mobile:audio:text", params);
	        System.out.println(text);
	    }
	    
	    public static void androidCall(String callTo) {
	        sendADBCommand("am start -a android.intent.action.CALL -d tel:"+callTo);
	    }
	    
	    public static void disconnectAndroidCall() {
	        sendADBCommand("input keyevent KEYCODE_ENDCALL");
	    }
	    
	    public static void acceptAndroidCall() {
	        sendADBCommand("input keyevent KEYCODE_CALL");
	    }
	    
	    public static String validateAudioText(String deviceAudioFile, String REFERENCE_FILE_KEY) {
	        Map<String, Object> params = new HashMap<>();
	        params.put("deviceAudio", deviceAudioFile);
	        //params.put("key", REFERENCE_FILE_KEY);
	        params.put("content", "find a bank of America branch near Lexington Massachusetts please");
	        //params.put("content", "find a bank of America branch l;akjfalkjflkjl;df Massachusetts please");
	        String text = (String) getDriver().executeScript("mobile:audio-text:validation", params);
	        System.out.println(text);
	        return text;
	    }
	    
	   
	    public static void  SetVoWIFICall(String content) throws InterruptedException {
	    	if(isAndroid()) {
	    		closeApp("Settings");
	    		launchApp("Settings");
	    		
	    		DeviceUtils.switchToContext("NATIVE_APP");
	    		getDriver().findElementByXPath("//*[@text=\"Connections\"]").click();
	    		getDriver().findElementByXPath("//*[@text=\"Wi-Fi Calling\"]").click();
	    		
	    		
	    		
	            if(content.equalsIgnoreCase("enabled")) {
	            	boolean Status = ExplicitWait("//*[@resource-id=\"android:id/switch_widget\" and @checked='false']", 5);
	            	if(Status)
	            		getDriver().findElementByXPath("//*[@resource-id=\"android:id/switch_widget\"]").click();
	            	
	            }
	            else if(content.equalsIgnoreCase("disabled")) {
	            	boolean Status = ExplicitWait("//*[@resource-id=\"android:id/switch_widget\" and @checked='true']", 5);
	            	if(Status)
	            		getDriver().findElementByXPath("//*[@resource-id=\"android:id/switch_widget\"]").click();
	            	
	            }	   
	            
	            Thread.sleep(5000);
	    	}
	    	else {
	    		
	    		closeApp("Settings");
	    		launchApp("Settings");
	    		
	    		scrolltoXPath("//UIATableCell[@label=\"Phone\"]");
	    		
	    		if(content.equalsIgnoreCase("enabled")) {
	    			
	    			boolean Status = ExplicitWait("//*[@label=\"Wi-Fi Calling\" and @value=\"Off\"]", 5);
	            	if(Status) {
	            		getDriver().findElementByXPath("//*[@value=\"Wi-Fi Calling\"]").click();
	            		getDriver().findElementByXPath("//UIASwitch").click();
	            	}
	    		}
	    		if(content.equalsIgnoreCase("disabled")) {
	    			
	    			boolean Status = ExplicitWait("//*[@label=\"Wi-Fi Calling\" and @value=\"On\"]", 5);
	            	if(Status) {
	            		getDriver().findElementByXPath("//*[@value=\"Wi-Fi Calling\"]").click();
	            		getDriver().findElementByXPath("//UIASwitch").click();
	            	}
	    		}
	    		
	    	}
	    }
	    public static void SetWIFIState(String content) throws InterruptedException {
	    
	    	if(isAndroid()) {
				Map<String, Object> params1 = new HashMap<>();
				params1.put("wifi", content);
				Object result1 = getDriver().executeScript("mobile:network.settings:set", params1);
	    	}
	    	else {
	    		closeApp("Settings");
	    		Thread.sleep(5000);
	    		launchApp("Settings");
	    		
	    		DeviceUtils.switchToContext("NATIVE_APP");
	    		
	    		boolean status = ExplicitWait("//UIAButton[@label=\"Settings\"]", 5);
	    				
	    		if(status)		
	    			getDriver().findElementByXPath("//UIAButton[@label=\"Settings\"]").click();
	    		
	    		
//	    		boolean dispalyed = false;
//				try {
//					dispalyed = getDriver().findElementByXPath("").isDisplayed();
//				} catch (Exception e) {}

				
				boolean dispalyed = ExplicitWait("//*[@label=\"Wi-Fi\" and @value=\"Off\"]//*[@label=\"Off\"]", 7);
	    		
	    		if( (content.equalsIgnoreCase("Enabled") && dispalyed)) {
		    		getDriver().findElementByXPath("//*[@value=\"Wi-Fi\"]").click();
		    		
		    		getDriver().findElementByXPath("//UIASwitch[@label=\"Wi-Fi\"]").click();
		    		
		    		DeviceUtils.switchToContext("WEBVIEW");
	    		}
	    		else if( (content.equalsIgnoreCase("Disabled") && !dispalyed)) {
		    		getDriver().findElementByXPath("//*[@value=\"Wi-Fi\"]").click();
		    		
		    		getDriver().findElementByXPath("//UIASwitch[@label=\"Wi-Fi\"]").click();
		    		
		    		DeviceUtils.switchToContext("WEBVIEW");
	    		}
	    	}
	    		
	    }	
	    
	    
	    public static void ValidateVoWIFISEttings(String content) throws InterruptedException {
	    	

				
			if(isAndroid()) {
				
				Map<String, Object> params = new HashMap<>();
				params.put("start", "40%,01%");
				params.put("end", "40%,80%");
				params.put("duration", "2");
				Object res = getDriver().executeScript("mobile:touch:swipe", params);
				
				Thread.sleep(5000);
				
				if(content.equalsIgnoreCase("Enabled")){
					String test = CommonFunctionsUtility.validateImage("find", "PUBLIC:Guru/WifiCallEnable.png");
					assertEquals("true", test);
	    		}
				if(content.equalsIgnoreCase("Disabled")){
					String test = CommonFunctionsUtility.validateImage("find", "PUBLIC:Guru/WifiCallDisable.png");
					assertEquals("true", test);
	    		}
	    
				Map<String, Object> params1 = new HashMap<>();
				params1.put("keySequence", "BACK");
				Object result1 = getDriver().executeScript("mobile:presskey", params1);
	    	}
	    	else {

	    		
	    		closeApp("Settings");
	    		launchApp("Settings");
	    		
	    		scrolltoXPath("//UIATableCell[@label=\"Phone\"]");
	    		
	    		if(content.equalsIgnoreCase("enabled")) {
	    			
	    			boolean Status = ExplicitWait("//*[@label=\"Wi-Fi Calling\" and @value=\"On\"]", 5);
	    			assertEquals(true, Status);
	    		}
	    		
	    		if(content.equalsIgnoreCase("disabled")) {
	    			boolean Status = ExplicitWait("//*[@label=\"Wi-Fi Calling\" and @value=\"Off\"]", 5);
	    			assertEquals(true, Status);
	            }
	    		
	    	}
	    		
	    }
	    
	    public static void SetMobileNetwork(String content) throws InterruptedException {
		    
	    	if(isAndroid()) {
	    		closeApp("Settings");
	    		launchApp("Settings");
	    		
	    		DeviceUtils.switchToContext("NATIVE_APP");
	    		getDriver().findElementByXPath("//*[@text=\"Connections\"]").click();
	            
	            getDriver().findElementByXPath("//*[@text=\"Mobile networks\"]").click();
	            
	            getDriver().findElementByXPath("//*[@text=\"Mobile data\"]").click();
	            
	            if(content.equalsIgnoreCase("NO")) {
	            	boolean Status = ExplicitWait("//*[@resource-id=\"android:id/checkbox\" and @checked='true']", 5);
	            	if(Status)
	            		getDriver().findElementByXPath("//*[@resource-id=\"android:id/checkbox\"]").click();
	            	
	            }
	            if(content.equalsIgnoreCase("YES")) {
	            	boolean Status = ExplicitWait("//*[@resource-id=\"android:id/checkbox\" and @checked='false']", 5);
	            	if(Status)
	            		getDriver().findElementByXPath("//*[@resource-id=\"android:id/checkbox\"]").click();            	
	            }
	            	            
	            DeviceUtils.switchToContext("WEBVIEW");
	    	}
	    	else {
	    		closeApp("Settings");
	    		launchApp("Settings");

	    		DeviceUtils.switchToContext("NATIVE_APP");
	    		
	    		getDriver().findElement("btn.Cellular").click();
	    		
	    		if(content.equalsIgnoreCase("YES")) {
	    			String test = CommonFunctionsUtility.OCRTextValidation("find", "AT&T 4G", "10");
	    			if(test.equalsIgnoreCase("false")) {
	    				test = CommonFunctionsUtility.OCRTextValidation("find", "AT&T LTE", "7");
	    				if(test.equalsIgnoreCase("false"))
	    					getDriver().findElementByXPath("//UIASwitch[@label=\"Cellular Data\"]").click();
	    					
	    			}
	    			
	    		}
	    		if(content.equalsIgnoreCase("NO")) {
	    			String test = CommonFunctionsUtility.OCRTextValidation("find", "AT&T 4G", "10");
	    			if(test.equalsIgnoreCase("false")) {
	    				test = CommonFunctionsUtility.OCRTextValidation("find", "AT&T LTE", "7");
	    				if(test.equalsIgnoreCase("true"))
	    					getDriver().findElementByXPath("//UIASwitch[@label=\"Cellular Data\"]").click();
	    			}
	    			else if(test.equalsIgnoreCase("true"))
    					getDriver().findElementByXPath("//UIASwitch[@label=\"Cellular Data\"]").click();
	    			
	    		}	    		
	    	}
	    		
	    }
	    
		// Wind Tunnel: Gets the user experience (UX) timer
	    public static long timerGet(String timerType) {
	         String command = "mobile:timer:info";
	         Map<String,String> params = new HashMap<String,String>();
	         params.put("type", timerType);
	         long result = (long)getDriver().executeScript(command, params);
	             return result;
	    }
	    
	    
        public static boolean ExplicitWait(String xpath, int timeout) {
            By adTree = By.xpath(xpath);
            Map<String, Object> adParams = new HashMap<>();
                            //trying to wait for the ad to come up and then click the Expense button
                           
            getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

            FluentWait<WebDriver> await = new FluentWait<WebDriver> (getDriver())
                                                            .withTimeout(timeout, TimeUnit.SECONDS)
                                                            .pollingEvery(500, TimeUnit.MILLISECONDS)
                                                            .ignoring(NoSuchElementException.class);
                            try {
                                            await.until(ExpectedConditions.visibilityOf(getDriver().findElement(adTree)));
                            // SEtting Implicit wait back
                                            getDriver().manage().timeouts().implicitlyWait(45, TimeUnit.SECONDS);
                            return true;
                            } catch (Exception t) {
                                            System.out.println("Given Element is not available");
                            // SEtting Implicit wait back
                                            getDriver().manage().timeouts().implicitlyWait(45, TimeUnit.SECONDS);
                                            return false;
                            }
           
        }
        
        
        
        public static void scrolltoXPath(String xPath) {
        	int count = 0;
        	do {
        		try {
        			getDriver().findElement(By.xpath(xPath)).click();
        			count = 5;
        			break;

        		} catch (Exception NoSuchElementException) {
        			
        			count = count + 1;
        			Map<String, Object> params = new HashMap<>();
        			params.put("start", "40%,90%");
        			params.put("end", "40%,20%");
        			params.put("duration", "2");
        			Object res = getDriver().executeScript("mobile:touch:swipe", params);
        		}
        		
        	} while (count != 10);
        }
        
        
        public static void speechToTextValidate(String audioFileURL, String text) {
    		if (null == audioFileURL || audioFileURL.length() < 1) return ;
            Map<String, Object> params = new HashMap<>();
            params.put("content", text);
            params.put("deviceAudio", audioFileURL);
            params.put("match", "contain");
            Object result = getDriver().executeScript("mobile:audio-text:validation", params);
        }
}
