package entities;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

public class Aluno {

    private static final Pattern PADRAO_RA = Pattern.compile("^[A-Z]\\d{5}-\\d$");
    private static final Pattern PADRAO_RG = Pattern.compile("^\\d{1,2}\\.\\d{3}\\.\\d{3}-[0-9Xx]$");
    private static final int IDADE_MINIMA_ALUNO_ANOS = 4;

    private String ra;
    private String nome;
    private LocalDate dataNascimento;
    private String rg;

    public Aluno(String ra, String nome, LocalDate dataNascimento, String rg) {
        setRa(ra);
        setNome(nome);
        setDataNascimento(dataNascimento);
        setRg(rg);
    }

    public String getRa() {
        return ra;
    }

    public void setRa(String ra) {
        this.ra = validarRa(ra);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = validarNome(nome);
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = validarDataNascimento(dataNascimento);
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = validarRg(rg);
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

    private String validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio.");
        }
        return nome.trim();
    }

    private LocalDate validarDataNascimento(LocalDate dataNascimento) {
        if (dataNascimento == null) {
            throw new IllegalArgumentException("Data de nascimento não pode ser nula.");
        }
        if (dataNascimento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de nascimento não pode ser futura.");
        }
        int idade = Period.between(dataNascimento, LocalDate.now()).getYears();
        if (idade < IDADE_MINIMA_ALUNO_ANOS) {
            throw new IllegalArgumentException(
                    "Data de nascimento incompatível: o aluno deve ter pelo menos " + IDADE_MINIMA_ALUNO_ANOS + " anos.");
        }
        return dataNascimento;
    }

    private String validarRg(String rg) {
        if (rg == null || rg.isBlank()) {
            throw new IllegalArgumentException("RG não pode ser nulo ou vazio.");
        }
        rg = rg.trim();
        if (!PADRAO_RG.matcher(rg).matches()) {
            throw new IllegalArgumentException(
                    "RG deve seguir o padrão brasileiro com dígito verificador (ex.: 12.345.678-9 ou 12.345.678-X).");
        }
        return rg;
    }
}