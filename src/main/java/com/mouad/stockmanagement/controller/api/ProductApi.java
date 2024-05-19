package com.mouad.stockmanagement.controller.api;
import static com.mouad.stockmanagement.utils.Constants.APP_ROOT;

import com.mouad.stockmanagement.dto.ProductDto;
import com.mouad.stockmanagement.dto.ClientOrderLineDto;
import com.mouad.stockmanagement.dto.SupplierOrderLineDto;
import com.mouad.stockmanagement.dto.SaleLineDto;

/*
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
*/

import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// @Api("products")
public interface ProductApi {
    @PostMapping(value = APP_ROOT + "/articles/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    /*
    @ApiOperation(value = "Enregistrer un article", notes = "Cette methode permet d'enregistrer ou modifier un article", response = ArticleDto.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "L'objet article cree / modifie"),
        @ApiResponse(code = 400, message = "L'objet article n'est pas valide")
    })
    */
    ProductDto save(@RequestBody ProductDto dto);

    @GetMapping(value = APP_ROOT + "/products/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    /*
    @ApiOperation(value = "Rechercher un article par ID", notes = "Cette methode permet de chercher un article par son ID", response = ArticleDto.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "L'article a ete trouve dans la BDD"),
        @ApiResponse(code = 404, message = "Aucun article n'existe dans la BDD avec l'ID fourni")
    })
    */
   ProductDto findById(@PathVariable("productId") Integer id);

    @GetMapping(value = APP_ROOT + "/products/filter/{productCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    /*
    @ApiOperation(value = "Rechercher un article par CODE", notes = "Cette methode permet de chercher un article par son CODE", response = ArticleDto.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "L'article a ete trouve dans la BDD"),
        @ApiResponse(code = 404, message = "Aucun article n'existe dans la BDD avec le CODE fourni")
    })
    */
    ProductDto findProductsByCode(@PathVariable("productCode") String codeArticle); // findByCodeArticle

    @GetMapping(value = APP_ROOT + "/products/all", produces = MediaType.APPLICATION_JSON_VALUE)
    /*
    @ApiOperation(value = "Renvoi la liste des articles", notes = "Cette methode permet de chercher et renvoyer la liste des articles qui existent " + "dans la BDD", responseContainer = "List<ArticleDto>")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "La liste des article / Une liste vide")
    })
    */
    List<ProductDto> findAll();

    @GetMapping(value = APP_ROOT + "/products/sales/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<SaleLineDto> findSales(@PathVariable("productId") Integer productId); // findHistoriqueVentes

    @GetMapping(value = APP_ROOT + "/products/historical/client/order/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<ClientOrderLineDto> findClientOrders(@PathVariable("productId") Integer productId); // findHistoriaueCommandeClient

    @GetMapping(value = APP_ROOT + "/products/historical/supplier/order/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<SupplierOrderLineDto> findSupplierOrders(@PathVariable("productId") Integer productId); // findHistoriqueCommandeFournisseur

    @GetMapping(value = APP_ROOT + "/products/filter/category/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<ProductDto> findAllProductsByIdCategory(@PathVariable("productId") Integer idCategory); // findAllArticleByIdCategory

    @DeleteMapping(value = APP_ROOT + "/products/delete/{productId}")
    /*
    @ApiOperation(value = "Supprimer un article", notes = "Cette methode permet de supprimer un article par ID")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "L'article a ete supprime")
    })
    */
    void delete(@PathVariable("productId") Integer id);
}
