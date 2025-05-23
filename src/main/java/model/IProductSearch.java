package model;

import java.time.LocalDate;
import java.util.ArrayList;
public interface IProductSearch {
    ArrayList<Product> searchBySeason(LocalDate date);
    ArrayList<Product> searchByProximity(int radius);
    ArrayList<Product> searchByCategory(String category);
}