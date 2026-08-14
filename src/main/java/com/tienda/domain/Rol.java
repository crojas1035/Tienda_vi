
package com.tienda.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name="rol")
public class Rol implements Serializable{
    
    private static final long serialVersionUID=1L;
    
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY )
    private Integer idRol;
    
    @Column (unique = true, nullable = false, length = 20)
    @NotNull
    @Size(max=20)
    private String rol;
}
