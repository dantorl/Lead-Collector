package com.br.lead.collector.services;

import com.br.lead.collector.enums.TipoDeLead;
import com.br.lead.collector.models.Lead;
import com.br.lead.collector.models.Produto;
import com.br.lead.collector.repositories.LeadRepository;
import com.br.lead.collector.repositories.ProdutoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class LeadServiceTest {

    Lead lead;
    Lead lead2;
    Optional leadOptional;
    Produto produto;
    Produto produto2;

    @MockBean
    LeadRepository leadRepository;

    @MockBean
    ProdutoRepository produtoRepository;

    @Autowired
    LeadService leadService;



    @BeforeEach
    public void inicializar() {
        lead = new Lead();
        lead.setId(1);
        lead.setTipoDeLead(TipoDeLead.QUENTE);
        lead.setEmail("daniel@hotmail");
        lead.setNome("Daniel");

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

        lead.setProdutos(Arrays.asList(produto, produto2));

        leadOptional = Optional.of(lead);

        lead2 = new Lead();
        lead2.setId(1);
        lead2.setTipoDeLead(TipoDeLead.QUENTE);
        lead2.setEmail("daniel@hotmail");
        lead2.setNome("Manu");
        lead2.setProdutos(Arrays.asList(new Produto()));

    }

    @Test
    public void testarCadastrarLead(){

        Mockito.when(leadRepository.save(Mockito.any(Lead.class))).thenReturn(lead);

        Lead leadObjeto = leadService.cadastrarLead(lead);

        Assertions.assertEquals(lead, leadObjeto);
        Assertions.assertEquals(lead.getEmail(), leadObjeto.getEmail());
        Assertions.assertEquals(lead.getNome(), leadObjeto.getNome());
        Assertions.assertEquals(lead.getTipoDeLead(), leadObjeto.getTipoDeLead());
        Assertions.assertEquals(lead.getProdutos(), leadObjeto.getProdutos());
        Assertions.assertEquals(lead.getId(), leadObjeto.getId());
    }
    @Test
    public void testarDeletarLead(){
        leadService.deletarLead(lead);
        Mockito.verify(leadRepository, Mockito.times(1)).delete(Mockito.any(Lead.class));
    }
    @Test
    public void buscarLeadById(){

        Mockito.when(leadRepository.findById(Mockito.anyInt())).thenReturn(leadOptional);

        Optional<Lead> leadOptional2 = leadService.buscarPorId(lead.getId());
        Assertions.assertEquals(leadOptional, leadOptional2);

    }
    @Test
    public void buscarTodosLead(){
        Iterable<Lead> leadIterable = Arrays.asList(lead,lead2);

        Mockito.when(leadRepository.findAll()).thenReturn(leadIterable);

        Iterable<Lead> leadIterable2 = leadService.buscarTodosLeads();
        Assertions.assertEquals(leadIterable, leadIterable2);
    }
    @Test
    public void buscarTodosProdutos(){
        List<Integer> list = Arrays.asList(1,2);
        Iterable<Produto> produtosIterable = Arrays.asList(produto, produto2);

        Mockito.when(produtoRepository.findAllById(Mockito.anyList())).thenReturn(produtosIterable);

        Iterable<Produto> produtosIterable2 = leadService.buscarTodosProdutos(list);
        Assertions.assertEquals(produtosIterable, produtosIterable2);
    }

    @Test
    public void TestarAtualizarLead(){
        Mockito.when(leadRepository.save(Mockito.any(Lead.class))).thenReturn(lead);
        Mockito.when(leadRepository.findById(Mockito.anyInt())).thenReturn(leadOptional);

        Lead leadObjeto = leadService.atualizarLead(lead);

        Assertions.assertEquals(lead, leadObjeto);
    }
    @Test
    public void TestarAtualizarLeadErro(){
        Optional<Lead> leadOptional2 = Optional.of(lead);

        Mockito.when(leadRepository.save(Mockito.any(Lead.class))).thenReturn(lead);
        Mockito.when(leadRepository.findById(Mockito.anyInt())).thenReturn(leadOptional);

        Lead lead3 = lead;
        lead3.setNome(null);
        Lead leadObjeto = leadService.atualizarLead(lead3);
        Assertions.assertEquals(lead3, leadObjeto);

        lead3.setEmail(null);
        leadObjeto = leadService.atualizarLead(lead3);
        Assertions.assertEquals(lead3, leadObjeto);

        lead3.setTipoDeLead(null);
        leadObjeto = leadService.atualizarLead(lead3);
        Assertions.assertEquals(lead3, leadObjeto);
     }
}
