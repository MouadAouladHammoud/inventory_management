package com.mouad.stockmanagement.controller.api;
import static com.mouad.stockmanagement.utils.Constants.APP_ROOT;

import com.mouad.stockmanagement.dto.ProductDto;
import com.mouad.stockmanagement.dto.ClientOrderLineDto;
import com.mouad.stockmanagement.dto.SupplierOrderLineDto;
import com.mouad.stockmanagement.dto.SaleLineDto;

import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface ProductApi {
    @PostMapping(value = APP_ROOT + "/articles/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ProductDto save(@RequestBody ProductDto dto);

    @GetMapping(value = APP_ROOT + "/products/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
   ProductDto findById(@PathVariable("productId") Integer id);

    @GetMapping(value = APP_ROOT + "/products/filter/{productCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    ProductDto findProductsByCode(@PathVariable("productCode") String codeArticle); // findByCodeArticle

    @GetMapping(value = APP_ROOT + "/products/all", produces = MediaType.APPLICATION_JSON_VALUE)
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
    void delete(@PathVariable("productId") Integer id);
}
