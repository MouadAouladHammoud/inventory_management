package com.mouad.stockmanagement.services;

import com.mouad.stockmanagement.dto.ProductDto;
import com.mouad.stockmanagement.dto.ClientOrderLineDto;
import com.mouad.stockmanagement.dto.SupplierOrderLineDto;
import com.mouad.stockmanagement.dto.SaleLineDto;
import java.util.List;

public interface ProductService {
    ProductDto save(ProductDto dto);
    ProductDto findById(Integer id);
    ProductDto findProductsByCode(String productCode);
    List<ProductDto> findAll();
    List<SaleLineDto> findSales(Integer productId);
    List<ClientOrderLineDto> findClientOrders(Integer productId);
    List<SupplierOrderLineDto> findSupplierOrders(Integer productId);
    List<ProductDto> findAllProductsByIdCategory(Integer productId);
    void delete(Integer id);
}
