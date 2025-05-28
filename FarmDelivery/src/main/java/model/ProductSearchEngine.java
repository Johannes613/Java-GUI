package model;


import javafx.collections.ObservableList;
import model.Exceptions.OutOfSeasonException;

import java.time.LocalDate;
import java.util.ArrayList;

public class ProductSearchEngine implements IProductSearch {
    public ArrayList<Product> searchBySeason(LocalDate date){
        return new ArrayList<Product>();
    }
    public ArrayList<Product> searchByProximity(int radius){
        return new ArrayList<Product>();
    }
    public ArrayList<Product> searchByCategory(String category){
        return new ArrayList<Product>();
    }

    @Override
    public void addToCart(int productId) {

    }

    @Override
    public ObservableList<Product> filterBySeason(String season) throws OutOfSeasonException {
        return null;
    }
}