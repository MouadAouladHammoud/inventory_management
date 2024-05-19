package com.mouad.stockmanagement.services.impl;

import com.mouad.stockmanagement.dto.SupplierDto;
import com.mouad.stockmanagement.exception.EntityNotFoundException;
import com.mouad.stockmanagement.exception.ErrorCodes;
import com.mouad.stockmanagement.exception.InvalidEntityException;
import com.mouad.stockmanagement.exception.InvalidOperationException;
import com.mouad.stockmanagement.model.ClientOrder;
import com.mouad.stockmanagement.model.SupplierOrder;
import com.mouad.stockmanagement.repository.SupplierOrderRepository;
import com.mouad.stockmanagement.repository.SupplierRepository;
import com.mouad.stockmanagement.services.SupplierService;
import com.mouad.stockmanagement.validator.SupplierValidator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    private SupplierRepository supplierRepository;
    private SupplierOrderRepository supplierOrderRepository;
    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository, SupplierOrderRepository supplierOrderRepository) {
        this.supplierRepository = supplierRepository;
        this.supplierOrderRepository = supplierOrderRepository;
    }


    @Override
    public SupplierDto save(SupplierDto dto) {
        List<String> errors = SupplierValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Supplier is not valid {}", dto);
            throw new InvalidEntityException("Le fournisseur n'est pas valide", ErrorCodes.SUPPLIER_NOT_VALID, errors);
        }

        return SupplierDto.fromEntity(
            supplierRepository.save(
                SupplierDto.toEntity(dto)
            )
        );
    }

    @Override
    public SupplierDto findById(Integer id) {
        if (id == null) {
            log.error("Fournisseur ID is null");
            return null;
        }
        return supplierRepository.findById(id)
            .map(SupplierDto::fromEntity)
            .orElseThrow(() -> new EntityNotFoundException("Aucun fournisseur avec l'ID = " + id + " n' ete trouve dans la BDD", ErrorCodes.SUPPLIER_NOT_FOUND));
    }

    @Override
    public List<SupplierDto> findAll() {
        return supplierRepository.findAll().stream()
            .map(SupplierDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Supplier ID is null");
            return;
        }
        List<ClientOrder> supplierOrder = supplierOrderRepository.findAllBySupplierId(id);
        if (!supplierOrder.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer un fournisseur qui a deja des commandes", ErrorCodes.SUPPLIER_ALREADY_IN_USE);
        }
        supplierRepository.deleteById(id);
    }
}
