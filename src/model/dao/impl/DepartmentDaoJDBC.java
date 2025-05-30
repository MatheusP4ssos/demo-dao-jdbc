package model.dao.impl;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {
 
    private Connection conn;
    
    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }
    
    @Override
    public void insert(Department obj) {
        PreparedStatement st = null;
        ResultSet rs = null;

        // Primeiro, verifica se já existe um departamento com o mesmo nome
        try {
            Department existingDepartment = findByName(obj.getName());
            if (existingDepartment != null) {
                System.out.println("Departamento " + obj.getName() + " Já existe com o ID "
                        + existingDepartment.getId());
                obj.setId(existingDepartment.getId());// Define o ID do departamento existente
                return;
            }
            st = DB.getConnection().prepareStatement(
                    "INSERT INTO department " +
                            "(Name) " +
                            "VALUES " +
                            "(?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getName()); // Adiciona esta linha

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Erro inesperado! Nenhuma linha foi afetada!");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }
    // Método auxiliar para buscar departamento pelo nome
private Department findByName(String name) {
    PreparedStatement st = null;
    ResultSet rs = null;

    try {
        st = DB.getConnection().prepareStatement(
                "SELECT * FROM department WHERE Name = ?");

        st.setString(1, name);
        rs = st.executeQuery();

        if (rs.next()) {
            Department dep = new Department();
            dep.setId(rs.getInt("Id"));
            dep.setName(rs.getString("Name"));
            return dep;
        }
        return null;
    } catch (SQLException e) {
        throw new DbException(e.getMessage());
    } finally {
        DB.closeStatement(st);
        if (rs != null) DB.closeResultSet(rs);
    }
}

@Override
public void update(Department obj) {
    PreparedStatement st = null;
    
    try {
        st = conn.prepareStatement(
                "UPDATE department "
                + "SET Name = ? "
                + "WHERE Id = ?");

        st.setString(1, obj.getName());
        st.setInt(2, obj.getId());
        
        int rowsAffected = st.executeUpdate();
        
        if (rowsAffected == 0) {
            throw new DbException("Erro inesperado! Nenhuma linha foi afetada!");
        }
    } catch (SQLException e) {
        throw new DbException(e.getMessage());
    } finally {
        DB.closeStatement(st);
    }
}

@Override
public void deleteById(Integer id) {
    PreparedStatement st = null;
    
    try {
        st = conn.prepareStatement(
            "DELETE FROM department WHERE Id = ?"
        );
        
        st.setInt(1, id);
        
        int rowsAffected = st.executeUpdate();
        
        if (rowsAffected == 0) {
            throw new DbException("Id não encontrado para exclusão");
        }
    } catch (SQLException e) {
        throw new DbIntegrityException(e.getMessage());
    } finally {
        DB.closeStatement(st);
    }
}

@Override
public Department findById(Integer id) {
    PreparedStatement st = null;
    ResultSet rs = null;
    
    try {
        st = conn.prepareStatement(
            "SELECT * FROM department WHERE Id = ?"
        );
        
        st.setInt(1, id);
        rs = st.executeQuery();
        
        if (rs.next()) {
            Department dep = new Department();
            dep.setId(rs.getInt("Id"));
            dep.setName(rs.getString("Name"));
            return dep;
        }
        return null;
    } catch (SQLException e) {
        throw new DbException(e.getMessage());
    } finally {
        DB.closeStatement(st);
        DB.closeResultSet(rs);
    }
}

@Override
public List<Department> findAll() {
    PreparedStatement st = null;
    ResultSet rs = null;

    try {
        Connection conn = DB.getConnection();
        st = conn.prepareStatement("SELECT * FROM department ORDER BY Id");
        rs = st.executeQuery();

        List<Department> departments = new ArrayList<>();

        while (rs.next()) {
            Department dep = new Department();
            dep.setId(rs.getInt("Id"));
            dep.setName(rs.getString("Name"));
            departments.add(dep);
        }
        return departments;
    } catch (SQLException e) {
        throw new DbException(e.getMessage());
    } finally {
        DB.closeResultSet(rs);
        DB.closeStatement(st);
    }
}
}