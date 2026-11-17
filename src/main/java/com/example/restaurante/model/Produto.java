package com.example.restaurante.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Entity
@Table(name = "produtos")
public class Produto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "nome é obrigatório")
  @Size(max = 120, message = "nome deve ter no máximo 120 caracteres")
  @Column(nullable = false, length = 120)
  private String nome;

  @Size(max = 500, message = "descrição deve ter no máximo 500 caracteres")
  @Column(length = 500)
  private String descricao;

  @NotNull(message = "valor é obrigatório")
  @PositiveOrZero(message = "valor não pode ser negativo")
  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal valor;

  public Produto() {
  }

  public Produto(String nome, String descricao, BigDecimal valor) {
    this.nome = nome;
    this.descricao = descricao;
    this.valor = valor;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public BigDecimal getValor() {
    return valor;
  }

  public void setValor(BigDecimal valor) {
    this.valor = valor;
  }
}
