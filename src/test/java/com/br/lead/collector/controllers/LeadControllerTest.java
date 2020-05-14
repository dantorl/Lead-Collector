package com.br.lead.collector.controllers;

import com.br.lead.collector.enums.TipoDeLead;
import com.br.lead.collector.models.Lead;
import com.br.lead.collector.models.Produto;
import com.br.lead.collector.repositories.LeadRepository;
import com.br.lead.collector.services.LeadService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.dynamic.DynamicType;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.CleanupFailureDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.swing.text.html.Option;
import java.util.Arrays;
import java.util.Optional;

@WebMvcTest(LeadController.class)
public class LeadControllerTest {

    @MockBean
    LeadService leadService;

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    Lead lead;
    Lead lead2;
    Lead lead3;
    Produto produto;

    @BeforeEach
    public void inciciar(){
        lead = new Lead();
        lead.setId(1);
        lead.setTipoDeLead(TipoDeLead.QUENTE);
        lead.setEmail("daniel@hotmail");
        lead.setNome("Daniel Torquato");

        produto = new Produto();
        produto.setNome("Cafe");
        produto.setId(1);
        produto.setPreco(10.00);
        produto.setDescricao("Cafe especial");

        lead.setProdutos(Arrays.asList(produto));

        lead2 = new Lead();
        lead2.setId(2);
        lead2.setTipoDeLead(TipoDeLead.QUENTE);
        lead2.setEmail("danielhotmail");
        lead2.setNome("Daniel Torquato");
        lead2.setProdutos(Arrays.asList(produto));

        lead3 = new Lead();
        lead3.setId(3);
        lead3.setTipoDeLead(TipoDeLead.QUENTE);
        lead3.setEmail("daniel@hotmail");
        lead3.setNome("Dani");
        lead3.setProdutos(Arrays.asList(produto));
    }

    @Test
    public void testarCadastroDeLead() throws Exception {

        Iterable<Produto> produtoIterable = Arrays.asList(produto);

        Mockito.when(leadService.cadastrarLead(Mockito.any(Lead.class))).thenReturn(lead);
        Mockito.when(leadService.buscarTodosProdutos(Mockito.anyList())).thenReturn(produtoIterable);

        String json = mapper.writeValueAsString(lead);

        mockMvc.perform(MockMvcRequestBuilders.post("/leads")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.equalTo("daniel@hotmail")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.produtos[0].id", CoreMatchers.equalTo(1)));

        json = mapper.writeValueAsString(lead2);
        mockMvc.perform(MockMvcRequestBuilders.post("/leads")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        json = mapper.writeValueAsString(lead3);
        mockMvc.perform(MockMvcRequestBuilders.post("/leads")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void testarBuscarLead() throws Exception {
        Optional<Lead> leadOptional = Optional.of(lead);


        Mockito.when(leadService.buscarPorId(Mockito.anyInt())).thenReturn(leadOptional);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/leads/" + lead.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.produtos[0].id", CoreMatchers.equalTo(1)));
    }

    @Test
    public void testarBuscarLeadErro() throws Exception {
        Optional<Lead> leadOptional = Optional.empty();
        Mockito.when(leadService.buscarPorId(Mockito.anyInt())).thenReturn(leadOptional);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/leads/"+lead.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testarBuscarTodosLeads() throws Exception {

        Iterable<Lead> leadIterable = Arrays.asList(lead,lead2,lead3);

        Mockito.when(leadService.buscarTodosLeads()).thenReturn(leadIterable);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/leads"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id", CoreMatchers.equalTo(1)));
    }

    @Test
    public void testarAtualizarLead() throws Exception {
        Optional<Lead> leadOptional = Optional.of(lead);

        lead.setNome("Teste Daniel");

        Mockito.when(leadService.atualizarLead(Mockito.any(Lead.class))).thenReturn(lead);
        Mockito.when(leadService.buscarPorId(Mockito.anyInt())).thenReturn(leadOptional);

        String json = mapper.writeValueAsString(lead);

        mockMvc.perform(MockMvcRequestBuilders.put("/leads/"+lead.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome", CoreMatchers.equalTo("Teste Daniel")));

    }

    @Test
    public void testarDeleteLead() throws Exception {
        Optional<Lead> leadOptional = Optional.of(lead);

        Mockito.when(leadService.buscarPorId(Mockito.anyInt())).thenReturn(leadOptional);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/leads/"+lead.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.equalTo(1)));
        Mockito.verify(leadService, Mockito.times(1)).deletarLead(Mockito.any(Lead.class));

        leadOptional = Optional.empty();

        Mockito.when(leadService.buscarPorId(Mockito.anyInt())).thenReturn(leadOptional);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/leads/"+lead.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Mockito.verify(leadService, Mockito.times(1)).deletarLead(Mockito.any(Lead.class));
    }

}
