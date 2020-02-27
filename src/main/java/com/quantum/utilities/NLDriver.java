package com.quantum.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebDriver;

import com.neotys.selenium.proxies.NLWebDriver;
import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebElement;
import com.quantum.utils.DeviceUtils;

public class NLDriver {

	/**
	 * returns the Neoload driver.
	 * 
	 * @param description
	 */
	public static WebDriver getDriver() {
		final WebDriver driver = ConfigurationManager.getBundle().getString("neoload", "false").equalsIgnoreCase("true") ?  (NLWebDriver) DeviceUtils.getQAFDriver().getUnderLayingDriver() : DeviceUtils.getQAFDriver().getUnderLayingDriver();
		return driver;
	}

	/**
	 * Starts transaction of Neoload driver.
	 * 
	 * @param description
	 */
	public static void startTransaction(String desc) {
		if(isNeoLoadEnabled()) {
			String platform = ConfigurationManager.getBundle().getString("perfecto.capabilities.platformName");
			((NLWebDriver) getDriver()).startTransaction(platform + "_" + desc);
		}	
	}

	/**
	 * Stops transaction of Neoload driver
	 * 
	 */
	public static void stopTransaction() {
		if(isNeoLoadEnabled()) {
			((NLWebDriver) getDriver()).stopTransaction();
		}	
	}

	/**
	 * returns QAFExtendedWebElement's locator value from .loc files
	 * 
	 * @param QAFExtendedWebElement
	 */
	public static String getLocator(QAFExtendedWebElement element) {
		String matching = "";
		Pattern pattern = Pattern.compile("\\=.*"); 
		Matcher m = pattern.matcher(element.getMetaData().get("locator").toString()); 
		while (m.find()) { 
			matching = m.group(0).substring(1);
		}
		return matching;
	}

	/**
	 * Returns true if neoload driver is enabled in application.properties
	 * 
	 */
	public static boolean isNeoLoadEnabled() {
		String neoload = ConfigurationManager.getBundle().getString("neoload", "false");
		if (neoload.equalsIgnoreCase("true")) {
			return true;
		}else {
			return false;
		}
	}
}