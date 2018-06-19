package homework_5.model;

import java.util.Random;

/**
 * Hold Product information that is used among tests.
 */
public class ProductData {
    private String name;
    private int qty;
    private float price;

    public ProductData(String name, int qty, float price) {
        this.name = name;
        this.qty = qty;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getQty() {
        return qty;
    }

    public float getPrice() {
        return price;
    }

}
