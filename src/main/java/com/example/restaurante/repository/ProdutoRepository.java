package com.example.restaurante.repository;

import java.util.List;

import com.example.restaurante.model.Produto;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

  List<Produto> findByNomeContainingIgnoreCase(String nome);
}
