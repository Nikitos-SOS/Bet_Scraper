package com.company.scraper;

import com.company.ElementStruct;
import com.google.common.base.Function;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;


public class ParimatchParser extends Thread {
    private final WebDriver driver;
    private final String elementClassName = "_3i6j5pH655bYkz5944HSdq";
    private List<ElementStruct> elementStructList;

    @Override
    public void run() {
        setPage();
    }

    public ParimatchParser(){
//        Connection of the chrome driver
        System.setProperty("webdriver.chrome.driver", ".\\src\\com\\driver\\chromedriver.exe");
        this.driver = new ChromeDriver();
//        setPage();
    }

    private void setPage(){
//        Connect to website by URL
//        String URL = "https://www.parimatch.ru/ru/prematch-championship/1%7CCS%7Ce1964405daba46d99da77ba5f5046b57%7C7523620fe3a94815bf69c92cad317102,1%7CCS%7Ce1964405daba46d99da77ba5f5046b57%7C95a9f3bf5f1140d0847872c8aba1f578,1%7CCS%7Ce1964405daba46d99da77ba5f5046b57%7C91dc0f4f437145698df10a27b7cd1e48,1%7CCS%7Ce1964405daba46d99da77ba5f5046b57%7Cb524114bde474ee69ab8fd6ecc000f48,1%7CCS%7Ce1964405daba46d99da77ba5f5046b57%7C5963ac97b33544ab8d63b1a8ec1c12be,1%7CCS%7Ce1964405daba46d99da77ba5f5046b57%7C12d316aacd8a41308bb07071f425b332,1%7CCS%7Ce1964405daba46d99da77ba5f5046b57%7C3be44e9be9a242a38c14628ee02d4c1f,1%7CCS%7Ce1964405daba46d99da77ba5f5046b57%7C6f80a2ae0ba54e26afde6de70b8924bd,1%7CCS%7Ce1964405daba46d99da77ba5f5046b57%7Cf7760904a4ea453191d64a2e4abc9b93,1%7CCS%7Ce1964405daba46d99da77ba5f5046b57%7Cf4d2c7673e8945e485b83483a7ea1e81,1%7CCS%7Ce1964405daba46d99da77ba5f5046b57%7C7e6051fcc59b4ba3b9de9339722506d2,1%7CCS%7Ce1964405daba46d99da77ba5f5046b57%7C683fd3aa98e649c8a027b8867c5b0a38,1%7CCS%7Ce1964405daba46d99da77ba5f5046b57%7Ca933a8989447485187ff34c6dba5a63a,1%7CCS%7Ce1964405daba46d99da77ba5f5046b57%7C783d89e78cd644fa953fc5afd7acda3c,1%7CCS%7Ce1964405daba46d99da77ba5f5046b57%7C08b5aa7b23d949e3a9d4ae1940b937ef,1%7CCS%7Ce1964405daba46d99da77ba5f5046b57%7C14baab25fc914f269a2ef865e9bdac3d,1%7CCS%7Ce1964405daba46d99da77ba5f5046b57%7C8296815774824c13ae74b6aa9157f780";
        String URL = "https://www.parimatch.ru/ru/e-sports";
        this.driver.get(URL);
//        Wait for page load
       waitPageLoad();
//       Scroll down the page to get all list of events
       scrollDown();
//       Print the amount of target elements
        System.out.println("parimatch " + findElements().size());

//        Get structed elements
        this.elementStructList = listOfStructs(findElements());

        driver.quit();
    }

    private void waitPageLoad(){
//        Awaiting rules
        Wait<WebDriver> wait = new FluentWait<>(this.driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);

//        Wait until target element is ready
        wait.until((Function<WebDriver, WebElement>) driver -> {
            assert driver != null;
            return driver.findElement(By.className(elementClassName));
        });
    }

    private void scrollDown(){
        try {
//            Current height of doc
            long lastHeight = (long) ((JavascriptExecutor) this.driver).executeScript("return document.body.scrollHeight");

            while (true) {
//                Scroll down the page
                ((JavascriptExecutor) this.driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
//                Wait until the page is load
                Thread.sleep(2000);
//                Write down new height of doc
                long newHeight = (long) ((JavascriptExecutor) this.driver).executeScript("return document.body.scrollHeight");
//                Check heights
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
//        Find target elements
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
        elementStruct.setData(element.findElement(By.className("_30aNOe5fOsKvCNDWBpBYDv")).getText());
//        Set name of Team1
        elementStruct.setTeam1(element.findElements(By.className("_2-4kPKVpNrNoq_0Ylv6TFX")).get(0).getText());
//        Set name of Team2
        elementStruct.setTeam2(element.findElements(By.className("_2-4kPKVpNrNoq_0Ylv6TFX")).get(1).getText());
//        Set coefficient on Team1
        elementStruct.setX1(Float.parseFloat(element.findElements( By.className("_1TBLfOVfJx5AZbnqsME6Y5")).get(0).getText()));
//        Set coefficient on Team2
        elementStruct.setX2(Float.parseFloat(element.findElements( By.className("_1TBLfOVfJx5AZbnqsME6Y5")).get(1).getText()));

        return elementStruct;
    }

    public List<ElementStruct> getElementStructList(){
        return this.elementStructList;
    }
}
