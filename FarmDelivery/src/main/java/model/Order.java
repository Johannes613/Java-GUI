package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Order {
    private  String customerName;
    private Product product;
    private int orderId;
    private int productId;
    private int quantity;
    private String deliveryAddress;
    private String orderStatus;
    public Order(int orderId, int productId, int quantity, String deliveryAddress, String orderStatus) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.deliveryAddress = deliveryAddress;
        this.orderStatus = orderStatus;
    }
    public Order(int orderId, int productId, int quantity,String customerName,Product product) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.customerName = customerName;
        this.product = product;

    }
    public int getOrderId() {
        return orderId;
    }

    public Product getProduct() {
        return product;
    }
    public int getProductId() {
        return productId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    private String[] getCurrentUser() {
        try (BufferedReader bfr = new BufferedReader(new FileReader("current_user.txt"))) {
            String currentUser = bfr.readLine();
            if (currentUser == null || currentUser.isEmpty()) {
                return null;
            }
            return currentUser.split(",");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private Product parseOrder(String line, String userName, String password) {
        String[] parameters = line.split(",");
        if (parameters.length < 6) return null;

        String userNameFromOrder = parameters[0];
        String passwordFromOrder = parameters[1];

        if (userName.equalsIgnoreCase(userNameFromOrder) &&
                password.equalsIgnoreCase(passwordFromOrder)) {
            int prdId = Integer.parseInt(parameters[2]);
            String prdName = parameters[3];
            double prdPrice = Double.parseDouble(parameters[4]);
            String imgUrl = parameters[5];
            return new Product(prdId, prdName, prdPrice, imgUrl);
        }

        return null;
    }
    public void fetchOrder() {
        System.out.println("fetching orders started.....");
        try (
                BufferedReader reader = new BufferedReader(new FileReader("orders.txt"))
        ) {
            String[] currentUser = getCurrentUser();
            if (currentUser == null) {
                System.out.println("No current user found.");
                return;
            }

            String userName = currentUser[0];
            String password = currentUser[1];

            String line;
            System.out.println("fetching orders....");
            while ((line = reader.readLine()) != null) {
                Product product = parseOrder(line, userName, password);
                if (product != null) {
                    // Do something with the product (e.g., add to a list or display)
                    System.out.println("Matched product: " + product.getName());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
