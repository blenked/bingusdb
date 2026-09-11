package entities;

import java.util.regex.Pattern;

public class Notas {

    private static final Pattern PADRAO_RA = Pattern.compile("^[A-Z]\\d{5}-\\d$");
    private static final double NOTA_MINIMA = 0.0;
    private static final double NOTA_MAXIMA = 10.0;

    private String ra;
    private int idDisciplina;
    private int idTipoProva;
    private double nota;

    public Notas(String ra, int idDisciplina, int idTipoProva, double nota) {
        setRa(ra);
        setIdDisciplina(idDisciplina);
        setIdTipoProva(idTipoProva);
        setNota(nota);
    }

    public String getRa() {
        return ra;
    }

    public void setRa(String ra) {
        this.ra = validarRa(ra);
    }

    public int getIdDisciplina() {
        return idDisciplina;
    }

    public void setIdDisciplina(int idDisciplina) {
        this.idDisciplina = validarIdDisciplina(idDisciplina);
    }

    public int getIdTipoProva() {
        return idTipoProva;
    }

    public void setIdTipoProva(int idTipoProva) {
        this.idTipoProva = validarIdTipoProva(idTipoProva);
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = validarNota(nota);
    }

    private String validarRa(String ra) {
        if (ra == null || ra.isBlank()) {
            throw new IllegalArgumentException("RA não pode ser nulo ou vazio.");
        }
        if (!PADRAO_RA.matcher(ra).matches()) {
            throw new IllegalArgumentException(
                    "RA deve seguir o padrão: 1 letra maiúscula, 5 dígitos, hífen e 1 dígito verificador (ex.: R00001-1).");
        }
        return ra;
    }

    private int validarIdDisciplina(int idDisciplina) {
        if (idDisciplina <= 0) {
            throw new IllegalArgumentException("ID da disciplina deve ser um valor inteiro positivo.");
        }
        return idDisciplina;
    }

    private int validarIdTipoProva(int idTipoProva) {
        if (idTipoProva <= 0) {
            throw new IllegalArgumentException("ID do tipo de prova deve ser um valor inteiro positivo.");
        }
        return idTipoProva;
    }

    private double validarNota(double nota) {
        if (nota <= NOTA_MINIMA || nota >= NOTA_MAXIMA) {
            throw new IllegalArgumentException(
                    "Nota deve estar entre " + NOTA_MINIMA + " e " + NOTA_MAXIMA + ". Valor informado: " + nota);
        }
        return nota;
    }
}