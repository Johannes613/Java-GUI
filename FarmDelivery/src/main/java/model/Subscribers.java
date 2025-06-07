package model;
public class Subscribers {
    private String name;
    private  String email;
    private String farmer;
    private String prodCategory;
    private String subPlan;
    private int cardNum;
    private int cvcNum;
    public Subscribers(String name,String email,String farmer,String prodCategory,String subPlan,int cardNum,int cvcNum){
        this.cardNum=cardNum;
        this.cvcNum=cvcNum;
        this.email=email;
        this.farmer=farmer;
        this.subPlan=subPlan;
        this.prodCategory=prodCategory;
        this.name=name;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public int getCardNum() {
        return cardNum;
    }
    public int getCvcNum() {
        return cvcNum;
    }
    public String getFarmer() {
        return farmer;
    }
    public String getProdCategory() {
        return prodCategory;
    }
    public String getSubPlan() {
        return subPlan;
    }
    public String toString(){
        //        Alice Johnson,alice@example.com,Organic Tomatoes,Monthly
        return name+","+email+","+prodCategory+","+subPlan;
    }
}