package model;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Farmer extends User {
    private ArrayList<Product> productList;

    public Farmer(int id, String name, String email){
        super(id,name,email);
    }

    // it shows the details of the project
    @Override
    public void showDetails() {
        System.out.println("Name: " + this.getName() + " ID: " + this.getId() + " Email: " + this.getEmail());
    }
    // it gives the number of the farmers exists
    public int getNumOfFarmers() {
        int length = 0;
        try (Scanner input = new Scanner(new File("farmer.txt"))) {
            while (input.hasNext()) {
                length++;
                input.nextLine();
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        return length;
    }

    // gives the list of the product
    public ArrayList<Product> getProductList() {
        return productList;
    }
    // adds a product to a specific farmer
    public void saveFarmerProduct(Product product){
            try(FileWriter writer =new FileWriter("product.txt",true)){
                writer.write(product.toFileString()+"\n");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

    }

    // returns the number of products exist in a specific farmer
    public int getNumOfFarmerProducts() {
        int length = 0;
        try {
            Scanner input = new Scanner(new File("product.txt"));
            while (input.hasNext()) {
                length++;
                input.nextLine();
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        return length;
    }
    // registers a farmer to the txt file
    public void addFarmerToFile(Farmer farmer){
        try(FileWriter writer =new FileWriter("farmer.txt",true)){
            writer.write(farmer.toString()+"\n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addProduct(Product product){
        productList.add(product);
    }

    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
    }



}
