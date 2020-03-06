package com.quantum.steps;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import com.qmetry.qaf.automation.step.QAFTestStepProvider;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebElement;
import com.quantum.utilities.NLDriver;
import com.quantum.utils.DeviceUtils;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@QAFTestStepProvider
public class GoogleStepDefs {
	@Given("^I am on Google Search Page$")
	public void I_am_on_Google_Search_Page() throws Throwable {
		new WebDriverTestBase().getDriver().get("http://www.google.com/");
	}

	@When("^I search for \"([^\"]*)\"$")
	public void I_search_for(String searchKey) throws Throwable {
		QAFExtendedWebElement searchBoxElement = new QAFExtendedWebElement("search.text.box");
		QAFExtendedWebElement searchBtnElement = new QAFExtendedWebElement("search.button");

		searchBoxElement.clear();
		searchBoxElement.sendKeys(searchKey);
		// Web and mobile elements are sometimes different so we have done two things we
		// used multiple/alternate locator strategy for finding the element.
		// We also used Javascript click because the element was getting hidden in
		// Desktop Web due to suggestions and was not clickable. This java script click
		// will work for both desktop and mobile in this case.
		JavascriptExecutor js = (JavascriptExecutor) DeviceUtils.getQAFDriver();
		js.executeScript("arguments[0].click();", searchBtnElement);

	}

	@Then("^it should have \"([^\"]*)\" in search results$")
	public void it_should_have_in_search_results(String result) throws Throwable {
		QAFExtendedWebElement searchResultElement = new QAFExtendedWebElement("partialLink=" + result);
		searchResultElement.verifyPresent(result);
	}

	@Then("^it should have following search results:$")
	public void it_should_have_all_in_search_results(List<String> results) {
		QAFExtendedWebElement searchResultElement;
		for (String result : results) {
			searchResultElement = new QAFExtendedWebElement("partialLink=" + result);
			searchResultElement.verifyPresent(result);
		}
	}

	@Given("^I am on Neoload submission$")
	public void iAmOnNeoloadSubmission() throws Throwable {

		QAFExtendedWebElement reports = new QAFExtendedWebElement("nl.reports");
		QAFExtendedWebElement submit = new QAFExtendedWebElement("nl.submit");
		
		//Both startTransaction & stopTransaction are activated only when neoload=true 
		NLDriver.startTransaction("home_Perfecto");
		//replace all your driver.get() to NLDriver.getDriver().get()
		NLDriver.getDriver().get("http://ushahidi.demo.neotys.com/");
		NLDriver.stopTransaction();

		NLDriver.startTransaction("reports_Perfecto");
		//if condition to differentiate usage of QAFExtendedWebElement functions across load testing flow without custom waits/ quantum testing
		if(NLDriver.isNeoLoadEnabled()) {
			//You can replace existing QAFExtendedWebElement click with the below format to work seamless across load/non-load/ put them in a if condition as well
			NLDriver.getDriver().findElement(By.xpath(NLDriver.getLocator(reports))).click();
		}else { 
			reports.waitForVisible(5000);
			reports.click();
		}
		NLDriver.stopTransaction();

		//The following commands works with or without neoload enablement
		NLDriver.startTransaction("submit_Perfecto");
		NLDriver.getDriver().findElement(By.partialLinkText(NLDriver.getLocator(submit))).click();
		NLDriver.stopTransaction();
		
		//The following command will not be captured in neoload, it will not throw an exception when neoload is enabled but will perform a click on reports.
		reports.click();
	}

	@Given("^I am on Neo Alerts$")
	public void I_am_on_Neoload() throws Throwable {

		QAFExtendedWebElement reports = new QAFExtendedWebElement("nl.reports");
		QAFExtendedWebElement alerts = new QAFExtendedWebElement("nl.alerts");
		QAFExtendedWebElement multireports = new QAFExtendedWebElement("nl.multi.reports");
		
		NLDriver.startTransaction("launch");
		NLDriver.getDriver().get("http://ushahidi.demo.neotys.com/");
		NLDriver.stopTransaction();
		
		reports.waitForEnabled(5000);
		NLDriver.getDriver().manage().window().maximize();	
		NLDriver.getDriver().findElements(By.xpath(NLDriver.getLocator(multireports))).get(1).isDisplayed();
		
		NLDriver.startTransaction("alerts_Perfecto");
		NLDriver.getDriver().findElement(By.partialLinkText(NLDriver.getLocator(alerts))).click();
		NLDriver.stopTransaction();
	}
}
