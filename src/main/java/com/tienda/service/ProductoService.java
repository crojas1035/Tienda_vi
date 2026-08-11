
package com.tienda.service;

import com.tienda.domain.Producto;
import com.tienda.repository.ProductoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductoService {
    //Se enlaza el repositorio de producto
    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

@Transactional (readOnly=true) 
public List<Producto> getProductos(boolean activo){
        
    if (activo){ //Solo quiero las productos activas
        return productoRepository.findByActivoTrue();
    }
    return productoRepository.findAll();
    }
}
