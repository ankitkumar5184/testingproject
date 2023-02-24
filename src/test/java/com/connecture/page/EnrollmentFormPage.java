package com.connecture.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.connecture.base.BaseUIPage;
import org.openqa.selenium.Keys;
import org.testng.Assert;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class EnrollmentFormPage extends BaseUIPage {

    // Enrollment form page URL's come from the contrsuctor
    public final String CURRENT_PAGE_URL;

    public EnrollmentFormPage(String url){
        CURRENT_PAGE_URL = url;
    }

    // Locators
    public SelenideElement miniCartArea = $("vnext-mini-cart");
    public SelenideElement miniCartTotalArea = $(".rounded-bottom");


    public SelenideElement firstNameTextBox = $("input[name='ApplicantFirstName']");
    public SelenideElement lastNameTextBox = $("input[name='ApplicantLastName']");
    public SelenideElement dobTextBox = $("input[name='ApplicantBirthDate']");
    public SelenideElement phoneTextBox = $("input[name='ApplicantPhone_hopetemp']");
    public SelenideElement cityTextbox = $("input[name='ApplicantCity']");
    public SelenideElement zipTextBox = $("input[name='ApplicantZip']");
    public SelenideElement stateSelect = $("select[name='ApplicantState']");
    public SelenideElement applicantAddress1 = $("input[name='ApplicantAddress1']");
    public SelenideElement applicantAddress2 = $("input[name='ApplicantAddress2']");
    public ElementsCollection labels = $$("label");
     public SelenideElement effectiveDateDropDownOpen = $(".DFSpaSelected");
    public SelenideElement medicareClaimNumberTextbox = $("input[name='ApplicantHICN']");
     public SelenideElement checkBoxOther = $("input.SpaReasonCheckbox");
    public SelenideElement sepReasonCode =$("input[name='SEPCMSReasonCODE']");
    public SelenideElement toggleLabelsWithNoText = $(".DFSpaToggleButton");




    public void validatePageLoaded(){
        webdriver().shouldHave(urlContaining(CURRENT_PAGE_URL));
        waitForOverlay();
    }

    public void fillOutContactInfo(String first, String last, String dob, String phone, String addressLine1,String addressLine2){
        firstNameTextBox.sendKeys(first);
        lastNameTextBox.sendKeys(last);
        dobTextBox.sendKeys(dob);
        phoneTextBox.sendKeys(phone);
        applicantAddress1.sendKeys(addressLine1);
        applicantAddress2.sendKeys(addressLine2);

    }
    public void validateMonthlyPremium(String totalMonthlyPremium ){
        miniCartArea.shouldHave(text(totalMonthlyPremium));
    }

    public void enterMediCareNumber(String medicareNumber){
        medicareClaimNumberTextbox.sendKeys(medicareNumber);
    }

    public void selectEffectiveDate(){
        effectiveDateDropDownOpen.click();
        $("div.DFSpaSelectItems").find("div:nth-child(2)").click();
    }

    public void selectAndEnterReasonSEP(String sepReason){
        checkBoxOther.sendKeys(Keys.SPACE);
        sepReasonCode.sendKeys(sepReason);
    }

    public void selectOptionForQuestion(String questionOption){
        toggleLabelsWithNoText.$$("label").find(text((questionOption))).click();
    }

    public void validateCityStateZip(String city,String state,String zip){
        String cityText = cityTextbox.getValue();
        Assert.assertEquals(cityText,city);
        String zipText = zipTextBox.getValue();
        Assert.assertEquals(zipText,zip);
        String stateText = stateSelect.getSelectedValue();
        Assert.assertEquals(stateText,state);
    }


    public void validateMiniCart(String planData){
        String miniCartAreaText = miniCartArea.getText();
        miniCartAreaText.contains(planData);
    }

    public void validateMiniCartTotal(String total){
        String miniCartTotalAreaText = miniCartTotalArea.getText();
        miniCartTotalAreaText.contains(total);
    }

    public void validateEmptyNameDOBPhone(String fname,String lname,String dob){
        Assert.assertEquals(firstNameTextBox.getValue(),fname);
        Assert.assertEquals(lastNameTextBox.getValue(),lname);
        Assert.assertEquals(dobTextBox.getValue(),dob);
    }

}
