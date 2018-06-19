package homework_5;


import homework_5.model.ProductData;
import homework_5.utils.DataConverter;
import homework_5.utils.logging.CustomReporter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.Random;

/**
 * Contains main script actions that may be used in scripts.
 */
public class GeneralActions {
    private WebDriver driver;
    private WebDriverWait wait;
    private By mobileMenu = By.id("_mobile_cart");


    public boolean isMobileVersion(){
        return driver.findElement(mobileMenu).isDisplayed();
    }

    public GeneralActions(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 30);
    }

    public void openRandomProduct() {
        CustomReporter.logAction("Opened random product");
        waitForContentLoad(By.xpath("//a[@class='all-product-link pull-xs-left pull-md-right h4']")).click();
        waitForContentLoad(By.xpath("//article[3]//h1[@class='h3 product-title']")).click();
    }

    private static final int NUMBER_LENGTH = 13;
    public static String getRandomNumber() {
        String s = "12345678910";
        StringBuffer number = new StringBuffer();

        for (int i = 0; i < NUMBER_LENGTH; i++) {
            number.append(s.charAt(new Random().nextInt(s.length())));
        }
        return number.toString();
    }
    /**
     * Extracts product information from opened product details page.
     *
     * @return
     */
    public ProductData getOpenedProductInfo() {
        CustomReporter.logAction("Get information about opened product");
        WebElement productNameElement = driver.findElement(By.xpath("//h1[@itemprop='name']"));
        String productName = productNameElement.getText();
        WebElement productQuantityElement = driver.findElement(By.xpath("//input[@class='input-group form-control']"));
        String productQuantity = productQuantityElement.getAttribute("value");
        WebElement productPriceElement = driver.findElement(By.xpath("//span[@itemprop='price']"));
        String productPrice = productPriceElement.getText();
        return new ProductData(productName, DataConverter.parseStockValue(productQuantity), DataConverter.parsePriceValue(productPrice));
    }
    private WebElement waitForContentLoad(By by) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }
}
