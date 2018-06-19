package homework_5.tests;

import homework_5.GeneralActions;
import homework_5.BaseTest;
import homework_5.model.ProductData;
import homework_5.utils.DataConverter;
import homework_5.utils.Properties;
import homework_5.utils.logging.CustomReporter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Objects;

public class PlaceOrderTest extends BaseTest {

    @Test()
    public void checkSiteVersion() {
        CustomReporter.logAction("Main page");
        driver.get(Properties.getBaseUrl());
        Assert.assertEquals(actions.isMobileVersion(), isMobileTesting, "It is desktop version of the site");
    }

    @Test (dependsOnMethods = "checkSiteVersion")
    public void createNewOrder() {
        CustomReporter.logAction("Create new order");
        GeneralActions generalActions = new GeneralActions(driver);
        generalActions.openRandomProduct();
        ProductData productData = generalActions.getOpenedProductInfo();
        waitForContentLoad(By.xpath("//a[@href='#product-details']")).click();
        WebElement productQuantityBeforeSaleElement = waitForContentLoad(By.xpath("//div[@class='product-quantities']/span"));
        int productQuantityBeforeSaleValue = DataConverter.parseStockValue(productQuantityBeforeSaleElement.getAttribute("innerHTML"));
        waitForContentLoad(By.xpath("//button[@class='btn btn-primary add-to-cart']")).click();
        clickOnInvisibleElement(waitForContentLoad(By.xpath("//a[@class='btn btn-primary']")), driver);
        By productNameLocator = By.xpath("//div[@class='product-line-info']//a[@class='label']");
        WebElement productNameElementElement = waitForContentLoad(productNameLocator);
        String productName = productNameElementElement.getText().toUpperCase();
        CustomReporter.logAction("Starting check items in the basket");

        Assert.assertEquals(productName, productData.getName(), "In the basket is incorrectly product name");
        WebElement productPriceElement = driver.findElement(By.xpath("//div[@class='product-line-info'][2]//span"));
        String productPrice = productPriceElement.getText();

        Assert.assertEquals(DataConverter.parsePriceValue(productPrice), productData.getPrice(), "In the basket is incorrectly product price");
        WebElement productQuantityElement = driver.findElement(By.xpath("//div[@class='input-group bootstrap-touchspin']/input"));
        String productQuantity = productQuantityElement.getAttribute("value");

        Assert.assertEquals(DataConverter.parseStockValue(productQuantity), productData.getQty(), "In the basket is incorrectly product quantity");
        CustomReporter.logAction("Ending check items in the basket");
        driver.findElement(By.xpath("//div[@class='text-xs-center']")).click();
        waitForContentLoad(By.name("firstname")).sendKeys("My first name");
        driver.findElement(By.name("lastname")).sendKeys("My last name");
        driver.findElement(By.name("email")).sendKeys("webinar.test"+ GeneralActions.getRandomNumber()+"@gmail.com");
        driver.findElement(By.name("continue")).click();
        waitForContentLoad(By.name("address1")).sendKeys("My address");
        driver.findElement(By.name("postcode")).sendKeys("37276");
        driver.findElement(By.name("city")).sendKeys("City");
        driver.findElement(By.name("confirm-addresses")).click();
        waitForContentLoad(By.name("confirmDeliveryOption")).click();
        waitForContentLoad(By.id("payment-option-1")).click();
        waitForContentLoad(By.xpath("//input[@id='conditions_to_approve[terms-and-conditions]']")).click();
        waitForContentLoad(By.xpath("//div[@class='ps-shown-by-js']//button[@type='submit']")).click();
        WebElement materialIconDoneElement = waitForContentLoad(By.xpath("//i[@class='material-icons done']"));
        CustomReporter.logAction("Starting check the message confirming the order of the goods");
        Assert.assertFalse(Objects.isNull(materialIconDoneElement));
        CustomReporter.logAction("Ending check the message confirming the order of the goods");
        By productNameAfterOrderingLocator = By.xpath("//div[@class='col-sm-4 col-xs-9 details']/span");
        WebElement productNameAfterOrderingElement = waitForContentLoad(productNameAfterOrderingLocator);
        String productNameAfterOrdering = productNameAfterOrderingElement.getText().substring(0, productData.getName().length()).toUpperCase();
        CustomReporter.logAction("Starting check details of the order");

        Assert.assertEquals(productNameAfterOrdering, productData.getName());
        WebElement productPriceAfterOrderingElement = driver.findElement(By.xpath("//div[@class='col-xs-5 text-sm-right text-xs-left']"));
        String productPriceAfterOrdering = productPriceAfterOrderingElement.getText();

        Assert.assertEquals(DataConverter.parsePriceValue(productPriceAfterOrdering), productData.getPrice());
        WebElement productQuantityAfterOrderingElement = driver.findElement(By.xpath("//div[@class='col-xs-2']"));
        String productQuantityAfterOrdering = productQuantityAfterOrderingElement.getText();

        Assert.assertEquals(DataConverter.parseStockValue(productQuantityAfterOrdering), productData.getQty());
        CustomReporter.logAction("Ending check details of the order");
        WebElement serchField = waitForContentLoad(By.xpath("//input[@name='s']"));
        new Actions(driver).moveToElement(serchField).click(serchField).build().perform();
        StringSelection selection = new StringSelection(productData.getName());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        serchField.sendKeys(Keys.BACK_SPACE);
        serchField.sendKeys(Keys.CONTROL + "v");
        serchField.submit();
        waitForContentLoad(By.xpath("//*[@class='h3 product-title']/a")).click();
        waitForContentLoad(By.xpath("//a[@href='#product-details']")).click();
        WebElement productQuantityAfterSaleElement = waitForContentLoad(By.xpath("//div[@class='product-quantities']/span"));
        int productQuantityAfterSaleValue = DataConverter.parseStockValue(productQuantityAfterSaleElement.getAttribute("innerHTML"));
        CustomReporter.logAction("Starting check of reduction of quantity of goods per unit");

        Assert.assertEquals(1, (productQuantityBeforeSaleValue - productQuantityAfterSaleValue));
        CustomReporter.logAction("Ending check of reduction of quantity of goods per unit");
    }

    private WebElement waitForContentLoad(By by) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public static void clickOnInvisibleElement(WebElement element, WebDriver driver) {
        String script = "var object = arguments[0];"
                + "var theEvent = document.createEvent(\"MouseEvent\");"
                + "theEvent.initMouseEvent(\"click\", true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);"
                + "object.dispatchEvent(theEvent);";
        ((JavascriptExecutor)driver).executeScript(script, element);
    }
}