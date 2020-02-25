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
public class GoogleStepDefs extends NLDriver{
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
		NLDriver.startTransaction(platform + "_home_Perfecto");
		driver.get("http://ushahidi.demo.neotys.com/");
		NLDriver.stopTransaction();
		NLDriver.startTransaction(platform + "_reports_Perfecto");
		driver.findElement(By.xpath("//div[@id='mainmenu']//a[contains(text(),'Reports')]")).click();
		NLDriver.stopTransaction();
		NLDriver.startTransaction(platform + "_submit_Perfecto");
		driver.findElement(By.partialLinkText("SUBMIT")).click();
		NLDriver.stopTransaction();	
	}
	
	@Given("^I am on Neo Alerts$")
	public void I_am_on_Neoload() throws Throwable {
		NLDriver.startTransaction(platform + "_home2_Perfecto");
		driver.get("http://ushahidi.demo.neotys.com/");
		NLDriver.stopTransaction();
		NLDriver.startTransaction(platform + "_alerts_Perfecto");
		driver.findElement(By.partialLinkText("GET ALERTS")).click();
		NLDriver.stopTransaction();
	}
}