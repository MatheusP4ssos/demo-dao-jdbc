package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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

    // Método para inserir um novo vendedor (a ser implementado)
    @Override
    public void insert(Seller obj) {

    }

    // Método para atualizar um vendedor (a ser implementado)
    @Override
    public void update(Seller obj) {

    }

    // Método para deletar um vendedor pelo ID (a ser implementado)
    @Override
    public void deleteById(Integer id) {

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


    // Método para buscar todos os vendedores (a ser implementado)
    @Override
    public List<Seller> findAll() {
        return List.of();
    }
}