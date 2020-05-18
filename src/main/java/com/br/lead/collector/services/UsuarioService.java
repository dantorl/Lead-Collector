package com.br.lead.collector.services;

import com.br.lead.collector.models.Usuario;
import com.br.lead.collector.repositories.UsuarioRepository;
import com.br.lead.collector.security.DetalhesUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public Usuario salvarUsuario(Usuario usuario){
        Usuario userObj = usuarioRepository.findByEmail(usuario.getEmail());
        if(userObj != null){
            throw new RuntimeException("Esse email já existe");
        }


        String senha = usuario.getSenha();
        String encode = bCryptPasswordEncoder.encode(senha);
        usuario.setSenha(encode);
        return usuarioRepository.save(usuario);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if(usuario == null){
            throw new UsernameNotFoundException(email);
        }
        DetalhesUsuario usuarioDetails = new DetalhesUsuario(usuario.getId(), usuario.getEmail(), usuario.getSenha());

        return usuarioDetails;
    }
}
