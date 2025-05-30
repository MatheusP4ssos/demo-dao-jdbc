package model.dao;

import db.DB;
import model.dao.impl.DepartmentDaoJDBC;

public class DaoFactory2 {
    public static DepartmentDao createDepartmentDao() {
        return new DepartmentDaoJDBC(DB.getConnection());
    }
}