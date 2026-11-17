package com.example.restaurante;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import com.example.restaurante.model.Produto;
import com.example.restaurante.repository.ProdutoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ProdutoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ProdutoRepository produtoRepository;

  @BeforeEach
  void limparBase() {
    produtoRepository.deleteAll();
  }

  @Test
  void listaProdutosVazia() throws Exception {
    mockMvc.perform(get("/produtos"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  void criaProdutoValido() throws Exception {
    mockMvc.perform(post("/produtos")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"nome\":\"Moqueca\",\"descricao\":\"Peixe e dendê\",\"valor\":58.00}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.nome").value("Moqueca"))
        .andExpect(jsonPath("$.valor").value(58.00));
  }

  @Test
  void rejeitaProdutoSemNomeENomeNegativo() throws Exception {
    mockMvc.perform(post("/produtos")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"descricao\":\"sem nome\",\"valor\":-1}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.erro").value("dados inválidos"))
        .andExpect(jsonPath("$.campos.nome").exists())
        .andExpect(jsonPath("$.campos.valor").exists());
  }

  @Test
  void buscaProdutoInexistenteRetorna404() throws Exception {
    mockMvc.perform(get("/produtos/999"))
        .andExpect(status().isNotFound());
  }

  @Test
  void atualizaProduto() throws Exception {
    Produto salvo = produtoRepository.save(new Produto("Suco", "Caju", new BigDecimal("9.90")));

    mockMvc.perform(put("/produtos/" + salvo.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"nome\":\"Suco de caju\",\"descricao\":\"500 ml\",\"valor\":11.50}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nome").value("Suco de caju"))
        .andExpect(jsonPath("$.valor").value(11.50));
  }

  @Test
  void removeProdutoEDepoisRetorna404() throws Exception {
    Produto salvo = produtoRepository.save(new Produto("Pudim", "Leite", new BigDecimal("14.50")));

    mockMvc.perform(delete("/produtos/" + salvo.getId()))
        .andExpect(status().isNoContent());

    mockMvc.perform(get("/produtos/" + salvo.getId()))
        .andExpect(status().isNotFound());
  }

  @Test
  void filtraPorNomeIgnorandoCaixa() throws Exception {
    produtoRepository.save(new Produto("Moqueca de peixe", "Peixe", new BigDecimal("58.00")));
    produtoRepository.save(new Produto("Baião de dois", "Carne-seca", new BigDecimal("42.90")));

    mockMvc.perform(get("/produtos").param("nome", "MOQUECA"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].nome").value("Moqueca de peixe"));
  }
}
