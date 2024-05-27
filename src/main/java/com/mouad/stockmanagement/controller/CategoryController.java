package com.mouad.stockmanagement.controller;

import com.mouad.stockmanagement.auth.jwt;
import com.mouad.stockmanagement.controller.api.CategoryApi;
import com.mouad.stockmanagement.dto.CategoryDto;
import com.mouad.stockmanagement.services.CategoryService;

import java.util.List;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController // Indique que cette classe est un contrôleur REST pour traiter les requêtes HTTP
// @RequestMapping("/app/") // En utilisant cette annotation, cette classe ne traite que les requêtes HTTP qui commencent par "/app/"
@RequiredArgsConstructor
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

    private final CategoryService categoryService;
    private final jwt.JwtService jwtService;

    /*
    // Pour injecter les deux services (CategoryService et JwtService) dans le constructeur, vous pouvez soit le faire manuellement comme dans l'exemple ci-dessous, soit utiliser l'annotation @RequiredArgsConstructor qui génère automatiquement un constructeur pour les champs marqués comme "final" ou avec l'annotation @NonNull.
    @Autowired // Permet d'injecter une instance de la classe concernée (qui peut être un Service, un Bean, un Component, etc.) dans notre contrôleur
    public CategoryController(jwt.JwtService jwtService, CategoryService categoryService) {
        this.categoryService = categoryService;
        this.jwtService = jwtService;
    }
    */

    @Override
    public CategoryDto save(CategoryDto dto) {
        return categoryService.save(dto);
    }

    @Override
    public CategoryDto findById(Integer categoryId, HttpServletRequest request) {
        System.out.println("**** From CategoryController ****");
        System.out.println("**************************************************************************************");
        System.out.println("**************************************************************************************");

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("email : " + email);

        final String authHeader = request.getHeader("Authorization");
        final String jwt = authHeader.substring(7);
        System.out.println("jwt : " + jwt);

        Object payload = Jwts.parser().setSigningKey("449e389b00ceb8880a8e64916a87dbe349f603890bcab1a98fb1729109cb83f9").parseClaimsJws(jwt).getBody();
        System.out.println("payload : " + payload);

        Jws<Claims> jwsClaims = Jwts.parser().setSigningKey("449e389b00ceb8880a8e64916a87dbe349f603890bcab1a98fb1729109cb83f9").parseClaimsJws(jwt);
        Claims claims = jwsClaims.getBody();
        System.out.println("claims : " + claims);
        Integer userId = claims.get("userId", Integer.class);

        System.out.println("**************************************************************************************");
        System.out.println("**************************************************************************************");
        return categoryService.findById(categoryId, userId);
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
