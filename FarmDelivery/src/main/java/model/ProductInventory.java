package model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.farmdelivery.ProductCard;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class ProductInventory {

//    private ArrayList<ProductCard> products=new ArrayList<>();
    ObservableList<ProductCard> observableProducts = FXCollections.observableArrayList();
    private String[] urls={"/org/example/farmdelivery/images/img2.jpeg","/org/example/farmdelivery/images/img3.jpeg",
            "/org/example/farmdelivery/images/img4.jpeg","/org/example/farmdelivery/images/img5.jpeg","/org/example/farmdelivery/images/img6.jpeg","/org/example/farmdelivery/images/img2.jpeg"};

    public ProductInventory(){
       for(int i=0;i<urls.length;i++){
           ProductCard pdc=new ProductCard(urls[i],"Mango",(20+i),(i+1),1);
           observableProducts.add(pdc);
       }
    }


    public ObservableList<ProductCard> getProducts() {
        return observableProducts;
    }

//    public ArrayList<Product> getProductInventory() {
//    }
//    this method saves cart product into cart.txt
    public void addToCart(int productId){
        File file=new File("cart.txt");
        for(ProductCard pdc:observableProducts){
            if(pdc.getProductId()==productId){
                try{
                    FileWriter fileWriter=new FileWriter(file,true);
                    System.out.println("written");
                    fileWriter.write(productId+","+pdc.getProductName()+","+pdc.getProductPrice()+","+pdc.getImgUrl()+","+pdc.getQuantity()+"\n");
                    fileWriter.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
