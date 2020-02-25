package com.quantum.utilities;

import org.openqa.selenium.WebDriver;

import com.neotys.selenium.proxies.NLWebDriver;
import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.quantum.utils.DeviceUtils;


public class NLDriver {
	protected String platform = ConfigurationManager.getBundle().getString("perfecto.capabilities.platformName");
	protected static WebDriver driver = ConfigurationManager.getBundle().getString("neoload", "false").equalsIgnoreCase("true") ?  (NLWebDriver) DeviceUtils.getQAFDriver().getUnderLayingDriver() : DeviceUtils.getQAFDriver().getUnderLayingDriver();

	public static void startTransaction(String desc) {
		if(ConfigurationManager.getBundle().getString("neoload", "false").equalsIgnoreCase("true")) {
			((NLWebDriver) driver).startTransaction(desc);
		}	
	}

	public static void stopTransaction() {
		if(ConfigurationManager.getBundle().getString("neoload", "false").equalsIgnoreCase("true")) {
			((NLWebDriver) driver).stopTransaction();
		}	
	}
	
//	public static WebDriver getDriver() {
//		if(ConfigurationManager.getBundle().getString("neoload", "false").equalsIgnoreCase("true")) {
//			return (NLWebDriver) DeviceUtils.getQAFDriver().getUnderLayingDriver();
//		}else {
//			return DeviceUtils.getQAFDriver().getUnderLayingDriver();  
//		}
//	}
}
