package com.br.lead.collector.services;

import com.br.lead.collector.enums.TipoDeLead;
import com.br.lead.collector.models.Lead;
import com.br.lead.collector.models.Produto;
import com.br.lead.collector.repositories.LeadRepository;
import com.br.lead.collector.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class LeadService {

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public Iterable<Produto> buscarTodosProdutos(List<Integer> produtosId){
        Iterable<Produto> produtosIterable = produtoRepository.findAllById(produtosId);
        return produtosIterable;
    }

    public Optional buscarPorId(int id){
        Optional<Lead> leadOptional = leadRepository.findById(id);
        return leadOptional;
    }
    public Lead cadastrarLead(Lead lead){
        Lead leadObjeto = leadRepository.save(lead);
        return leadObjeto;
    }

    public Iterable<Lead> buscarTodosLeads(){
        Iterable<Lead> leads = leadRepository.findAll();
        return leads;
    }

    public Lead atualizarLead(Lead lead){
        Optional<Lead> leadOptional = buscarPorId(lead.getId());
        if (leadOptional.isPresent()){
            Lead leadData = leadOptional.get();
            if(lead.getNome() == null){
                lead.setNome(leadData.getNome());
            }
            if(lead.getEmail() == null){
                lead.setEmail(leadData.getEmail());
            }
            if(lead.getTipoDeLead() == null){
                lead.setTipoDeLead(leadData.getTipoDeLead());
            }
        }
        Lead leadObjeto = leadRepository.save(lead);
        return leadObjeto;
    }

    public void deletarLead(Lead lead){
        leadRepository.delete(lead);
    }
}
