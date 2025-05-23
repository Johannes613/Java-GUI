package model;

//import org.example.farmdelivery.ProductCard;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class Cart {
    private ArrayList<Product> cartItems=new ArrayList<>();
    public Cart(){
        File file=new File("cart.txt");
        try(BufferedReader bf=new BufferedReader(new FileReader(file))) {
            String line;
            while ((line=bf.readLine())!=null){
               String[] productInfo=line.split(",");
                System.out.println(Arrays.toString(productInfo));
                if(productInfo.length == 7){
                    int productId=Integer.parseInt(productInfo[0]);
                    String productName=productInfo[1];
                    double productPrice=Double.parseDouble(productInfo[2]);
                    String imgUrl=productInfo[3];
                    int quantity=Integer.parseInt(productInfo[4]);
                    String prodDesc=productInfo[5];
                    String prodCategory=productInfo[6];
                    boolean found=false;
                    System.out.println(productId);
                    System.out.println(productName);
                    System.out.println(imgUrl);
                    System.out.println(quantity);
                    Product product=new Product(productId,productName,prodDesc,productPrice,quantity, LocalDate.now(),imgUrl,prodCategory);
//                    ProductCard productCard=new ProductCard(imgUrl,productName,productPrice,productId,quantity,"Milk");
                    for (Product pdc:cartItems){
                        if(pdc.getProductId()==productId){
                            found=true;
                            pdc.increaseQuantity();
                            break;
                        }
                    }
                    if(!found){
                        cartItems.add(product);
                    }
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }

    }
    public ArrayList<Product> getCartItems() {
        return cartItems;
    }

    public double getTotalPrice(){
        double total=0;
        for(Product pdc:cartItems){
            total+=pdc.getPrice()*pdc.getQuantityAvailable();
        }
        return total;
    }
}
