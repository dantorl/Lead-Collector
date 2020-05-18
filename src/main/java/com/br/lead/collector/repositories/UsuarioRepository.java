package com.br.lead.collector.repositories;

import com.br.lead.collector.models.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<Usuario, Integer>  {

    Usuario findByEmail(String email);

}


