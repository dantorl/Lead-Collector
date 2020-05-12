package com.br.lead.collector.repositories;

import com.br.lead.collector.models.Produto;
import org.springframework.data.repository.CrudRepository;

public interface ProdutoRepository extends CrudRepository<Produto, Integer> {
}
