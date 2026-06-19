package model;

public class Aposta {
    private int id;
    private int usuarioId;
    private int eventoId;
    private double valorApostado;
    private TipoPalpite palpite;
    private StatusAposta status;
    private double ganhoPotencial;

    public Aposta() {
    }

    public Aposta(int id, int usuarioId, int eventoId, double valorApostado, TipoPalpite palpite, StatusAposta status, double ganhoPotencial) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.eventoId = eventoId;
        this.valorApostado = valorApostado;
        this.palpite = palpite;
        this.status = status;
        this.ganhoPotencial = ganhoPotencial;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getEventoId() {
        return eventoId;
    }

    public void setEventoId(int eventoId) {
        this.eventoId = eventoId;
    }

    public double getValorApostado() {
        return valorApostado;
    }

    public void setValorApostado(double valorApostado) {
        this.valorApostado = valorApostado;
    }

    public TipoPalpite getPalpite() {
        return palpite;
    }

    public void setPalpite(TipoPalpite palpite) {
        this.palpite = palpite;
    }

    public StatusAposta getStatus() {
        return status;
    }

    public void setStatus(StatusAposta status) {
        this.status = status;
    }

    public double getGanhoPotencial() {
        return ganhoPotencial;
    }

    public void setGanhoPotencial(double ganhoPotencial) {
        this.ganhoPotencial = ganhoPotencial;
    }
}