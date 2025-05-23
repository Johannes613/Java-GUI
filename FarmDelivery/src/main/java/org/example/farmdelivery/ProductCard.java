package org.example.farmdelivery;

public class ProductCard implements Comparable<ProductCard> {
    private String imgUrl;
    private String productName;
    private double productPrice;
    private int productId;
    private int quantity;
    public  ProductCard(String imgUrl,String productName,double productPrice,int productId,int quantity) {
         this.productName=productName;
         this.productPrice=productPrice;
         this.imgUrl=imgUrl;
         this.productId=productId;
    }

    @Override
    public int compareTo(ProductCard o) {
        return (int) (this.productPrice-o.getProductPrice());
    }

    public double getProductPrice() {
        return productPrice;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getProductId() {
        return productId;
    }

    public void increaseQuantity(){
        this.quantity++;
    }
}
