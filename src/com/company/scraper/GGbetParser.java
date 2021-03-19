package com.company.scraper;

import com.company.ElementStruct;
import com.google.common.base.Function;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

public class GGbetParser extends Thread {
    private final WebDriver driver;
    private final String elementClassName = "sportEventRow__body___3Ywcg";
    private List<ElementStruct> elementStructList;

    @Override
    public void run() {
        setPage();
    }

    public GGbetParser() {
        System.setProperty("webdriver.chrome.driver", ".\\src\\com\\driver\\chromedriver.exe");
        this.driver = new ChromeDriver();
    }

    private void setPage(){

        String url = "https://ggbet.ru/esports/next-to-go?dateTo=2021-03-20T06%3A19%3A30.638Z&sportIds[]=esports_counter_strike&sportIds[]=esports_dota_2&tournamentIds[]=gin%3Ac1009dbc-d20c-4ca4-a341-dfc3954d2222&tournamentIds[]=gin%3A4616ac93-d0f4-47b3-a1ab-78137eecc942&tournamentIds[]=gin%3A4e8e9387-f072-4911-809b-ce1b2359387e&tournamentIds[]=gin%3A071deac6-7666-4673-964a-17174de4c44f&tournamentIds[]=gin%3A14f59bf7-54b7-45d0-9da8-fe195b443387&tournamentIds[]=gin%3A8135a945-d650-431a-a76c-d78dd2e3eba1&tournamentIds[]=gin%3Ad45849be-6678-4fc4-acc1-ac56ecd7efed&tournamentIds[]=gin%3Aedcd2f96-e283-44e8-b337-f60fd8c9ba96&tournamentIds[]=gin%3A2817ff2c-c167-4cda-9c52-17fdc497674c&tournamentIds[]=gin%3A80f22879-47af-49ff-814a-acd8e3f11e32&tournamentIds[]=gin%3Acefb7efd-1344-486f-8585-5d2d0f635faf&tournamentIds[]=gin%3Ad649fb37-993f-4917-a932-11793e0db66d&tournamentIds[]=gin%3Adbd3e532-751d-4493-865d-94fedcf6bc3a&tournamentIds[]=gin%3Aef5f6985-e9a8-4663-963f-7b22e4ea6de6&tournamentIds[]=gin%3Aff86f2d5-0caf-4d41-862b-218b3edce57d&tournamentIds[]=gin%3A6fa6366f-1bd6-434d-b44a-7d0233da5bad&tournamentIds[]=gin%3A751c5787-8b48-48c5-9cac-b18718a503f2&tournamentIds[]=gin%3A8b256811-a21e-4da9-adcf-ab340d272d42&tournamentIds[]=gin%3Aa18a7b2c-edea-4a64-8863-27a7d9e0fe48&tournamentIds[]=gin%3Aa7c84844-ab24-4035-a329-16f6b6ff908b&tournamentIds[]=gin%3Acf99a870-7a1c-47a2-87f7-62218db9e5ba";
        this.driver.get(url);
        waitPageLoad();
        scrollDown();

        System.out.println("ggbets " + findElements().size());

        this.elementStructList = listOfStructs(findElements());
        driver.quit();
    }

    private void waitPageLoad(){
        Wait<WebDriver> wait = new FluentWait<>(this.driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);

        wait.until((Function<WebDriver, WebElement>) driver -> {
            assert driver != null;
            return driver.findElement(By.className(elementClassName));
        });
    }

    private void scrollDown(){
        try {
            long lastHeight = (long) ((JavascriptExecutor) this.driver).executeScript("return document.body.scrollHeight");

            while (true) {
                ((JavascriptExecutor) this.driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
                Thread.sleep(2000);

                long newHeight = (long) ((JavascriptExecutor) this.driver).executeScript("return document.body.scrollHeight");
                if (newHeight == lastHeight) {
                    break;
                }
                lastHeight = newHeight;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<WebElement> findElements(){
        return driver.findElements(By.className(this.elementClassName));
    }


    private List<ElementStruct> listOfStructs(List<WebElement> elements){
//        Add a list of structs
        List<ElementStruct> resultList = new LinkedList<>();
//        The loop for get all events in struct
        for (WebElement element : elements){
            resultList.add(elementStruct(element));
        }
        return resultList;
    }

    private ElementStruct elementStruct(WebElement element){
        ElementStruct elementStruct = new ElementStruct();
//        Set Data from Element
        elementStruct.setData(element.findElement(By.className("__app-DateTime-time")).getText());
//        Set name of Team1
        elementStruct.setTeam1(element.findElements(By.className("__app-LogoTitle-name")).get(0).getText());
//        Set name of Team2
        elementStruct.setTeam2(element.findElements(By.className("__app-LogoTitle-name")).get(1).getText());
//        Set coefficient on Team1
        try{
            elementStruct.setX1(Float.parseFloat(element.findElements( By.className("odd__ellipsis___3b4Yk")).get(0).getText()));
            elementStruct.setX2(Float.parseFloat(element.findElements( By.className("odd__ellipsis___3b4Yk")).get(1).getText()));
        } catch(Exception e){
            elementStruct.setX1(0);
            elementStruct.setX2(0);
        }
//        elementStruct.setX1(Float.parseFloat(element.findElements( By.className("odd__ellipsis___3b4Yk")).get(0).getText()));
////        Set coefficient on Team2
//        elementStruct.setX2(Float.parseFloat(element.findElements( By.className("odd__ellipsis___3b4Yk")).get(1).getText()));

        return elementStruct;
    }

    public List<ElementStruct> getElementStructList(){
        return this.elementStructList;
    }
}
