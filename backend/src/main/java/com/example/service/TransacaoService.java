package com.example.service;

import com.example.enums.TipoTransacao;
import com.example.models.Transacao;
import com.example.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransacaoService {
    private final TransacaoRepository repository;

    public Transacao salvar(Transacao transacao) {
        return repository.save(transacao);
    }

    public List<Transacao> listarTodas() {
        return repository.findAll();
    }

    public List<Transacao> listarPorMes(int mes, int ano) {
        LocalDate inicio = LocalDate.of(ano, mes, 1);
        LocalDate fim = inicio.plusMonths(1).minusDays(1);
        return repository.findByDataBetween(inicio, fim);
    }

    public Map<String, Object> obterResumo(int mes, int ano) {
        List<Transacao> transacoes = listarPorMes(mes, ano);

        BigDecimal totalReceitas = transacoes.stream()
                .filter(t -> t.getTipo() == TipoTransacao.RECEITA)
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDespesas = transacoes.stream()
                .filter(t -> t.getTipo() == TipoTransacao.DESPESA)
                .map(Transacao::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal saldo = totalReceitas.subtract(totalDespesas);

        return Map.of(
                "totalReceitas", totalReceitas,
                "totalDespesas", totalDespesas,
                "saldo", saldo
        );
    }

    public Map<String, BigDecimal> obterDespesasPorCategoria() {
        List<Object[]> resultado = repository.somarDespesasPorCategoria();
        return resultado.stream()
                .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> (BigDecimal) arr[1]
                ));
    }
}