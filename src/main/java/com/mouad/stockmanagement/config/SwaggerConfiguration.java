package com.mouad.stockmanagement.config;

import static com.mouad.stockmanagement.utils.Constants.APP_ROOT;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition // Cette annotation est utilisée pour fournir des informations de base pour la documentation OpenAPI, Elle peut inclure des détails tels que les informations de licence, la version, etc. (cela ici pour la page de swagger)
@Configuration // Cette annotation indique que la classe contient des configurations Spring, cela s'exécute toujours au démarrage de l'application
public class SwaggerConfiguration {

    @Bean // Cette annotation indique que la méthode "baseOpenAPI" produit un bean géré par le conteneur Spring. Ce bean sera disponible dans le contexte de l'application Spring.
    public OpenAPI baseOpenAPI() {
        return new OpenAPI().info(new Info().title("Spring Doc") // le titre de la documentation de l'API (Swagger)
            .version("1.0.0") // la version de l'API (Swagger)
            .description("Spring App doc") // description de l'API.
        );
    }
}
