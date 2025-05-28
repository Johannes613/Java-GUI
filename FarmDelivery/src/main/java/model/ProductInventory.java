package model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Exceptions.OutOfSeasonException;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class ProductInventory implements IProductSearch {
    ObservableList<Product> observableProducts = FXCollections.observableArrayList();
    ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
    ObservableList<Product> searchedProducts=FXCollections.observableArrayList();
    public ProductInventory(){
        addProductToInventory();
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
                try(BufferedReader reader = new BufferedReader(new FileReader("current_user.txt"))){
                                String line = reader.readLine();
            String[] userCred = line.split(",");
                    FileWriter fileWriter=new FileWriter(file,true);
                    System.out.println(Arrays.toString(userCred));
                    System.out.println("written");
                    fileWriter.write(userCred[0].trim() + "," + userCred[1].trim() + "," +productId+","+pdc.getName()+","+pdc.getPrice()+","+pdc.getImageUrl()+","+pdc.getQuantity()+","+pdc.getDescription()+","+pdc.getProductCategory()+"\n");
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
    //filter product based on seasonality
    public ObservableList<Product> filterBySeason(String season) throws OutOfSeasonException {
        filteredProducts.clear();
        boolean found = false;
        if(season.equalsIgnoreCase("all seasons")){
            return observableProducts;
        }else{
            for(Product pdc:observableProducts){
                if(pdc.getSeason().equalsIgnoreCase(season)){
                    filteredProducts.add(pdc);
                    found = true;
                }
            }
        }
        if(!found){
            throw new OutOfSeasonException("No products available for the selected season: " + season);
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

    //a method that retrieves the product from product.txt file and create
    //a product object and add it to the observableProducts list
    public void addProductToInventory(){
        //read products from file called product.txt
        File file = new File("product.txt");
        System.out.println("Reading products from file: " + file.getAbsolutePath());
        try(BufferedReader bf=new BufferedReader(new FileReader(file))){
            String line;
            while((line=bf.readLine())!=null){
                String[] productInfo=line.split(",");
                if(productInfo.length==9){
                    int productId=Integer.parseInt(productInfo[0]);
                    System.out.println("Product ID: " + productId);

                    String productName=productInfo[1];
                    String productDesc=productInfo[5];
                    double productPrice=Double.parseDouble(productInfo[2]);
                    int quantityAvailable=Integer.parseInt(productInfo[4]);
                    LocalDate harvestDate=LocalDate.parse(productInfo[6]);
                    String imageUrl=productInfo[3];
                    String season=productInfo[7];
                    String category=productInfo[8];
                    Product pdc=new Product(productId,productName,productDesc,productPrice,quantityAvailable,harvestDate,imageUrl,season,category);
                    observableProducts.add(pdc);
                }
            }
        }catch (IOException e){
            System.out.println("Something happen while reading product.txt in ProductInventory");
            System.out.println(e.getMessage());
        }
    }

}
