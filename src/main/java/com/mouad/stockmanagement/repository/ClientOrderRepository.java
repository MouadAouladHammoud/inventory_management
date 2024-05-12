package com.mouad.stockmanagement.repository;

import com.mouad.stockmanagement.model.ClientOrder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientOrderRepository extends JpaRepository<ClientOrder, Integer> {
    Optional<ClientOrder> findClientOrderByCode(String code);
    List<ClientOrder> findAllByClientId(Integer id);
}
