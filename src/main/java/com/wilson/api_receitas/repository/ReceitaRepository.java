package com.wilson.api_receitas.repository;

import com.wilson.api_receitas.model.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long> {

    Optional<Receita> findByExternalId(String externalId);
}
