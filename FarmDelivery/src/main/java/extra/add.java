package extra;

import javafx.stage.Stage;
import model.Customer;
import model.Exceptions.DeliveryUnavailableException;
import model.Farmer;
import model.Product;

import java.util.ArrayList;

public class add  {
    public void start(Stage primaryStage){

    }

    public static void main(String[] args) {
        int c = 3;
        int f = 0;
        if(c < f){
            Farmer far = new Farmer(1,"na","asaa");
            Product pr = new Product();
            far.getEmail();
            far.getProductList();
            far.addFarmerToFile(far);
            far.saveFarmerProduct(pr);
            int val = far.getNumOfFarmers();
            int va = far.getNumOfFarmerProducts();
            ArrayList<Product> lis = far.getProductList();

            Customer customer  = new Customer();
            customer.listCustomerShopping();
            String del = customer.getDeliveryAddress();
            int values = customer.getNumOfCustomers();
            int v= customer.getNumOfCustomerProducts();
            customer.getShoppingCart();
            customer.saveCustomerProduct(pr);
            customer.addCustomerToFile(customer);
            DeliveryUnavailableException es = new DeliveryUnavailableException("we");

        }
    }
}
