package com.connecture.base;

import com.codeborne.selenide.*;
import com.connecture.utils.MoneyUtil;
import com.connecture.utils.exception.ConnectureCustomException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.testng.Assert;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;
import static com.connecture.utils.TestDataProvider.*;

public class BaseUIPage implements PageObjectModel {

    // Navigation, common color config, loading
    public ElementsCollection secondaryButtons = $$(".Spa_Style_SecondaryButton");
    public ElementsCollection secondaryButtonsSelected = $$(".Spa_Style_SecondaryButtonSelected");
    public ElementsCollection primaryButtons = $$(".Spa_Style_PrimaryButton");
    public ElementsCollection primaryLinks = $$(".Spa_Style_Link");
    public SelenideElement overlayLoader = $(".overlay");
    public ElementsCollection labels = $$("label");

    // Platform auth page
    public SelenideElement usernameInput = $("#username");
    public SelenideElement passwordInput = $("#password");
    public SelenideElement submitButton = $("button");

    // Commons items like zip
    public SelenideElement zipCodeInput = $(".demographic-input");
    public SelenideElement countyNameArea = $(".selected-county");
    public ElementsCollection countyNameListItems = $$(".county-dropdown-item");
    public SelenideElement countyNameList = $(".county-dropdown-list");
    public SelenideElement largeZipLink = $(".zip-code-link");
    public ElementsCollection formControls = $$("input").filter(attribute("formcontrolname"));
    public ElementsCollection modalPrimaryButtons= $$(".modal-dialog .Spa_Style_PrimaryButton");

    public SelenideElement modalZipCodeInput= $(".modal-dialog .demographic-input");

    public SelenideElement modalCountyNameArea= $(".modal-dialog .selected-county");

    public ElementsCollection primaryHeadingLinks = $$(".Spa_Style_H3");
    public SelenideElement modalTitle = $(".modal-header");
    public SelenideElement modalBody = $(".modal-content");


    public SelenideElement secondaryButtonWithText = $(".Spa_Style_SecondaryButtonSelected");

    // Page object model
    public void validatePageLoaded() {
        webdriver().shouldHave(urlContaining(CURRENT_PAGE_URL));
        waitForOverlay();
    }

    public void enterZipCode(String zip){
        zipCodeInput.sendKeys(zip);
    }

    public String validateZipCode(){
        return zipCodeInput.getValue();
    }
    public void validateCountyName(String countyName){
        countyNameArea.shouldHave(text(countyName));
    }
    public void validateCountyListItemName(String countyName){
        countyNameList.shouldHave(text(countyName));
    }
    public void selectCountyFromList(String countyName){
        $$(".county-dropdown-item").find(text(countyName)).click();
    }
    public void clearZipCodeField(){
        zipCodeInput.clear();
    }
    public void clickPrimaryButton(String partialText){
        primaryButtons.find(text(partialText)).click();
    }
    public void clickSecondaryButton(String partialText){
        secondaryButtons.find(text(partialText)).click();
    }
    public void clickPrimaryLink(String partialText){
        primaryLinks.find(text(partialText)).click();
    }
    public void waitForPrimaryButton(String partialText){
        primaryButtons.find(text(partialText)).shouldBe(visible);
    }
    public void waitForSecondaryButton(String partialText){
        secondaryButtons.find(text(partialText)).shouldBe(visible);
    }
    public void waitForSecondaryButtonText(String partialText){
        secondaryButtons.find(text(partialText)).shouldHave(text(partialText));

    }
    public void validateCountyDuplicates(String countyName){
        countyNameArea.shouldHave(text(countyName));
    }
    public void validateSecondaryButtonSelected(String exactText){
        secondaryButtonsSelected.find(exactText(exactText)).click();
    }
    public void clickLargeZipCode(){
        largeZipLink.click();
    }
    public void clickPrimaryButtonWithExactText(String givenText){
        primaryButtons.find(exactText(givenText)).click();
    }

