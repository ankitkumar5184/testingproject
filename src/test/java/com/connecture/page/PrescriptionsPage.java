package com.connecture.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.connecture.base.BaseUIPage;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class PrescriptionsPage extends BaseUIPage {

    public final String CURRENT_PAGE_URL = "/GuidedHelp/Prescriptions";

    // Locators
    public SelenideElement widgetDrugDosage = $("vnext-drug-dosage");
    public SelenideElement widgetDrugSearch = $("vnext-widget-drug-search");
    public SelenideElement drugTextBox= widgetDrugSearch.$(".drug-search-input");

    public SelenideElement drugSearchResults = widgetDrugSearch.$(".dropdown-item");

    public SelenideElement toggleOptionSelected = $(".toggle-option.Spa_Style_SecondaryButtonSelected");
    public ElementsCollection toggleOptions = $$(".toggle-option");

    public SelenideElement overlaySmoker = $(".overlay");

    public ElementsCollection drugCardList = $$(".drug-card-panel");


    public void validatePageLoaded(){
        webdriver().shouldHave(urlContaining(CURRENT_PAGE_URL));
        waitForOverlay();
    }

    public void searchDrug(String drug){
    drugTextBox.setValue(drug);
    drugSearchResults.shouldHave(text(drug));
    drugSearchResults.click();
    }

    public void userSelectFirstSearchResult(String drugDosage){
        toggleOptions.find(text(drugDosage)).click();
    }

    public void validateSelectedOption(String selectionOption){
        toggleOptionSelected.shouldHave(text(selectionOption));
    }

    public void validateOptionalMinDosage(String minDosage){
        widgetDrugDosage.findAll(".toggle-option").shouldBe(CollectionCondition.sizeGreaterThan(Integer.parseInt(minDosage)));
    }
    public boolean validateOverlaySmoker(){
    return overlaySmoker.isDisplayed();
    }

    public void validateDrugTextBox(){
        drugTextBox.isDisplayed();
    }

    public void validateDrugCardList() {
        drugCardList.shouldBe(CollectionCondition.sizeGreaterThanOrEqual(1));
    }
    public void removeDrugFromList(String drugName){
            drugCardList.find(text(drugName)).find("i").click();
        }

    public void validateDrugNameInDrugCardList(String drugName){
        drugCardList.find(text(drugName)).isDisplayed();
    }

    }