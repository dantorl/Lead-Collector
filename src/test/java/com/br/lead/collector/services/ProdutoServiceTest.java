package com.br.lead.collector.services;

import com.br.lead.collector.enums.TipoDeLead;
import com.br.lead.collector.models.Lead;
import com.br.lead.collector.models.Produto;
import com.br.lead.collector.repositories.ProdutoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Optional;

@SpringBootTest
public class ProdutoServiceTest {
    @MockBean
    ProdutoRepository produtoRepository;

    @Autowired
    ProdutoService produtoService;
    Produto produto;
    Produto produto2;
    Optional produtoOptional;

    @BeforeEach
    public void inicializar() {
        produto = new Produto();
        produto.setNome("Cafe");
        produto.setId(1);
        produto.setPreco(10.00);
        produto.setDescricao("Cafe especial");

        produto2 = new Produto();
        produto2.setNome("Cafe");
        produto2.setId(1);
        produto2.setPreco(10.00);
        produto2.setDescricao("Cafe especial");

        produtoOptional = Optional.of(produto);
    }

    @Test
    public void testarCadastrarProduto(){

        Mockito.when(produtoRepository.save(Mockito.any(Produto.class))).thenReturn(produto);

        Produto produtoObjeto = produtoService.cadastrarProduto(produto);

        Assertions.assertEquals(produto, produtoObjeto);
    }
    @Test
    public void testarDeletarProduto(){
        produtoService.deletarProduto(produto);
        Mockito.verify(produtoRepository, Mockito.times(1)).delete(Mockito.any(Produto.class));
    }
    @Test
    public void buscarProdutoPorId(){

        Mockito.when(produtoRepository.findById(Mockito.anyInt())).thenReturn(produtoOptional);

        Optional<Produto> produtoOptional2 = produtoService.buscarPorId(produto.getId());
        Assertions.assertEquals(produtoOptional, produtoOptional2);
    }

    @Test
    public void buscarTodosProdutos(){
        Iterable<Produto> produtoIterable = Arrays.asList(produto,produto2);

        Mockito.when(produtoRepository.findAll()).thenReturn(produtoIterable);

        Iterable<Produto> produtoIterable2 = produtoService.buscarTodosProdutos();
        Assertions.assertEquals(produtoIterable, produtoIterable2);
    }

    @Test
    public void TestarAtualizarLead(){
        Mockito.when(produtoRepository.save(Mockito.any(Produto.class))).thenReturn(produto);
        Mockito.when(produtoRepository.findById(Mockito.anyInt())).thenReturn(produtoOptional);

        Produto produtoObjeto = produtoService.atualizarProduto(produto);

        Assertions.assertEquals(produto, produtoObjeto);
    }
    @Test
    public void TestarAtualizarLeadErro(){
        Optional<Produto> produtoOptional2 = Optional.of(produto);

        Mockito.when(produtoRepository.save(Mockito.any(Produto.class))).thenReturn(produto);
        Mockito.when(produtoRepository.findById(Mockito.anyInt())).thenReturn(produtoOptional);

        Produto produto3 = produto;
        produto3.setNome(null);
        Produto produtoObjeto = produtoService.atualizarProduto(produto3);
        Assertions.assertEquals(produto3, produtoObjeto);

        produto3.setDescricao(null);
        produtoObjeto = produtoService.atualizarProduto(produto3);
        Assertions.assertEquals(produto3, produtoObjeto);

        produto3.setPreco(null);
        produtoObjeto = produtoService.atualizarProduto(produto3);
        Assertions.assertEquals(produto3, produtoObjeto);
    }
}
