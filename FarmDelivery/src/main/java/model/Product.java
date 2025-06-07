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
    private String season;
    private int quantity=1;
    public Product(int productId, String name, String description, double price, int quantityAvailable, LocalDate harvestDate,String imageUrl,String season,String productCategory) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantityAvailable = quantityAvailable;
        this.harvestDate = harvestDate;
        this.imageUrl=imageUrl;
        this.season = season;
        this.productCategory = productCategory;
    }
    public Product(int productId, String name, String description, double price, int quantityAvailable, LocalDate harvestDate,String imageUrl,String season,String productCategory,int quantity) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantityAvailable = quantityAvailable;
        this.harvestDate = harvestDate;
        this.imageUrl=imageUrl;
        this.season = season;
        this.productCategory = productCategory;
        this.quantity = quantity;
    }

    public Product(int productId,String name,double price, String imageUrl){
        quantityAvailable = 1;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
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

    public String getDescription() {
        return description;
    }

    public void setPrice(double price) {
        if(price > 0){
            this.price = price;
        }
        else{
            throw new IllegalArgumentException("Price has to be greater than 0");
        }
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
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

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
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
        return productId + "," + name + "," + price + "," + imageUrl + "," + quantityAvailable + "," + description + "," + harvestDate + "," + season + "," + productCategory;
    }
    //the following methods are important for sorting
    public int compareTo(Product o) {
        return (int) (this.price-o.getPrice());
    }
    public void increaseQuantity(){
        this.quantity++;
    }

}


