package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Customer {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String deliveryAddress;
    private List<Product> shoppingCart;

    public Customer() {
        this.id = generateId();
        this.shoppingCart = new ArrayList<>();
    }


    public Customer(String name, String email, String phone, String deliveryAddress) {
        this.id = generateId();
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.deliveryAddress = deliveryAddress;
        this.shoppingCart = new ArrayList<>();
    }

    // generates a random id to the customer
    private int generateId() {
        Random random = new Random();
        return random.nextInt(9000) + 1000;
    }
    // adds a product to a specific customer
    public void saveCustomerProduct(Product product) {
        try (FileWriter writer = new FileWriter("product.txt", true)) {
            writer.write(product.toFileString() + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // returns the number if products that customer added
    public int getNumOfCustomerProducts() {
        int length = 0;
        try (Scanner input = new Scanner(new File("product.txt"))) {
            while (input.hasNext()) {
                length++;
                input.nextLine();
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        return length;
    }

    // returns the number of customers registered in the txt file
    public int getNumOfCustomers() {
        int length = 0;
        try (Scanner input = new Scanner(new File("customer.txt"))) {
            while (input.hasNext()) {
                length++;
                input.nextLine();
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        return length;
    }

    // registers a customer to a txt file
    public void addCustomerToFile(Customer customer){
        try(FileWriter writer =new FileWriter("customer.txt",true)){
            writer.write(customer.toString()+"\n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addToCart(Product product) {
        shoppingCart.add(product);
    }

    public void removeFromCart(Product product) {
        shoppingCart.remove(product);
    }

    public List<Product> getShoppingCart() {
        return shoppingCart;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", shoppingCart=" + shoppingCart +
                '}';
    }
}
