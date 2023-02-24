package com.connecture.page;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.impl.CollectionElement;
import com.connecture.base.BaseUIPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class DashboardPage extends BaseUIPage {

    public final String CURRENT_PAGE_URL = "/Dashboard";

    public void validatePageLoaded(){
        webdriver().shouldHave(urlContaining(CURRENT_PAGE_URL));
        waitForOverlay();
    }

    // Locators
    public SelenideElement dashBoardPageHeading = $(".Spa_Style_H1");

    public SelenideElement profileNavigationBar = $("vnext-profile-navigation-bar");


    public void selectPlans(String plans){
        profileNavigationBar.$$("span").find(text(plans)).click();
    }


}
