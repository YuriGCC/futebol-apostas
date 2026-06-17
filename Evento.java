package model;

public class Evento {
    private int id;
    private String timeCasa;
    private String timeFora;
    private double oddCasa;
    private double oddEmpate;
    private double oddFora;
    private StatusEvento status;
    private TipoPalpite resultadoFinal;

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTimeCasa() { return timeCasa; }
    public void setTimeCasa(String timeCasa) { this.this.timeCasa = timeCasa; }
    public String getTimeFora() { return timeFora; }
    public void setTimeFora(String timeFora) { this.timeFora = timeFora; }
    public double getOddCasa() { return oddCasa; }
    public void setOddCasa(double oddCasa) { this.oddCasa = oddCasa; }
    public double getOddEmpate() { return oddEmpate; }
    public void setOddEmpate(double oddEmpate) { this.oddEmpate = oddEmpate; }
    public double getOddFora() { return oddFora; }
    public void setOddFora(double oddFora) { this.oddFora = oddFora; }
    public StatusEvento getStatus() { return status; }
    public void setStatus(StatusEvento status) { this.status = status; }
    public TipoPalpite getResultadoFinal() { return resultadoFinal; }
    public void setResultadoFinal(TipoPalpite resultadoFinal) { this.resultadoFinal = resultadoFinal; }
}