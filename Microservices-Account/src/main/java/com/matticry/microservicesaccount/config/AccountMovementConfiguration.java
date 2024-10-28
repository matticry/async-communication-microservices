package com.matticry.microservicesaccount.config;

import com.matticry.microservicesaccount.dto.AccountResponseDTO;
import com.matticry.microservicesaccount.dto.MovementResponseDTO;
import com.matticry.microservicesaccount.models.Account;
import com.matticry.microservicesaccount.models.Movement;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "API CUENTA-MOVIMIENTO",
        description = "Prueba Tecnica Microservicios",
        termsOfService = "www.programacion.com/terminos_y_condiciones",
        version = "1.0.0",
        contact = @Contact(
                name = "Mateo Eras",
                url = "https://test-ejemplo.com",
                email = "matticry1@gmail.com"
        ),
        license = @License(
                name = "Standard Software Use License for programador",
                url = "www.programacion.com/licence"
        )
))
public class AccountMovementConfiguration implements WebMvcConfigurer {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setSkipNullEnabled(true)  // Salta propiedades nulas
                .setAmbiguityIgnored(true) // Ignora ambigüedad en el mapeo
                .setMatchingStrategy(MatchingStrategies.STRICT); // Estrategia de mapeo estricta

        // Configuramos mapeos específicos
        TypeMap<Account, AccountResponseDTO> accountMapping = modelMapper.createTypeMap(Account.class, AccountResponseDTO.class);
        accountMapping.addMappings(mapper -> mapper.skip(AccountResponseDTO::setClient));  // Ignoramos el mapeo del cliente

        TypeMap<Movement, MovementResponseDTO> movementMapping = modelMapper.createTypeMap(Movement.class, MovementResponseDTO.class);
        movementMapping.addMappings(mapper -> mapper.skip(MovementResponseDTO::setAccount));  // Ignoramos el mapeo de la cuenta

        return modelMapper;
    }

    @Bean("accountRestTemplate")  // Renombrado el bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
