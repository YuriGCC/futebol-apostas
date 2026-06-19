package dao;

import model.Usuario;
import util.BD;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class UsuarioDAO implements DAO<Usuario> {
    
    private BD bd;

    public UsuarioDAO() {
        this.bd = new BD();
    }

    public Usuario buscarPorLoginESenha(String login, String senha) {
        Usuario u = null;
        if (bd.getConnection()) {
            String sql = "SELECT * FROM usuarios WHERE login = ? AND senha = ?";
            try (PreparedStatement stmt = bd.getConn().prepareStatement(sql)) {
                stmt.setString(1, login);
                stmt.setString(2, senha);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    u = extrairUsuarioDoResultSet(rs);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                bd.close();
            }
        }
        return u;
    }

    @Override
    public Usuario inserir(Usuario obj) {
        if (bd.getConnection()) {
            String sql = "INSERT INTO usuarios (nome, login, senha, saldo, ehAdmin) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = bd.getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, obj.getNome());
                stmt.setString(2, obj.getLogin());
                stmt.setString(3, obj.getSenha());
                stmt.setDouble(4, obj.getSaldo());
                stmt.setBoolean(5, obj.isEhAdmin()); 
                
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    obj.setId(rs.getInt(1));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                bd.close();
            }
        }
        return obj;
    }
    
    @Override
    public Usuario atualizar(Usuario obj) {
        if (bd.getConnection()) {
            String sql = "UPDATE usuarios SET nome = ?, login = ?, senha = ?, saldo = ?, ehAdmin = ? WHERE id = ?";
            try (PreparedStatement stmt = bd.getConn().prepareStatement(sql)) {
                stmt.setString(1, obj.getNome());
                stmt.setString(2, obj.getLogin());
                stmt.setString(3, obj.getSenha());
                stmt.setDouble(4, obj.getSaldo());
                stmt.setBoolean(5, obj.isEhAdmin());
                stmt.setInt(6, obj.getId());
                
                stmt.executeUpdate();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                bd.close();
            }
        }
        return obj;
    }

    @Override
    public void deletar(int id) {
        if (bd.getConnection()) {
            String sql = "DELETE FROM usuarios WHERE id = ?";
            try (PreparedStatement stmt = bd.getConn().prepareStatement(sql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                bd.close();
            }
        }
    }

    @Override
    public Usuario buscar(int id) {
        Usuario u = null;
        if (bd.getConnection()) {
            String sql = "SELECT * FROM usuarios WHERE id = ?";
            try (PreparedStatement stmt = bd.getConn().prepareStatement(sql)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    u = extrairUsuarioDoResultSet(rs);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                bd.close();
            }
        }
        return u;
    }

    @Override
    public ArrayList<Usuario> buscarTodos() {
        ArrayList<Usuario> lista = new ArrayList<>();
        if (bd.getConnection()) {
            String sql = "SELECT * FROM usuarios";
            try (PreparedStatement stmt = bd.getConn().prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Usuario u = extrairUsuarioDoResultSet(rs);
                    lista.add(u);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                bd.close();
            }
        }
        return lista;
    }

    // Método auxiliar para extrair o usuário do ResultSet e evitar repetição de código
    private Usuario extrairUsuarioDoResultSet(ResultSet rs) throws Exception {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setNome(rs.getString("nome"));
        u.setLogin(rs.getString("login"));
        u.setSenha(rs.getString("senha"));
        u.setSaldo(rs.getDouble("saldo"));
        u.setEhAdmin(rs.getBoolean("ehAdmin"));
        return u;
    }
}