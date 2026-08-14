package com.tienda;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.context.annotation.Lazy;


@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, @Lazy RutaService rutaService) throws Exception {
        
        var rutas = rutaService.getRutas();
        //Se establecen cuales rutas se acceden desde qué roles...
        http.authorizeHttpRequests(request ->  {
                for (Ruta ruta : rutas) { 
                    if (ruta.isRequiereRol()) {
                        request.requestMatchers(ruta.getRuta()).hasRole(ruta.getRol().getRol());
                    } else {
                        request.requestMatchers(ruta.getRuta()).permitAll();
                    }
                }
                request.anyRequest().authenticated();
        });

        //Se establece el proceso para hacer "login"
        http.formLogin(login -> login
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
        );

        //Se establece el proceso para hacer "logout"
        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
        );

        //Se establece el recurso para cuando hay alguna "excepción"
        http.exceptionHandling(ex -> ex.accessDeniedPage("/acceso_denegado"));

        //Se establece qué hacer con sesiones concurrentes
        http.sessionManagement(ses -> ses
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
        );

        return http.build();
    }
    
    //Se define el metodo para encriptar la clave
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    // Este metodo se usa en el login
    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder build, @Lazy PasswordEncoder passwordEncoder, @Lazy UserDetailsService userDetailsService) { 
        build.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
    }
     
}
