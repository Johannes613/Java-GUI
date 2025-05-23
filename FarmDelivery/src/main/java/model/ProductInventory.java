package model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ProductInventory implements IProductSearch {
//    private ArrayList<ProductCard> products=new ArrayList<>();
    ObservableList<Product> observableProducts = FXCollections.observableArrayList();
    ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
    ObservableList<Product> searchedProducts=FXCollections.observableArrayList();
    private String[] prodCategory={"All", "Milk Products", "Fruits", "Vegetables", "Cereal Products","Cereal"};
    private String[] urls={"/org/example/farmdelivery/images/img2.jpeg","/org/example/farmdelivery/images/img3.jpeg",
            "/org/example/farmdelivery/images/img4.jpeg","/org/example/farmdelivery/images/img5.jpeg","/org/example/farmdelivery/images/img6.jpeg","/org/example/farmdelivery/images/img2.jpeg"};
    private String[] fruits = {"Apple", "Banana", "Mango", "Orange", "Strawberry", "Grapes"};
    public ProductInventory(){
       for(int i=0;i<urls.length;i++){
           Product pdc=new Product((i+1),fruits[i],"Amazing",(20+i),1,LocalDate.now(),urls[i],prodCategory[i]);
           observableProducts.add(pdc);
       }
    }

    public ObservableList<Product> getProducts() {
        return observableProducts;
    }

    @Override
    public ArrayList<Product> searchBySeason(LocalDate date) {
        return null;
    }

    @Override
    public ArrayList<Product> searchByProximity(int radius) {
        return null;
    }

    @Override
    public ArrayList<Product> searchByCategory(String category) {
        return null;
    }

    @Override
//    this method saves cart product into cart.txt
    public void addToCart(int productId){
        File file=new File("cart.txt");
        for(Product pdc:observableProducts){
            if(pdc.getProductId()==productId){
                try{
                    FileWriter fileWriter=new FileWriter(file,true);
                    System.out.println("written");
                    fileWriter.write(productId+","+pdc.getName()+","+pdc.getPrice()+","+pdc.getImageUrl()+","+pdc.getQuantityAvailable()+","+pdc.getDescription()+","+pdc.getProductCategory()+"\n");
                    fileWriter.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    //filter product based on category
    public ObservableList<Product> getFilteredProducts(String category){
        filteredProducts.clear();
        if(category.equalsIgnoreCase("all")){
            return observableProducts;
        }else{
            for(Product pdc:observableProducts){
                if(pdc.getProductCategory().equalsIgnoreCase(category)){
                    filteredProducts.add(pdc);
                }
            }
        }
        return filteredProducts;
    }

    public ObservableList<Product> searchProducts(String query) {
        searchedProducts.clear();
        for(Product pd:observableProducts){
            if(pd.getName().toLowerCase().contains(query.toLowerCase())){
                searchedProducts.add(pd);
            }
        }
        if(!searchedProducts.isEmpty()){
            return searchedProducts;
        }else{
            return null;
        }

    }

}
