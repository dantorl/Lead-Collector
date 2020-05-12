package com.br.lead.collector.models;

import com.br.lead.collector.enums.TipoDeLead;

import javax.persistence.*;

@Entity
@Table(name = "Lead")
public class Lead {

    @Id //identifica que o campo abaixo é o ID da tabela mysql
    @GeneratedValue(strategy = GenerationType.IDENTITY) //gera ID sequencial e único
    private Integer id;

    @Column(name = "nome_completo")
    private String nome;
    private String email;
    private TipoDeLead tipoDeLead;

    public Lead() {
    }

    public Lead(String nome, String email, TipoDeLead tipoDeLead) {
        this.nome = nome;
        this.email = email;
        this.tipoDeLead = tipoDeLead;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TipoDeLead getTipoDeLead() {
        return tipoDeLead;
    }

    public void setTipoDeLead(TipoDeLead tipoDeLead) {
        this.tipoDeLead = tipoDeLead;
    }
}
