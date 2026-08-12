
package com.tienda.service;

import com.tienda.domain.Categoria;
import com.tienda.repository.CategoriaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaService {
    //Se enlaza el repositorio de categoria
    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

@Transactional (readOnly=true) 
public List<Categoria> getCategorias(boolean activo){
        
    if (activo){ //Solo quiero las categorias activas
        return categoriaRepository.findByActivoTrue();
    }
    return categoriaRepository.findAll();
    }

@Transactionsl(readOnly = true)
public Optional<Categoria> getCategoria(Integer idCategoria) { 
    return categoriaRepository.findById(idCategoria);
}

}
