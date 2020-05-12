package com.br.lead.collector.repositories;

import com.br.lead.collector.models.Lead;
import org.springframework.data.repository.CrudRepository;

public interface LeadRepository extends CrudRepository<Lead, Integer> {
}