    public void doPlatformAuth() {
        System.out.println("Do platform auth");
        usernameInput.shouldBe(visible);
        usernameInput.sendKeys(getPlatformLoginUsername());
        passwordInput.sendKeys(getPlatformLoginPassword());
        usernameInput.shouldHave(value(getPlatformLoginUsername()));
        submitButton.click();
    }



    public void openWithBaseUrlAndAuthenticate() {
        navigateToSiteBaseUrl();
        WebDriverRunner.getWebDriver().manage().window().maximize();
        System.out.println("BaseUrl open: " + Configuration.baseUrl);
        doPlatformAuth();
    }

    public void openWithBaseUrl() {
        navigateToSiteBaseUrl();
        WebDriverRunner.getWebDriver().manage().window().maximize();
        System.out.println("BaseUrl open: " + Configuration.baseUrl);
    }

    public void navigateToSiteBaseUrl() {
        System.out.println("Go to: " + Configuration.baseUrl);
        Selenide.open(Configuration.baseUrl);
        WebDriverRunner.getWebDriver().manage().window().maximize();
    }
    public void waitForOverlay() {
        overlayLoader.shouldBe(hidden);
    }

public void setTextFormControl(String formControlNameAttribute,String firstNameText){
    formControls.find(attribute("formcontrolname",formControlNameAttribute)).setValue(firstNameText);
}
    public void clickLabel(String partialText) {
        labels.find(exactText(partialText)).click();
    }

    public void clickLabelExactText(String exactText) {
        labels.find(exactText(exactText)).click();
    }

    public static BigDecimal convertStringRateToBigDecimal(String rate) throws ConnectureCustomException {
        return MoneyUtil.convertToBigDecimal(rate);
    }

    public void clickModalPrimaryButton(String partialText){
        modalPrimaryButtons.find(text(partialText)).click();
    }

    public void clickPrimaryHeadingLink(String partialText){
        primaryHeadingLinks.find(text(partialText)).click();
    }

    public void validateModalTitle(String modalText){
        modalTitle.shouldHave(text(modalText));
    }

    public void validateModalBody(String modalBodyText){
        modalBody.shouldHave(text(modalBodyText));
    }

    public void validateZipCode(String zip) {
        String zipCodeValue = zipCodeInput.getValue();
        Assert.assertEquals(zipCodeValue, zip);
    }

    public void enterZipCodeInModalWindow(String zipCode){
        modalZipCodeInput.sendKeys(zipCode);
    }

    public void validateCountyNameInModalWindow(String zipCode){
        modalCountyNameArea.shouldHave(text(zipCode));
    }


    public boolean validateSecondaryButtonSelectedDisplayed(String partialText){
        return secondaryButtonsSelected.find(text(partialText)).isDisplayed();

    }

    public File findFile(String partialName){
        File[] list = new File("./build/downloads").listFiles();
        for (File file : list)
        {
            try{  System.out.println("Found file: " + file.getName()); } catch(Throwable t){ }
            if( file != null &&
                    file.getName().contains(partialName) &&
                    file.length() > 500 &&
                    !file.getName().contains(".crdownload") ) {
                return file;
            }
        }
        return null;
    }

    public void validateDownloadedPDF(File downloadedFile, String[] dataItems) throws IOException {
        System.out.println("PDF File validated: " + downloadedFile.getName());
        InputStream resource = new FileInputStream(downloadedFile);
        PDDocument document = PDDocument.load( resource );
        PDFTextStripper stripper = new PDFTextStripper();
        String parsedText = stripper.getText(document);
        //assert document.getNumberOfPages() >= 2; // Sanity, should always see 2+ pages, can adjust for short forms later
        parsedText = parsedText.replace("\u00a0"," "); // Fix spaces ( the PDF uses non-breaking spaces too many places )
        parsedText = parsedText.replace("\u00ad","-"); // Fix dashes ( the PDF use a small dash )
        document.close();
        resource.close();
        for ( String dataItem : dataItems){
                try{
                    assert parsedText.contains(dataItem);
                } catch(Throwable t){
                    System.out.println("Failed to validate: " + dataItem.toString());
                    throw t;
                }
        }
    }

}