package service;

import dao.ApostaDAO;
import dao.EventoDAO;
import dao.UsuarioDAO;
import model.*;

import java.util.ArrayList;

public class FinanceiroService {
    
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private ApostaDAO apostaDAO = new ApostaDAO();
    private EventoDAO eventoDAO = new EventoDAO();

    public double calcularOverround(double oddCasa, double oddEmpate, double oddFora) {
        return (1.0 / oddCasa) + (1.0 / oddEmpate) + (1.0 / oddFora);
    }

    public boolean processarAposta(Usuario u, Evento e, double valor, TipoPalpite palpite) {
        if (e.getStatus() != StatusEvento.ABERTO) {
            return false; 
        }
        if (u.getSaldo() < valor) {
            return false; 
        }

        u.setSaldo(u.getSaldo() - valor);
        usuarioDAO.atualizar(u);

        double oddEscolhida = 0;
        switch (palpite) {
            case CASA: oddEscolhida = e.getOddCasa(); break;
            case EMPATE: oddEscolhida = e.getOddEmpate(); break;
            case FORA: oddEscolhida = e.getOddFora(); break;
        }

        Aposta aposta = new Aposta();
        aposta.setUsuarioId(u.getId());
        aposta.setEventoId(e.getId());
        aposta.setValorApostado(valor);
        aposta.setPalpite(palpite);
        aposta.setStatus(StatusAposta.PENDENTE);
        aposta.setGanhoPotencial(valor * oddEscolhida);
        
        apostaDAO.inserir(aposta);
        return true;
    }

    public Evento encerrarEvento(Evento e, TipoPalpite resultadoReal) {
        e.setStatus(StatusEvento.ENCERRADO);
        e.setResultadoFinal(resultadoReal);
        eventoDAO.atualizar(e);

        ArrayList<Aposta> todasApostas = apostaDAO.buscarTodos(); 
        for (Aposta a : todasApostas) {
            if (a.getEventoId() == e.getId() && a.getStatus() == StatusAposta.PENDENTE) {
                if (a.getPalpite() == resultadoReal) {
                    a.setStatus(StatusAposta.GANHA);
                    
                    Usuario apostador = usuarioDAO.buscar(a.getUsuarioId());
                    if (apostador != null) {
                        apostador.setSaldo(apostador.getSaldo() + a.getGanhoPotencial());
                        usuarioDAO.atualizar(apostador);
                    }
                } else {
                    a.setStatus(StatusAposta.PERDIDA);
                }
                apostaDAO.atualizar(a);
            }
        }
        return e;
    }
}