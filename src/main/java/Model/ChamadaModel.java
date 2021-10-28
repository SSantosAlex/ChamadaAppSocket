package Model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class ChamadaModel implements Serializable {
    private String turmaID;
    private LocalDateTime Inicio;
    private LocalDateTime Final;
    List<String> matriculas;

    public ChamadaModel() {}

    public String getTurmaID() {
        return turmaID;
    }

    public void setTurmaID(String turmaID) {
        this.turmaID = turmaID;
    }

    public LocalDateTime getInicio() {
        return Inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        Inicio = inicio;
    }

    public LocalDateTime getFinal() {
        return Final;
    }

    public void setFinal(LocalDateTime aFinal) {
        Final = aFinal;
    }

    public List<String> getMatriculas() {
        return matriculas;
    }

    public void setMatriculas(List<String> matriculas) {
        this.matriculas = matriculas;
    }

    @Override
    public String toString() {
        return "ChamadaProfessor{" +
                "turmaID='" + turmaID + '\'' +
                ", Inicio=" + Inicio +
                ", Final=" + Final +
                ", matriculas=" + matriculas +
                '}';
    }
}
