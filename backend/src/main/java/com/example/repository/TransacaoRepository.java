package com.example.repository;

import com.example.models.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    List<Transacao> findByDataBetween(LocalDate inicio, LocalDate fim);

    @Query("SELECT t.categoria, SUM(t.valor) FROM Transacao t WHERE t.tipo = 'DESPESA' GROUP BY t.categoria")
    List<Object[]> somarDespesasPorCategoria();
}
