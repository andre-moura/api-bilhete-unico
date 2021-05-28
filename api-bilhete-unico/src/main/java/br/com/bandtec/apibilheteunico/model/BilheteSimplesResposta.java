package br.com.bandtec.apibilheteunico.model;

import java.time.LocalDate;

public class BilheteSimplesResposta {

    // Atributos
    private String passageiro;

    private LocalDate nascimento;

    private String cpf;

    private Double saldo;

    // Getters e Setters
    public String getPassageiro() {
        return passageiro;
    }

    public void setPassageiro(String passageiro) {
        this.passageiro = passageiro;
    }

    public LocalDate getNascimento() {
        return nascimento;
    }

    public void setNascimento(LocalDate nascimento) {
        this.nascimento = nascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }
}
