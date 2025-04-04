package com.example.IAM.database;

import com.example.IAM.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAll ();

    Optional<Product> findById(Long id);

    void deleteById(Long id);

}

