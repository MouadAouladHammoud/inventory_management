package com.mouad.stockmanagement.repository;

import com.mouad.stockmanagement.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

}
