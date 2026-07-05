package com.tecsup.lab15.cliente;

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
@RequestMapping("/api/clientes")
public class ClienteController {
    private final AtomicLong sequence = new AtomicLong(3);
    private final List<Cliente> clientes = new ArrayList<>(List.of(
            new Cliente(1L, "Ana Torres", "ana.torres@demo.com", "987111222"),
            new Cliente(2L, "Luis Ramirez", "luis.ramirez@demo.com", "987333444")
    ));

    @GetMapping
    public List<Cliente> listar() {
        return clientes;
    }

    @GetMapping("/{id}")
    public Cliente obtener(@PathVariable Long id) {
        return clientes.stream()
                .filter(cliente -> cliente.id().equals(id))
                .findFirst()
                .orElseThrow();
    }

    @PostMapping
    public ResponseEntity<Cliente> crear(@RequestBody Cliente datos) {
        Cliente creado = new Cliente(sequence.getAndIncrement(), datos.nombre(), datos.correo(), datos.telefono());
        clientes.add(creado);
        return ResponseEntity.status(201).body(creado);
    }

    @PutMapping("/{id}")
    public Cliente actualizar(@PathVariable Long id, @RequestBody Cliente datos) {
        eliminarInterno(id);
        Cliente actualizado = new Cliente(id, datos.nombre(), datos.correo(), datos.telefono());
        clientes.add(actualizado);
        return actualizado;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        eliminarInterno(id);
        return ResponseEntity.noContent().build();
    }

    private void eliminarInterno(Long id) {
        clientes.removeIf(cliente -> cliente.id().equals(id));
    }
}
