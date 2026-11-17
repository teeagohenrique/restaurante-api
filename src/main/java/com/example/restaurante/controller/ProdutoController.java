package com.example.restaurante.controller;

import java.util.List;

import javax.validation.Valid;

import com.example.restaurante.model.Produto;
import com.example.restaurante.repository.ProdutoRepository;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

  private final ProdutoRepository produtoRepository;

  public ProdutoController(ProdutoRepository produtoRepository) {
    this.produtoRepository = produtoRepository;
  }

  @GetMapping
  public List<Produto> listar(@RequestParam(required = false) String nome) {
    if (nome == null || nome.isBlank()) {
      return produtoRepository.findAll();
    }
    return produtoRepository.findByNomeContainingIgnoreCase(nome);
  }

  @GetMapping("/{id}")
  public Produto buscar(@PathVariable Long id) {
    return produtoRepository.findById(id).orElseThrow(() -> naoEncontrado(id));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Produto criar(@Valid @RequestBody Produto produto) {
    produto.setId(null);
    return produtoRepository.save(produto);
  }

  @PutMapping("/{id}")
  public Produto atualizar(@PathVariable Long id, @Valid @RequestBody Produto dados) {
    Produto produto = buscar(id);
    produto.setNome(dados.getNome());
    produto.setDescricao(dados.getDescricao());
    produto.setValor(dados.getValor());
    return produtoRepository.save(produto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void remover(@PathVariable Long id) {
    if (!produtoRepository.existsById(id)) {
      throw naoEncontrado(id);
    }
    produtoRepository.deleteById(id);
  }

  private ResponseStatusException naoEncontrado(Long id) {
    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto " + id + " não encontrado");
  }
}
