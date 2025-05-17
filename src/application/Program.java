package application;

import model.entities.Department;
import model.entities.Seller;

import java.util.Date;

public class Program {
    public static void main(String[] args) {

        Department obj = new Department(1, "books");
        Seller seller = new Seller(21, "Bob", "Bob@gmail.com", new Date(), 1200.0, obj);


        System.out.println(seller);


    }
}
