package com.mouad.stockmanagement.dto;

import com.mouad.stockmanagement.model.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/*
DTO (Data Transfer Object) : L'utilisation de DTOs permet de définir un modèle de données spécifique aux besoins de l'API et de séparer les objets métier (entités JPA) des objets transférés via l'API.

JPA (Java Persistence API) : Le code utilise des entités JPA pour représenter les données persistantes de l'application.
    Les méthodes statiques "fromEntity" et "toEntity" sont utilisées pour convertir respectivement une entité "Category" en un objet "CategoryDto" pour l'utilisation dans l'API, et pour convertir un objet "CategoryDto" en une entité "Category" pour la persistance dans la base de données.
    Cela facilite le transfert des données entre la couche de persistance (entités JPA) et la couche de présentation (DTO) de l'application.
*/

@Data // Cette annotation est fournie par Lombok et génère automatiquement les méthodes toString, equals, hashCode et les getters/setters pour toutes les propriétés de la classe.
@Builder // Cette annotation est fournie par Lombok et génère un constructeur de type builder pour la classe, ce qui facilite la création d'instances de la classe avec un style de construction fluide.
public class CategoryDto {
    private Integer id;
    private String code;
    private String designation;
    private Integer companyId;

    // @JsonIgnore : indique que la propriété annotée doit être ignorée lors de la sérialisation ou de la désérialisation JSON.
    //   Autrement dit, quand un objet de cette classe est converti en JSON, cette propriété ne sera pas incluse dans le résultat JSON.
    //   Inversement, quand un JSON est converti en objet Java, cette propriété ne sera pas initialisée à partir du JSON.
    @JsonIgnore
    private List<ProductDto> articles; // Une liste des produits associés à cette catégorie.

    // fromEntity(Category category) : Méthode statique permettant de convertir une entité "Category" en un objet "CategoryDto".
    public static CategoryDto fromEntity(Category category) {
        if (category == null) {
            return null;
            // TODO throw an exception
        }
        return CategoryDto.builder()
            .id(category.getId())
            .code(category.getCode())
            .designation(category.getDesignation())
            .companyId(category.getCompanyId())
            .build();
    }

    // toEntity(CategoryDto categoryDto) : Méthode statique permettant de convertir un objet CategoryDto en une entité Category.
    public static Category toEntity(CategoryDto categoryDto) {
        if (categoryDto == null) {
            return null;
            // TODO throw an exception
        }
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setCode(categoryDto.getCode());
        category.setDesignation(categoryDto.getDesignation());
        category.setCompanyId(categoryDto.getCompanyId());
        return category;
    }
}
