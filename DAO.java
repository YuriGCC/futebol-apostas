import java.util.ArrayList;

public interface DAO<T> {
    T inserir(T obj);
    T atualizar(T obj);
    void deletar(int id);
    T buscar(int id);
    ArrayList<T> buscarTodos();
}