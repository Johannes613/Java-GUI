package model;
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

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
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
