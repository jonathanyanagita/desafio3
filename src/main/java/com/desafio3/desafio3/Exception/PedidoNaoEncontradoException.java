package com.desafio3.desafio3.Exception;

public class PedidoNaoEncontradoException extends RuntimeException {

    public PedidoNaoEncontradoException() {
        super("Pedido não encontrado! ");
    }

    public PedidoNaoEncontradoException(String message) {
        super(message);
    }
}
