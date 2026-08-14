package com.tienda.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.tienda.repository.UsuarioRepository;
import jakarta.serviet.http.HttpSession;
import java.util.Optional;
import com.tienda.domain.Usuario;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.stream.Collectors;
import org.springframework.security.core.userdetails.User;

@Service("userDetailsService")
public class UsuarioDetailsService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;
    private final HttpSession session;
    
    public UsuarioDetailsService(UsuarioRepository usuarioRepository, HttpSession session) {
        this.usuarioRepository = usuarioRepository;
        this.session = session;
    }

    @Override
    @Transaction(readOnly=true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    //Lo primero es buscar el usuario por username
    Usuario usuario = usuarioRepository.findByUsernameAndActivoTrue(username)
            .orElseThrow( () -> new UsernameNotFoundException("Usuario no encontrado "+username));

    //Si estamos acá, se encontró un usuario...
    //Se crea un atributo/variable de session para guardar la foto del usuario
    session.removeAttribute("usuarioImagen");
    session.setAttribute("usuarioImagen", usuario.getRutaImagen());

    //Ahora se toman los roles y se convierten en roles de seguridad...
    var roles = usuario.getRoles().stream().map( rol -> new SimpleGrantedAuthority("ROLE_"+rol.getRol()))
            .collect(Collectors.toSet());
    
    return new User(usuario.getUsername(), usuario.getPassword, roles);

}