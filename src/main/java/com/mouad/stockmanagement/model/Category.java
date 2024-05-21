package com.mouad.stockmanagement.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data // Cette annotation est fournie par le projet lombok. Elle génère automatiquement les méthodes toString, equals, hashCode, les getters et setters pour tous les champs de la classe.
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true) // Cette annotation génère les méthodes "equals" et "hashCode" en utilisant tous les champs de la classe
@Entity // Cette annotation indique que la classe est une entité JPA, ce qui signifie qu'elle est associée à une table dans la base de données. Hibernate utilise cette annotation pour gérer la persistance de l'objet dans la base de données.
@Table(name = "categories") // Cette annotation spécifie le nom de la table dans la base de données à laquelle l'entité est associée
public class Category extends AbstractEntity {

    @Column(name = "code") // Cette annotation est utilisée pour mapper un champ de classe à une colonne dans la table de base de données
    private String code;

    @Column(name = "designation")
    private String designation;

    @Column(name = "company_id")
    private Integer companyId;

    // mappedBy = "category" => il doit correspondre exactement au nom de la propriété dans la classe "Product" qui fait référence à la classe "Category"
    // la propriété dans la classe "Product" est nommée "category", donc il faut utiliser "category" comme valeur de mappedBy dans la classe "Category".
    @OneToMany(mappedBy = "category") // Cette annotation définit une relation "un à plusieurs" entre la classe "Category" et la classe "Product".
    private List<Product> products;

    @PrePersist
    void prePersist() {

    }

    @PreUpdate
    void preUpdate() {

    }

}

/*
// @NoArgsConstructor: Cette annotation génère un constructeur sans argument pour la classe.
// Exemple :

@NoArgsConstructor
public class MyClass {
    private String name;
    private int age;
}

// Avec cette annotation, on peut créer une instance de "MyClass" sans fournir d'arguments au constructeur :
MyClass obj = new MyClass();
*/

/*
// @AllArgsConstructor: Cette annotation génère un constructeur prenant en charge tous les champs de la classe comme arguments.
// Exemple :

@AllArgsConstructor
public class Person {
    private String name;
    private int age;
    private String address;
}

// Avec cette annotation, on peut créer une instance de Person en spécifiant des valeurs pour tous ses champs :
Person person = new Person("John", 30, "123 Main Street");
*/