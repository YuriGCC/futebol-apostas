package dao;

import model.Evento;
import model.StatusEvento;
import model.TipoPalpite;
import util.BD;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class EventoDAO implements DAO<Evento> {
    private BD bd = new BD();

    public ArrayList<Evento> listarEventosAbertos() {
        ArrayList<Evento> lista = new ArrayList<>();
        if (bd.getConnection()) {
            String sql = "SELECT * FROM eventos WHERE status = 'ABERTO'";
            try (PreparedStatement stmt = bd.getConn().prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Evento e = extrairEventoDoResultSet(rs);
                    lista.add(e);
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
    public Evento inserir(Evento obj) {
        if (bd.getConnection()) {
            String sql = "INSERT INTO eventos (timeCasa, timeFora, oddCasa, oddEmpate, oddFora, status, resultadoFinal) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = bd.getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, obj.getTimeCasa());
                stmt.setString(2, obj.getTimeFora());
                stmt.setDouble(3, obj.getOddCasa());
                stmt.setDouble(4, obj.getOddEmpate());
                stmt.setDouble(5, obj.getOddFora());
                stmt.setString(6, obj.getStatus() != null ? obj.getStatus().name() : StatusEvento.ABERTO.name());
                stmt.setString(7, obj.getResultadoFinal() != null ? obj.getResultadoFinal().name() : null);
                
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
    public Evento atualizar(Evento obj) {
        if (bd.getConnection()) {
            String sql = "UPDATE eventos SET timeCasa = ?, timeFora = ?, oddCasa = ?, oddEmpate = ?, oddFora = ?, status = ?, resultadoFinal = ? WHERE id = ?";
            try (PreparedStatement stmt = bd.getConn().prepareStatement(sql)) {
                stmt.setString(1, obj.getTimeCasa());
                stmt.setString(2, obj.getTimeFora());
                stmt.setDouble(3, obj.getOddCasa());
                stmt.setDouble(4, obj.getOddEmpate());
                stmt.setDouble(5, obj.getOddFora());
                stmt.setString(6, obj.getStatus() != null ? obj.getStatus().name() : StatusEvento.ABERTO.name());
                stmt.setString(7, obj.getResultadoFinal() != null ? obj.getResultadoFinal().name() : null);
                stmt.setInt(8, obj.getId());
                
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
            String sql = "DELETE FROM eventos WHERE id = ?";
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
    public Evento buscar(int id) {
        Evento e = null;
        if (bd.getConnection()) {
            String sql = "SELECT * FROM eventos WHERE id = ?";
            try (PreparedStatement stmt = bd.getConn().prepareStatement(sql)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    e = extrairEventoDoResultSet(rs);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                bd.close();
            }
        }
        return e;
    }

    @Override
    public ArrayList<Evento> buscarTodos() {
        ArrayList<Evento> lista = new ArrayList<>();
        if (bd.getConnection()) {
            String sql = "SELECT * FROM eventos";
            try (PreparedStatement stmt = bd.getConn().prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Evento e = extrairEventoDoResultSet(rs);
                    lista.add(e);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                bd.close();
            }
        }
        return lista;
    }

    private Evento extrairEventoDoResultSet(ResultSet rs) throws Exception {
        Evento e = new Evento();
        e.setId(rs.getInt("id"));
        e.setTimeCasa(rs.getString("timeCasa"));
        e.setTimeFora(rs.getString("timeFora"));
        e.setOddCasa(rs.getDouble("oddCasa"));
        e.setOddEmpate(rs.getDouble("oddEmpate"));
        e.setOddFora(rs.getDouble("oddFora"));
        e.setStatus(StatusEvento.valueOf(rs.getString("status")));
        
        String resultado = rs.getString("resultadoFinal");
        if (resultado != null && !resultado.isEmpty()) {
            e.setResultadoFinal(TipoPalpite.valueOf(resultado));
        } else {
            e.setResultadoFinal(null);
        }
        return e;
    }
}