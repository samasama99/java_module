package org.example.item;

public class Item {
    String name;
    double price;

    public Item() {
    }

    public Item(String name, double price) {
        this.name = name;
        this.price = price;
    }

    // per between 0 and 1
    public double calculateAfterDiscountPrice(double per) {
        return price - (price * per);
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
