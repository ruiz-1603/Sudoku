package org.example.sudoku;

import java.awt.*;

public class Resolvedor {
    private TableroSudoku tablero;
    private Validador validador;

    public Resolvedor(TableroSudoku miTablero) {
        tablero = miTablero;
        validador = new Validador(tablero);
    }

    public boolean resolver() {
        Point primeraCeldaVacia = tablero.siguienteCeldaVacia(new Point(0, 0));
        if (primeraCeldaVacia == null) {
            return tablero.estaCompleto();
        }
        return resolverConBackTracking(primeraCeldaVacia);
    }

    private boolean resolverConBackTracking(Point actual) {
        if (actual == null) {
            return tablero.estaCompleto();
        }

        for (int numero = 1; numero <= 9; numero++) {
            if (validador.esValido(actual, numero)) {
                tablero.colocar(actual, numero);

                Point siguiente = tablero.siguienteCeldaVacia(actual);

                if (resolverConBackTracking(siguiente)) {
                    return true;
                }

                tablero.limpiar(actual);
            }
        }

        return false;
    }
}