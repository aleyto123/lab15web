package com.tecsup.lab15.pedido;

import java.math.BigDecimal;

public record Pedido(Long id, Long clienteId, Long categoriaId, String producto, Integer cantidad, BigDecimal total) {
}
