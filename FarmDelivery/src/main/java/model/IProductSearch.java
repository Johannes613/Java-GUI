package model;

import javafx.collections.ObservableList;
import model.Exceptions.OutOfSeasonException;

import java.time.LocalDate;
import java.util.ArrayList;
public interface IProductSearch {
    ArrayList<Product> searchBySeason(LocalDate date);
    ArrayList<Product> searchByProximity(int radius);
    ArrayList<Product> searchByCategory(String category);

    //    this method saves cart product into cart.txt
    void addToCart(int productId);
    ObservableList<Product> filterBySeason(String season) throws OutOfSeasonException;
}