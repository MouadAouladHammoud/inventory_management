package com.mouad.stockmanagement.controller;

import com.mouad.stockmanagement.controller.api.CategoryApi;
import com.mouad.stockmanagement.dto.CategoryDto;
import com.mouad.stockmanagement.services.CategoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // pour definir que cette class controller Rest pour pouvoir traiter les requettes de type HTTP
// @RequestMapping("/app/") // si je met cette annotation, c-v-d cette classe ne traite que les requettes de HTTPS qui commencent par "/app/"
public class CategoryController implements CategoryApi {

    /*
    // NB: il existe trois méthodes pour injecter des services dans le contrôleur
    // 1: Field Ingection
    @Autowired
    public CategoryService categoryService;

    // 2: Getter Injection
    @Autowired
    public CategoryService getCategoryService() {
        return productService;
    }

    // 3: Constructor Injection
    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    */

    /*
    // L'injection dans le cas où plusieurs implémentations de services étendent le même service :
    // Supposons que nous ayons plusieurs implémentations de services ("CategoryServiceImpl1" et "CategoryServiceImpl2") qui étendent le même service ("CategoryService").
    //   Ici, il est nécessaire de spécifier quel service d'implémentation parmi les deux doit être injecté pour le "categoryService".
    //   C'est pourquoi nous avons dû ajouter une annotation @Qualifier pour spécifier quel service d'implémentation doit être injecté et utilisé.
    // S'il n'y a qu'un seul service d'implémentation disponible pour "categoryService", alors dans ce cas, il n'est pas nécessaire d'utiliser l'annotation @Qualifier, le service d'implémentation sera automatiquement déterminé
    @Autowired
    @Qualifier("CategoryServiceImpl1") // Dans ce contrôleur, j'ai choisi "CategoryServiceImpl1" pour l'utiliser dans "categoryService".
    private CategoryService categoryService;
    */

    private CategoryService categoryService;
    @Autowired // Cela nous permet d'injecter une instance de la classe concernée (la classe à injecter ici peut être un Service, un Bean, un Component...) dans notre contrôleur
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public CategoryDto save(CategoryDto dto) {
        return categoryService.save(dto);
    }

    @Override
    public CategoryDto findById(Integer categoryId) {
        return categoryService.findById(categoryId);
    }

    @Override
    public CategoryDto findByCode(String code) {
        return categoryService.findByCode(code);
    }

    @Override
    public List<CategoryDto> findAll() {
        return categoryService.findAll();
    }

    @Override
    public void delete(Integer id) {
        categoryService.delete(id);
    }
}
