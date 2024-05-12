package com.mouad.stockmanagement.dto;

import com.mouad.stockmanagement.model.Supplier;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SupplierDto {
    private Integer id;
    private String lastName;
    private String firstName;
    private AddressDto address;
    private String photoUrl;
    private String email;
    private String phoneNumber;
    private Integer companyId;

    @JsonIgnore
    private List<SupplierOrderDto> supplierOrders;

    public static SupplierDto fromEntity(Supplier supplier) {
        if (supplier == null) {
            return null;
        }
        return SupplierDto.builder()
            .id(supplier.getId())
            .lastName(supplier.getLastName())
            .firstName(supplier.getFirstName())
            .address(AddressDto.fromEntity(supplier.getAddress()))
            .photoUrl(supplier.getPhotoUrl())
            .email(supplier.getEmail())
            .phoneNumber(supplier.getPhoneNumber())
            .companyId(supplier.getCompanyId())
            .build();
    }

    public static Supplier toEntity(SupplierDto dto) {
        if (dto == null) {
            return null;
        }
        Supplier supplier = new Supplier();
        supplier.setId(dto.getId());
        supplier.setLastName(dto.getLastName());
        supplier.setFirstName(dto.getFirstName());
        supplier.setAddress(AddressDto.toEntity(dto.getAddress()));
        supplier.setPhotoUrl(dto.getPhotoUrl());
        supplier.setEmail(dto.getEmail());
        supplier.setPhoneNumber(dto.getPhoneNumber());
        supplier.setCompanyId(dto.getCompanyId());
        return supplier;
    }
}
