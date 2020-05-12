package com.br.lead.collector.controllers;
import com.br.lead.collector.models.Lead;
import com.br.lead.collector.models.Produto;
import com.br.lead.collector.services.LeadService;
import com.br.lead.collector.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public Iterable<Produto> buscarTodosProdutos(){
        return produtoService.buscarTodosProdutos();
    }

    @GetMapping("/{id}")
    public Produto buscarProduto(@PathVariable Integer id)
    {
        Optional<Produto> produtoOptional = produtoService.buscarPorId(id);

        if(produtoOptional.isPresent()){
            return produtoOptional.get();
        }else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping
    public ResponseEntity<Produto> cadastrarProduto(@RequestBody Produto produto){
        Produto produtoObjeto = produtoService.cadastrarProduto(produto);
        return ResponseEntity.status(201).body(produtoObjeto);
    }

    @PutMapping("/{id}")
    public Produto atualizarProduto(@PathVariable Integer id, @RequestBody Produto produto)
    {
        produto.setId(id);
        Produto produtoObjeto = produtoService.atualizarProduto(produto);
        return produtoObjeto;
    }

    @DeleteMapping("/{id}")
    public Produto deletarLead(@PathVariable Integer id){
        Optional<Produto> produtoOptional = produtoService.buscarPorId(id);
        if(produtoOptional.isPresent()){
            produtoService.deletarProduto(produtoOptional.get());
            return produtoOptional.get();
        }else{
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
    }
}
