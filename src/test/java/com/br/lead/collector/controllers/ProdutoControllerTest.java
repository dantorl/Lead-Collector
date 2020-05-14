package com.br.lead.collector.controllers;

import com.br.lead.collector.models.Lead;
import com.br.lead.collector.models.Produto;
import com.br.lead.collector.services.LeadService;
import com.br.lead.collector.services.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Optional;

@WebMvcTest(ProdutoController.class)
public class ProdutoControllerTest {

    @MockBean
    ProdutoService produtoService;

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    Produto produto;
    Produto produto2;

    @BeforeEach
    public void inicializar() {
        produto = new Produto();
        produto.setNome("Cafe");
        produto.setId(1);
        produto.setPreco(10.00);
        produto.setDescricao("Cafe especial");

        produto2 = new Produto();
        produto2.setNome("Cafe");
        produto2.setId(2);
        produto2.setPreco(10.00);
        produto2.setDescricao("Cafe especial");

    }

    @Test
    public void testarCadastroDeProduto() throws Exception {

        Mockito.when(produtoService.cadastrarProduto(Mockito.any(Produto.class))).thenReturn(produto);

        String json = mapper.writeValueAsString(produto);

        mockMvc.perform(MockMvcRequestBuilders.post("/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome", CoreMatchers.equalTo("Cafe")));
    }

    @Test
    public void testarCadastroDeProdutoErro() throws Exception {

        produto = null;

        Mockito.when(produtoService.cadastrarProduto(Mockito.any(Produto.class))).thenReturn(produto);

        String json = mapper.writeValueAsString(produto);

        mockMvc.perform(MockMvcRequestBuilders.post("/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testarBuscarProduto() throws Exception {
        Optional<Produto> produtoOptional = Optional.of(produto);

        Mockito.when(produtoService.buscarPorId(Mockito.anyInt())).thenReturn(produtoOptional);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/produtos/" + produto.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.equalTo(1)));
    }
    @Test
    public void testarBuscarProdutoErro() throws Exception {
        Optional<Produto> produtoOptional = Optional.empty();
        Mockito.when(produtoService.buscarPorId(Mockito.anyInt())).thenReturn(produtoOptional);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/produtos/"+produto.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testarBuscarTodosProdutos() throws Exception {

        Iterable<Produto> produtoIterable = Arrays.asList(produto, produto2);

        Mockito.when(produtoService.buscarTodosProdutos()).thenReturn(produtoIterable);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/produtos"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id", CoreMatchers.equalTo(2)));
    }

    @Test
    public void testarBuscarTodosProdutosVazio() throws Exception {

        Iterable<Produto> produtoIterable = Arrays.asList();

        Mockito.when(produtoService.buscarTodosProdutos()).thenReturn(produtoIterable);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/produtos"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    public void testarAtualizarProduto() throws Exception {
        Optional<Produto> produtoOptional = Optional.of(produto);

        produto.setNome("Teste Produto");

        Mockito.when(produtoService.atualizarProduto(Mockito.any(Produto.class))).thenReturn(produto);
        Mockito.when(produtoService.buscarPorId(Mockito.anyInt())).thenReturn(produtoOptional);

        String json = mapper.writeValueAsString(produto);

        mockMvc.perform(MockMvcRequestBuilders.put("/produtos/"+produto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome", CoreMatchers.equalTo("Teste Produto")));
    }

    @Test
    public void testarDeleteLead() throws Exception {
        Optional<Produto> produtoOptional = Optional.of(produto);

        Mockito.when(produtoService.buscarPorId(Mockito.anyInt())).thenReturn(produtoOptional);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/produtos/"+produto.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.equalTo(1)));
        Mockito.verify(produtoService, Mockito.times(1)).deletarProduto(Mockito.any(Produto.class));

        produtoOptional = Optional.empty();

        Mockito.when(produtoService.buscarPorId(Mockito.anyInt())).thenReturn(produtoOptional);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/produtos/"+produto.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Mockito.verify(produtoService, Mockito.times(1)).deletarProduto(Mockito.any(Produto.class));
    }

}
