package com.tecsup.lab15.categoria;

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
@RequestMapping("/api/categorias")
public class CategoriaController {
    private final AtomicLong sequence = new AtomicLong(3);
    private final List<Categoria> categorias = new ArrayList<>(List.of(
            new Categoria(1L, "Tecnologia", "Productos tecnologicos"),
            new Categoria(2L, "Oficina", "Articulos de oficina")
    ));

    @GetMapping
    public List<Categoria> listar() {
        return categorias;
    }

    @GetMapping("/{id}")
    public Categoria obtener(@PathVariable Long id) {
        return categorias.stream()
                .filter(categoria -> categoria.id().equals(id))
                .findFirst()
                .orElseThrow();
    }

    @PostMapping
    public ResponseEntity<Categoria> crear(@RequestBody Categoria datos) {
        Categoria creada = new Categoria(sequence.getAndIncrement(), datos.nombre(), datos.descripcion());
        categorias.add(creada);
        return ResponseEntity.status(201).body(creada);
    }

    @PutMapping("/{id}")
    public Categoria actualizar(@PathVariable Long id, @RequestBody Categoria datos) {
        categorias.removeIf(categoria -> categoria.id().equals(id));
        Categoria actualizada = new Categoria(id, datos.nombre(), datos.descripcion());
        categorias.add(actualizada);
        return actualizada;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        categorias.removeIf(categoria -> categoria.id().equals(id));
        return ResponseEntity.noContent().build();
    }
}
