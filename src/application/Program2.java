package application;

import model.dao.DaoFactory2;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.util.List;

public class Program2 {
    public static void main(String[] args) {

        DepartmentDao departmentDao = DaoFactory2.createDepartmentDao();

        System.out.println("=== TEST 1: Department findAll ===");
        List<Department> list = departmentDao.findAll();
        for (Department d : list) {
            System.out.println(d);
        }

        System.out.println("=== TEST 2: Department findById ===");
        Department department = departmentDao.findById(1);
        System.out.println(department);

        System.out.println("=== TEST 3: Department insert ===");
        Department newDepartment = new Department(null, "RH");
        departmentDao.insert(newDepartment);
        System.out.println("Inserted! New id = " + newDepartment.getId());

        System.out.println("=== TEST 4: Department update ===");
        System.out.println("Department before update: ");
        department = departmentDao.findById(1);
        System.out.println(department);
        department.setName("Finance");
        departmentDao.update(department);
        System.out.println(department);

        System.out.println("=== TEST 5: Department delete ===");
        System.out.println("Enter id for delete test: ");
        departmentDao.deleteById(2);
        System.out.println("Delete completed");
    }
}