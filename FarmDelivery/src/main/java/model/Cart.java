package model;

import org.example.farmdelivery.ProductCard;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Cart {
    private ArrayList<ProductCard> cartItems=new ArrayList<>();
    public Cart(){
        File file=new File("cart.txt");
        try(BufferedReader bf=new BufferedReader(new FileReader(file))) {
            String line;
            while ((line=bf.readLine())!=null){
               String[] productInfo=line.split(",");
                System.out.println(Arrays.toString(productInfo));
                if(productInfo.length == 5){
                    int productId=Integer.parseInt(productInfo[0]);
                    String productName=productInfo[1];
                    double productPrice=Double.parseDouble(productInfo[2]);
                    String imgUrl=productInfo[3];
                    int quantity=Integer.parseInt(productInfo[4]);
                    boolean found=false;
                    System.out.println(productId);
                    System.out.println(productName);
                    System.out.println(imgUrl);
                    System.out.println(quantity);
                    ProductCard productCard=new ProductCard(imgUrl,productName,productPrice,productId,quantity);
                    for (ProductCard pdc:cartItems){
                        if(pdc.getProductId()==productId){
                            found=true;
                            pdc.increaseQuantity();
                            break;
                        }
                    }
                    if(!found){
                        cartItems.add(productCard);
                    }
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }

    }
    public ArrayList<ProductCard> getCartItems() {
        return cartItems;
    }
}
