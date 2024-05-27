package com.mouad.stockmanagement.controller.api;

import static com.mouad.stockmanagement.utils.Constants.APP_ROOT;

import com.mouad.stockmanagement.dto.CategoryDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface CategoryApi {

    // @PostMapping : Indique que cette méthode doit répondre aux requêtes HTTP POST.
    // value: Définit l'URL de l'endpoint.
    // consumes: Spécifie que ce endpoint accepte des données au format JSON dans le corps de la requête (MediaType.APPLICATION_JSON_VALUE).
    // produces: Spécifie que ce endpoint retourne des données au format JSON (MediaType.APPLICATION_JSON_VALUE).
    @PostMapping(value = APP_ROOT + "/categories/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)

    // En effet, avec "springdoc-openapi-starter-webmvc-ui", toutes les requêtes des contrôleurs sont générées automatiquement dans Swagger (tapez le lien : http://localhost:8801/swagger-ui/index.html).
    // Cependant, si on veut personnaliser une requête d'un contrôleur, on peut procéder comme suit (exemple de création d'une nouvelle catégorie) :
    @Operation(
        description = "Create Post Category",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully created Category!",
                content = @Content(
                    mediaType ="application/json",
                    examples = {
                        @ExampleObject(
                            value = "{\"code\" : 200, \"Status\" : \"Ok!\", \"Message\" :\"Successfully created category!\"}"
                        ),
                    }
                )
            )
        }
    )
    // @RequestBody: Indique que l'argument "dto" doit être extrait du corps de la requête HTTP. Spring Boot désérialise automatiquement le JSON reçu dans une instance de "CategoryDto"
    CategoryDto save(@RequestBody CategoryDto dto);

    @GetMapping(value = APP_ROOT + "/categories/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    CategoryDto findById(@PathVariable("categoryId") Integer categoryId, HttpServletRequest request);

    @GetMapping(value = APP_ROOT + "/categories/filter/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    CategoryDto findByCode(@PathVariable("code") String code);

    @GetMapping(value = APP_ROOT + "/categories/all", produces = MediaType.APPLICATION_JSON_VALUE)
    List<CategoryDto> findAll();

    @DeleteMapping(value = APP_ROOT + "/categories/delete/{categoryId}")
    void delete(@PathVariable("categoryId") Integer id);

}
