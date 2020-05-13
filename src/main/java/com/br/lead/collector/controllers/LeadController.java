package com.br.lead.collector.controllers;

import com.br.lead.collector.models.Lead;
import com.br.lead.collector.models.Produto;
import com.br.lead.collector.services.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/leads")
public class LeadController {
    @Autowired
    private LeadService leadService;

    @GetMapping
    public Iterable<Lead> buscarTodosLeads(){
        return leadService.buscarTodosLeads();
    }
    @GetMapping("/{id}")
    public Lead buscarLead(@PathVariable Integer id)
    {
        Optional<Lead> leadOptional = leadService.buscarPorId(id);

        if(leadOptional.isPresent()){
            return leadOptional.get();
        }else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
    }
    @PostMapping
    public ResponseEntity<Lead> cadastrarLead(@RequestBody Lead lead){
        List<Integer> produtosId = new ArrayList<>();
        for (Produto produto: lead.getProdutos()){
            produtosId.add(produto.getId());
        }
        Iterable<Produto> produtosIterable = leadService.buscarTodosProdutos(produtosId);

        lead.setProdutos((List) produtosIterable);

        Lead leadObjeto = leadService.cadastrarLead(lead);
        return ResponseEntity.status(201).body(leadObjeto);
    }
    @PutMapping("/{id}")
    public Lead atualizarLead(@PathVariable Integer id, @RequestBody Lead lead)
    {
        lead.setId(id);
        Lead leadObjeto = leadService.atualizarLead(lead);
        return leadObjeto;
    }
    @DeleteMapping("/{id}")
    public Lead deletarLead(@PathVariable Integer id){
        Optional<Lead> leadOptional = leadService.buscarPorId(id);
        if(leadOptional.isPresent()){
            leadService.deletarLead(leadOptional.get());
            return leadOptional.get();
        }else{
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
    }
}
