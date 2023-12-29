package com.example.ClientApp.repository;

import com.example.ClientApp.dto.ClientResponse;
import com.example.ClientApp.entities.ClientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository <ClientEntity,Long> {
    @Query("SELECT p FROM ClientEntity p WHERE (:nome IS NULL OR p.nome LIKE %:nome%) " +
            "AND (:idade IS NULL OR p.idade = :idade) " +
            "AND (:cep IS NULL OR p.cep = :cep)")
    Page<ClientEntity> findByFilters(String nome, Integer idade, String cep, Pageable pageable);
}
