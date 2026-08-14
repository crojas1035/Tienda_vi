package com.tienda.service;

import com.tienda.domain.Constante;
import com.tienda.repository.ConstanteRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConstanteService {

    private final ConstanteRepository constanteRepository;

    public ConstanteService(ConstanteRepository constanteRepository) {
        this.constanteRepository = constanteRepository;
    }

    @Transactional(readOnly = true)
    public List<Constante> getConstantes() {
        var lista = constanteRepository.findAll();
        return lista;
    }

    @Transactional(readOnly = true)
    public Constante getConstante(Integer idConstante) {
        return constanteRepository.findById(idConstante).orElseThrow(
            () -> new NoSuchElementException("Constante con ID " + idConstante + " no encontrada."));
    }

    @Transactional
    public void save(Constante constante) {
        constanteRepository.save(constante);
    }

    @Transactional
    public void delete(Integer idConstante) {
        // Verifica si la categoría existe antes de intentar eliminarlo
        if (!constanteRepository.existsById(idConstante)) {
            // Lanza una excepción para indicar que el usuario no fue encontrado
            throw new IllegalArgumentException("La Constante con ID " + idConstante + " no existe.");
        }
        try {
            constanteRepository.deleteById(idConstante);
        } catch (DataIntegrityViolationException e) {
            // Lanza una nueva excepción para encapsular el problema de integridad de datos
            throw new IllegalStateException("No se puede eliminar la constante. Tiene datos asociados.", e);
        }
    }

    @Transactional(readOnly = true)
    public Optional<Constante> findByAtributo(String atributo) {
        return constanteRepository.findByAtributo(atributo);
    }
    
    //Se busca un atributo en la tabla de constantes y se devuelve su valor...
    @Transactional(readOnly = true)
    public String demeValorDeAtributo(String atributo) {
        Optional<Constante> registro= constanteRepository.findByAtributo(atributo);
        if (registro.isPresent()) {
            return registro.get().getValor();
        }
        return "";
    }

    //Convierte un monto en colones a dólares
    public BigDecimal demeDolares(BigDecimal montoColones) {
        String tCambio=demeValorDeAtributo("dolar");
        double dolar = (tCambio.isBlank() || tCambio.isEmpty())?500.00:Double.parseDouble(tCambio);
        return montoColones.divide(BigDecimal.valueOf(dolar), RoundingMode.CEILING);
    }
}
