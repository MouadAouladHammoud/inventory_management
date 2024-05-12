package com.mouad.stockmanagement.repository;

import com.mouad.stockmanagement.model.Category;

import java.util.List;
import java.util.Optional;

import com.mouad.stockmanagement.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/*
extends JpaRepository<Category, Integer> : Cela signifie que "CategoryRepository" hérite des méthodes définies dans "JpaRepository".
    "JpaRepository" est une interface fournie par Spring Data JPA qui fournit des méthodes de base pour effectuer des opérations CRUD (Create, Read, Update, Delete) sur une entité dans une base de données.
    Dans ce cas, l'entité est "Category" et le type de son identifiant(id) est "Integer".
    On peut également exécuter d'autres méthodes prédéfinies provenant de "JpaRepository" sur notre modèle (entité) "Category"
*/

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    // Optional<Category>: Cela indique que la méthode peut retourner une catégorie, mais elle peut également ne rien retourner si aucune catégorie ne correspond au code spécifié.
    // findCategoryByCode(String code): C'est le nom de la méthode. Spring Data JPA générera automatiquement une requête SQL pour rechercher une catégorie en fonction du code spécifié.
    Optional<Category> findCategoryByCode(String code);


    // @Query: Cette annotation permet de définir une requête personnalisée en utilisant JPQL (Java Persistence Query Language) dans une méthode de repository.
    // findByCustomQuery: C'est le nom de la méthode qui sera utilisé pour invoquer une requête personnalisée.
    // NB: "findByCustomQuery" utilise JPQL (Java Persistence Query Language), une syntaxe similaire au SQL mais spécifique à JPA. Cette requête JPQL est automatiquement transformée en une requête SQL correspondante par le framework de persistance lors de son exécution
    @Query("SELECT p from Product p WHERE p.code = :code AND p.company_id = :company_id")
    List<Product> findByCustomQuery(@Param("code") String c, @Param("company_id") Integer cp);

    // @Query: Cette annotation permet de définir une requête personnalisée en utilisant JPQL (Java Persistence Query Language) dans une méthode de repository.
    // findByCustomQuery: C'est le nom de la méthode qui sera utilisé pour invoquer une requête personnalisée.
    // NB: "findByCustomNativeQuery" utilise une requête SQL native, écrite directement en SQL.
    @Query(value = "SELECT * FROM Product p WHERE p.code = :code AND p.company_id = :company_id", nativeQuery = true)
    List<Product> findByCustomNativeQuery(@Param("code") String c, @Param("company_id") Integer cp);

    // on peut aussi exécuter d'autres méthodes sur l'entité "Category" qui sont déjà définies grâce à "JpaRepository" comme suit :
    // NB: Il se base toujours sur les propriétés du modèle (entité) "Category" pour générer les méthodes prédéfinies, car celui qu'on a utilisé dans l'interface "JpaRepository" => extends JpaRepository<Category, Integer>
    List<Product> findByCodeAndCompanyId(String code, Integer CompanyId);
    List<Product> findByCodeIgnoreCaseAndCompanyId(String code, Integer CompanyId);

}