package dao;

import model.Aposta;
import model.StatusAposta;
import model.TipoPalpite;
import util.BD;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ApostaDAO implements DAO<Aposta> {
    private BD bd = new BD();

    public ArrayList<Aposta> listarApostasPorUsuario(int usuarioId) {
        ArrayList<Aposta> lista = new ArrayList<>();
        if (bd.getConnection()) {
            String sql = "SELECT * FROM apostas WHERE usuarioId = ?";
            try (PreparedStatement stmt = bd.getConn().prepareStatement(sql)) {
                stmt.setInt(1, usuarioId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Aposta a = new Aposta();
                    a.setId(rs.getInt("id"));
                    a.setUsuarioId(rs.getInt("usuarioId"));
                    a.setEventoId(rs.getInt("eventoId"));
                    a.setValorApostado(rs.getDouble("valorApostado"));
                    a.setPalpite(TipoPalpite.valueOf(rs.getString("palpite")));
                    a.setStatus(StatusAposta.valueOf(rs.getString("status")));
                    a.setGanhoPotencial(rs.getDouble("ganhoPotencial"));
                    lista.add(a);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                bd.close();
            }
        }
        return lista;
    }

    @Override
    public Aposta inserir(Aposta obj) {
        if (bd.getConnection()) {
            String sql = "INSERT INTO apostas (usuarioId, eventoId, valorApostado, palpite, status, ganhoPotencial) VALUES (?, ?, ?, ?, ?, ?)";
            // Usamos RETURN_GENERATED_KEYS para recuperar o ID gerado pelo banco de dados
            try (PreparedStatement stmt = bd.getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, obj.getUsuarioId());
                stmt.setInt(2, obj.getEventoId());
                stmt.setDouble(3, obj.getValorApostado());
                stmt.setString(4, obj.getPalpite().name());
                stmt.setString(5, obj.getStatus().name());
                stmt.setDouble(6, obj.getGanhoPotencial());
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
    public Aposta atualizar(Aposta obj) {
        if (bd.getConnection()) {
            String sql = "UPDATE apostas SET usuarioId = ?, eventoId = ?, valorApostado = ?, palpite = ?, status = ?, ganhoPotencial = ? WHERE id = ?";
            try (PreparedStatement stmt = bd.getConn().prepareStatement(sql)) {
                stmt.setInt(1, obj.getUsuarioId());
                stmt.setInt(2, obj.getEventoId());
                stmt.setDouble(3, obj.getValorApostado());
                stmt.setString(4, obj.getPalpite().name());
                stmt.setString(5, obj.getStatus().name());
                stmt.setDouble(6, obj.getGanhoPotencial());
                stmt.setInt(7, obj.getId());
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
            String sql = "DELETE FROM apostas WHERE id = ?";
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
    public Aposta buscar(int id) {
        Aposta a = null;
        if (bd.getConnection()) {
            String sql = "SELECT * FROM apostas WHERE id = ?";
            try (PreparedStatement stmt = bd.getConn().prepareStatement(sql)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    a = new Aposta();
                    a.setId(rs.getInt("id"));
                    a.setUsuarioId(rs.getInt("usuarioId"));
                    a.setEventoId(rs.getInt("eventoId"));
                    a.setValorApostado(rs.getDouble("valorApostado"));
                    a.setPalpite(TipoPalpite.valueOf(rs.getString("palpite")));
                    a.setStatus(StatusAposta.valueOf(rs.getString("status")));
                    a.setGanhoPotencial(rs.getDouble("ganhoPotencial"));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                bd.close();
            }
        }
        return a;
    }

    @Override
    public ArrayList<Aposta> buscarTodos() {
        ArrayList<Aposta> lista = new ArrayList<>();
        if (bd.getConnection()) {
            String sql = "SELECT * FROM apostas";
            try (PreparedStatement stmt = bd.getConn().prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Aposta a = new Aposta();
                    a.setId(rs.getInt("id"));
                    a.setUsuarioId(rs.getInt("usuarioId"));
                    a.setEventoId(rs.getInt("eventoId"));
                    a.setValorApostado(rs.getDouble("valorApostado"));
                    a.setPalpite(TipoPalpite.valueOf(rs.getString("palpite")));
                    a.setStatus(StatusAposta.valueOf(rs.getString("status")));
                    a.setGanhoPotencial(rs.getDouble("ganhoPotencial"));
                    lista.add(a);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                bd.close();
            }
        }
        return lista;
    }
}