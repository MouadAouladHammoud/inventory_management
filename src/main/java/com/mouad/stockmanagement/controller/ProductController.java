package com.mouad.stockmanagement.controller;

import com.mouad.stockmanagement.controller.api.ProductApi;
import com.mouad.stockmanagement.dto.ProductDto;
import com.mouad.stockmanagement.dto.ClientOrderLineDto;
import com.mouad.stockmanagement.dto.SupplierOrderLineDto;
import com.mouad.stockmanagement.dto.SaleLineDto;
import com.mouad.stockmanagement.services.ProductService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController implements ProductApi {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public ProductDto save(ProductDto dto) {
        return productService.save(dto);
    }

    @Override
    public ProductDto findById(Integer id) {
        return productService.findById(id);
    }

    @Override
    public ProductDto findProductsByCode(String codeArticle) {
        return productService.findProductsByCode(codeArticle);
    }

    @Override
    public List<ProductDto> findAll() {
        return productService.findAll();
    }

    @Override
    public List<SaleLineDto> findSales(Integer productId) {
        return productService.findSales(productId);
    }

    @Override
    public List<ClientOrderLineDto> findClientOrders(Integer productId) {
        return productService.findClientOrders(productId);
    }

    @Override
    public List<SupplierOrderLineDto> findSupplierOrders(Integer productId) {
        return productService.findSupplierOrders(productId);
    }

    @Override
    public List<ProductDto> findAllProductsByIdCategory(Integer categoryId) {
        return productService.findAllProductsByIdCategory(categoryId);
    }

    @Override
    public void delete(Integer id) {
        productService.delete(id);
    }
}
