package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;
import org.w3c.dom.ls.LSOutput;

import java.sql.SQLOutput;
import java.util.Date;
import java.util.List;

public class Program {
    public static void main(String[] args) {

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("=== TEST 1: seller findById ===");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);

        System.out.println("=== TEST 2: seller findByDepartment ===");
        Department department = new Department(2, null);
        List<Seller> list = sellerDao.findByDepartment(department);
        for (Seller s : list) {
            System.out.println(s);
        }

        System.out.println("=== TEST 3: seller findAll ===");
        List<Seller> list2 = sellerDao.findAll();
        for (Seller s : list2) {
            System.out.println(s);
        }

        System.out.println("=== TEST 4: seller insert ===");
        Seller newSeller = new Seller(null, "<Michael o'Brien>", "<Obrien@gmail.com>", new Date(), 1000.0, department);
        sellerDao.insert(newSeller);
        System.out.println("Inserted! New id = " + newSeller.getId());

        System.out.println("=== TEST 5: update Base ===");
        System.out.println("Salary before update: ");
        seller = sellerDao.findById(1);
        System.out.println(seller);

        System.out.println("\nSalary after update: ");
        seller = sellerDao.findById(1);

        seller.setBases(4500.0);
        sellerDao.update(seller);
        System.out.println(seller);

        System.out.println("=== TEST 6: seller deleteById ===");
        sellerDao.deleteById(2);
        System.out.println("Deleted!");
        System.out.println(seller);
    }
}
