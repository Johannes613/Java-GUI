package model;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
public class Cart {
    private ArrayList<Product> cartItems=new ArrayList<>();
    public Cart(){
        File file=new File("cart.txt");
        try(BufferedReader bf=new BufferedReader(new FileReader(file));
            BufferedReader reader = new BufferedReader(new FileReader("current_user.txt"))) {
            String line;
//            BufferedReader reader = new BufferedReader(new FileReader("current_user.txt"))
            String userCredLine = reader.readLine();
            String[] userCred = userCredLine.split(",");
            while ((line=bf.readLine())!=null){
                String[] productInfo=line.split(",");
                System.out.println(Arrays.toString(productInfo));
                if(productInfo.length == 9 && userCred[0].equals(productInfo[0]) && userCred[1].equals(productInfo[1])){
                    int productId=Integer.parseInt(productInfo[2]);
                    String productName=productInfo[3];
                    double productPrice=Double.parseDouble(productInfo[4]);
                    String imgUrl=productInfo[5];
                    int quantity=Integer.parseInt(productInfo[6]);
                    String prodDesc=productInfo[7];
                    String prodCategory=productInfo[8];
                    boolean found=false;
                    System.out.println(productId);
                    System.out.println(productName);
                    System.out.println(imgUrl);
                    System.out.println(quantity);
                    for (Product pdc:cartItems){
                        if(pdc.getProductId()==productId){
                            found=true;
                            pdc.increaseQuantity();
                            break;
                        }
                    }
                    if(!found){
                        Product product=new Product(productId,productName,prodDesc,productPrice,quantity, LocalDate.now(),imgUrl,"Spring","Milk",quantity);
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
            total+=pdc.getPrice()*pdc.getQuantity();
        }
        return total;
    }
    public void setCartItemsClear(){
        cartItems.clear();
    }
}