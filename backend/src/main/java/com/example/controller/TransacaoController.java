package com.example.controller;

import com.example.models.Transacao;
import com.example.service.TransacaoService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transacoes")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class TransacaoController {
    private final TransacaoService service;

    @PostMapping
    public ResponseEntity<Transacao> criar(@Valid @RequestBody Transacao transacao) {
        return ResponseEntity.ok(service.salvar(transacao));
    }

    @GetMapping
    public ResponseEntity<List<Transacao>> listar(
            @RequestParam int mes,
            @RequestParam int ano) {
        return ResponseEntity.ok(service.listarPorMes(mes, ano));
    }

    @GetMapping("/resumo")
    public ResponseEntity<Map<String, Object>> resumo(
            @RequestParam int mes,
            @RequestParam int ano) {
        return ResponseEntity.ok(service.obterResumo(mes, ano));
    }

    @GetMapping("/despesas-categoria")
    public ResponseEntity<Map<String, BigDecimal>> despesasPorCategoria() {
        return ResponseEntity.ok(service.obterDespesasPorCategoria());
    }

    @GetMapping("/export")
    public void exportarCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=transacoes.csv");

        List<Transacao> transacoes = service.listarTodas();
        PrintWriter writer = response.getWriter();

        writer.println("Data,Tipo,Categoria,Valor,Descrição");
        transacoes.forEach(t ->
                writer.println(String.format("%s,%s,%s,%.2f,%s",
                        t.getData(), t.getTipo(), t.getCategoria(),
                        t.getValor(), t.getDescricao()))
        );
    }
}