
package com.tienda.repository;

import com.tienda.domain.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Integer>{
    
    //Se crea una consulta derivada para recuperar los registros activas...
    public Optional<Usuario> findByUsernameAndActivoTrue(String username);
}
