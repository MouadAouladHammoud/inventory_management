package com.mouad.stockmanagement.services.impl;

import com.mouad.stockmanagement.dto.CategoryDto;
import com.mouad.stockmanagement.exception.EntityNotFoundException;
import com.mouad.stockmanagement.exception.ErrorCodes;
import com.mouad.stockmanagement.exception.InvalidEntityException;
import com.mouad.stockmanagement.exception.InvalidOperationException;
import com.mouad.stockmanagement.model.Product;
import com.mouad.stockmanagement.repository.ProductRepository;
import com.mouad.stockmanagement.repository.CategoryRepository;
import com.mouad.stockmanagement.services.CategoryService;
import com.mouad.stockmanagement.validator.CategoryValidator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

// @Service : cela permet de définir que cette classe est de type "service" afin d'avoir une instance pour l'injecter dans les classes de contrôleur.
// NB: On peut utiliser l'annotation @Service avec un nom spécifique : @Service("CategoryServiceImpl1") lorsque on a plusieurs services d'implémentation étendant la classe "CategoryService"
//   car dans ce cas, nous devons spécifier au contrôleur le service d'implémentation à utiliser dans l'injection pour "CategoryService".
//   Dans l'application, il n'y a qu'un seul service d'implémentation ("CategoryServiceImpl"), donc il n'est pas nécessaire de nommer le service, car cela est défini automatiquement lors de l'injection d'une instance de "CategoryService" dans le contrôleur.
@Service
@Slf4j // utilisé pour générer des logs.
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;
    @Autowired // cela nous permet d'injecter une instance de la class concerné (la classe à injecter ici peut etre Service, Bean, Compoenet...) à notre service de l'implémentation
    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public CategoryDto save(CategoryDto dto) {
        List<String> errors = CategoryValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Product is not valid {}", dto);
            throw new InvalidEntityException("La category n'est pas valide", ErrorCodes.CATEGORY_NOT_VALID, errors);
        }
        return CategoryDto.fromEntity(
            categoryRepository.save(CategoryDto.toEntity(dto))
        );
    }

    @Override
    public CategoryDto findById(Integer id) {
        if (id == null) {
            log.error("Category ID is null");
            return null;
        }
        return categoryRepository.findById(id)
            .map(CategoryDto::fromEntity)
            .orElseThrow(() -> new EntityNotFoundException("Aucune category avec l'ID = " + id + " n' ete trouve dans la BDD", ErrorCodes.CATEGORY_NOT_FOUND));
    }

    @Override
    public CategoryDto findByCode(String code) {
        if (!StringUtils.hasLength(code)) {
            log.error("Category CODE is null");
            return null;
        }
        return categoryRepository.findCategoryByCode(code)
            .map(CategoryDto::fromEntity)
            .orElseThrow(() -> new EntityNotFoundException("Aucune category avec le CODE = " + code + " n' ete trouve dans la BDD", ErrorCodes.CATEGORY_NOT_FOUND));
    }

    @Override
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream()
            .map(CategoryDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Category ID is null");
            return;
        }
        List<Product> articles = productRepository.findAllByCategoryId(id);
        if (!articles.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer cette categorie qui est deja utilisé", ErrorCodes.CATEGORY_ALREADY_IN_USE);
        }
        categoryRepository.deleteById(id);
    }
}
