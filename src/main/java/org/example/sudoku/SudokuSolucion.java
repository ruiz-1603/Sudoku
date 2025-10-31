package org.example.sudoku;

import java.awt.*;

public class SudokuSolucion {
    private TableroSudoku tablero;
    private Validador validador;

    public SudokuSolucion(TableroSudoku miTablero) {
        tablero = miTablero;
        validador = new Validador(tablero);
    }

    public boolean resolver() {
        Point primeraCeldaVacia = tablero.siguienteCeldaVacia(new Point(0, 0));
        return resolver(primeraCeldaVacia);
    }

    private boolean resolver(Point actual) {
        //  si no hay + celdas vacías, verificar solución
        if (actual == null) {
            return tablero.esSolucion();
        }

        boolean x1 = resolver(actual, 1);
        boolean x2 = resolver(actual, 2);
        boolean x3 = resolver(actual, 3);
        boolean x4 = resolver(actual, 4);
        boolean x5 = resolver(actual, 5);
        boolean x6 = resolver(actual, 6);
        boolean x7 = resolver(actual, 7);
        boolean x8 = resolver(actual, 8);
        boolean x9 = resolver(actual, 9);

        return x1 || x2 || x3 || x4 || x5 || x6 || x7 || x8 || x9;
    }

    private boolean resolver(Point actual, int numero) {
        if (validador.esValido(actual, numero)) {
            tablero.colocar(actual, numero);

            Point siguiente = tablero.siguienteCeldaVacia(actual);

            if (resolver(siguiente)) {
                return true;
            } else {
                tablero.limpiar(actual);
            }
        }
        return false;
    }
}