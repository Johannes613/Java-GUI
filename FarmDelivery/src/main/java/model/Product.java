package model;

import model.Exceptions.HarvestDateException;


import java.time.LocalDate;
import java.util.Date;
public class Product implements Comparable<Product>{

    private int productId;
    private String name;
    private String description;
    private double price;
    private int quantityAvailable;
    private String imageUrl;
    private LocalDate harvestDate;
    private String productCategory;

    public Product(int productId, String name, String description, double price, int quantityAvailable, LocalDate harvestDate,String imageUrl,String productCategory){
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantityAvailable = quantityAvailable;
        this.harvestDate = harvestDate;
        this.imageUrl=imageUrl;
        this.productCategory=productCategory;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getProductId() {
        return productId;
    }
    public void setProductId(int productId){
        this.productId = productId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        if(price > 0){
            this.price = price;
        }
        else{
            throw new IllegalArgumentException("Price has to be greater than 0");
        }
    }

    public String getDescription() {
        return description;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public LocalDate getHarvestDate() {
        return harvestDate;
    }

    public void setHarvestDate(LocalDate harvestDate) throws HarvestDateException {
        if(harvestDate.isAfter(LocalDate.now())){
            this.harvestDate = harvestDate;
        }
        else{
            throw new HarvestDateException("Harvest date has to be after today(in the future)");
        }
    }
    public String toFileString() {
        return productId + "," + name + "," + description + "," + price + "," + quantityAvailable + "," + harvestDate + "," + imageUrl +  "\n";
    }
    //the following methods are important
    public int compareTo(Product o) {
        return (int) (this.price-o.getPrice());
    }
    public void increaseQuantity(){
        this.quantityAvailable++;
    }
}

