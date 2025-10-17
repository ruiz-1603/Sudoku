package org.example.sudoku;

import java.awt.*;

public class Resolvedor {
    private TableroSudoku tablero;
    private Validador validador;

    public Resolvedor(TableroSudoku miTablero) {
        tablero = miTablero;
        validador = new Validador(tablero);
    }

    public boolean Resolver() {
        Point primeraCeldaVacia = tablero.SiguienteCeldaVacia(new Point(0, 0));
        if (primeraCeldaVacia == null) {
            return tablero.EstaCompleto();
        }
        return ResolverConBackTracking(primeraCeldaVacia);
    }

    private boolean ResolverConBackTracking(Point actual) {
        if (actual == null) {
            return tablero.EstaCompleto();
        }

        for (int numero = 1; numero <= 9; numero++) {
            if (validador.EsValido(actual, numero)) {
                tablero.Colocar(actual, numero);

                Point siguiente = tablero.SiguienteCeldaVacia(actual);

                if (ResolverConBackTracking(siguiente)) {
                    return true;
                }

                tablero.Limpiar(actual);
            }
        }

        return false;
    }


}