package com.example.restaurante.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, Object> camposInvalidos(MethodArgumentNotValidException excecao) {
    Map<String, String> campos = new LinkedHashMap<>();
    excecao.getBindingResult().getFieldErrors()
        .forEach(erro -> campos.put(erro.getField(), erro.getDefaultMessage()));

    Map<String, Object> resposta = new LinkedHashMap<>();
    resposta.put("erro", "dados inválidos");
    resposta.put("campos", campos);
    return resposta;
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, String> corpoInvalido() {
    return Map.of("erro", "corpo da requisição inválido");
  }
}
