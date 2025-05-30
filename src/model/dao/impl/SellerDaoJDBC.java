package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe responsável por implementar as operações de acesso a dados de Seller no banco de dados usando JDBC
 */

// Implementação do DAO (Data Access Object) para a entidade Seller usando JDBC
public class SellerDaoJDBC implements SellerDao {

    // Conexão com o banco de dados
    private Connection conn;

    // Construtor que recebe a conexão
    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    // Método para inserir um novo vendedor
    @Override
    public void insert(Seller obj) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            // Primeiro, verifica se já existe um vendedor com o mesmo email
            Seller existingSeller = findByEmail(obj.getEmail());
            if (existingSeller != null) {
                System.out.println("Vendedor com email " + obj.getEmail() + " já existe com o ID "
                        + existingSeller.getId());
                obj.setId(existingSeller.getId());
                return;
            }


            st = conn.prepareStatement(
                    "INSERT INTO seller " +
                            "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                            "VALUES " +
                            "(?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBases());
            st.setInt(5, obj.getDepartment().getId());

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
        } catch (
                SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    // Método para atualizar um vendedor
    @Override
    public void update(Seller obj) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "UPDATE seller "
                            + "SET name = ?, Email = ?, BirthDate = ?," +
                            " BaseSalary = ?, DepartmentId = ? "
                            + "WHERE id = ?");

            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBases());
            st.setInt(5, obj.getDepartment().getId());
            st.setInt(6, obj.getId());
            int rowsAffected = st.executeUpdate();
            if (rowsAffected == 0) {
                throw new DbException("Erro inesperado! Nenhuma linha foi afetada!");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    // Método para deletar um vendedor pelo ID
    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            st = conn.prepareStatement("DELETE FROM seller WHERE id = ? ");
            
            st.setInt(1, id);
            
            int rowsAffected = st.executeUpdate();

            if (rowsAffected == 0) {
                throw new DbException("Id não encontrado para exclusão");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    // Método para buscar um vendedor pelo ID
    @Override
    public Seller findById(Integer id) {
        // Declaração dos objetos PreparedStatement e ResultSet como null
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            // Prepara a consulta SQL
            // Seleciona todos os campos do vendedor e o nome do departamento
            st = conn.prepareStatement(
                    "SELECT seller.*, department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "WHERE seller.Id = ?");

            // Define o valor do parâmetro na consulta (ID do vendedor)
            st.setInt(1, id);

            // Executa a consulta e obtém o resultado
            rs = st.executeQuery();

            // Se encontrou algum resultado
            if (rs.next()) {
                Department dep = instantiateDepartment(rs); // Cria e popula o objeto Department
                Seller seller = instantiateSeller(rs, dep); // Cria e popula o objeto Seller


                // Retorna o vendedor encontrado
                return seller;
            }
            // Se não encontrou nenhum resultado, retorna null
            return null;

            // Tratamento de exceções SQL
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        // Bloco finally para garantir o fechamento dos recursos
        finally {
            // Fecha o ResultSet e o Statement, independente do resultado
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    // Associa o departamento ao vendedor
    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
        Seller sel = new Seller();
        sel.setId(rs.getInt("Id"));
        sel.setName(rs.getString("Name"));
        sel.setEmail(rs.getString("Email"));
        sel.setBirthDate(rs.getDate("BirthDate"));
        sel.setBases(rs.getDouble("BaseSalary"));
        sel.setDepartment(dep);
        return sel;
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }


    // Método para buscar todos os vendedores
    @Override
    public List<Seller> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT seller.*, department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id ");

            rs = st.executeQuery();
            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {
                Department dep = map.get(rs.getInt("DepartmentId"));

                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }
                Seller obj = instantiateSeller(rs, dep);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }


    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "SELECT seller.*, department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "WHERE DepartmentId = ? " +
                            "ORDER BY Name");

            st.setInt(1, department.getId());

            rs = st.executeQuery();

            List<Seller> list = new ArrayList<>();

            Map<Integer, Department> map = new HashMap<>();
            map.put(department.getId(), department);

            while (rs.next()) {

                Department dep = map.get(rs.getInt("DepartmentId"));

                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }

                Seller obj = instantiateSeller(rs, dep);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    @Override
    public Seller findByEmail(String email) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "SELECT seller.*, department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "WHERE seller.Email = ?");

            st.setString(1, email);
            rs = st.executeQuery();

            if (rs.next()) {
                Department dep = instantiateDepartment(rs);
                return instantiateSeller(rs, dep);
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            if (rs != null) DB.closeResultSet(rs);
        }
    }
}
