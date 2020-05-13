package com.br.lead.collector.models;

import com.br.lead.collector.enums.TipoDeLead;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "Lead")
public class Lead {

    @Id //identifica que o campo abaixo é o ID da tabela mysql
    @GeneratedValue(strategy = GenerationType.IDENTITY) //gera ID sequencial e único
    private Integer id;

    @Column(name = "nome_completo")
    @Size(min = 8, max = 100, message = "O nome deve ter no minimo 8 caracteres e no maximo 100")
    private String nome;

    @Email(message = "O formato do email é invalido")
    private String email;
    private TipoDeLead tipoDeLead;

    @ManyToMany
    private List<Produto> produtos;

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

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }
}
