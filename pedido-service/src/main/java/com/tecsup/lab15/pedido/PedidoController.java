package com.tecsup.lab15.pedido;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    private final AtomicLong sequence = new AtomicLong(3);
    private final List<Pedido> pedidos = new ArrayList<>(List.of(
            new Pedido(1L, 1L, 1L, "Laptop Lenovo", 2, new BigDecimal("5200.00")),
            new Pedido(2L, 2L, 2L, "Silla ergonomica", 1, new BigDecimal("480.00"))
    ));

    @GetMapping
    public List<Pedido> listar() {
        return pedidos;
    }

    @GetMapping("/{id}")
    public Pedido obtener(@PathVariable Long id) {
        return pedidos.stream()
                .filter(pedido -> pedido.id().equals(id))
                .findFirst()
                .orElseThrow();
    }

    @PostMapping
    public ResponseEntity<Pedido> crear(@RequestBody Pedido datos) {
        Pedido creado = new Pedido(sequence.getAndIncrement(), datos.clienteId(), datos.categoriaId(),
                datos.producto(), datos.cantidad(), datos.total());
        pedidos.add(creado);
        return ResponseEntity.status(201).body(creado);
    }

    @PutMapping("/{id}")
    public Pedido actualizar(@PathVariable Long id, @RequestBody Pedido datos) {
        pedidos.removeIf(pedido -> pedido.id().equals(id));
        Pedido actualizado = new Pedido(id, datos.clienteId(), datos.categoriaId(),
                datos.producto(), datos.cantidad(), datos.total());
        pedidos.add(actualizado);
        return actualizado;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pedidos.removeIf(pedido -> pedido.id().equals(id));
        return ResponseEntity.noContent().build();
    }
}
