package org.example.sudoku;

import java.awt.*;

public class Validador {
    private TableroSudoku tablero;

    public Validador(TableroSudoku miTablero) {
        tablero = miTablero;
    }

    public boolean esValido(Point posicion, int numero) {
        if (posicion == null) return false;
        return esMovimientoValido(posicion.x, posicion.y, numero);
    }

    public boolean esMovimientoValido(int fila, int columna, int numero) {
        boolean validoEnFila = revisarFila(fila, numero);
        boolean validoEnColumna = revisarColumna(columna, numero);
        boolean validoEnCuadro = revisarCuadro(fila, columna, numero);

        return validoEnFila && validoEnColumna && validoEnCuadro;
    }

    private boolean revisarFila(int fila, int numero) {
        for (int col = 0; col < TableroSudoku.TAM; col++) {
            if (tablero.getValor(fila, col) == numero)
                return false;
        }
        return true;
    }

    private boolean revisarColumna(int columna, int numero) {
        for (int fil = 0; fil < TableroSudoku.TAM; fil++) {
            if (tablero.getValor(fil, columna) == numero)
                return false;
        }
        return true;
    }

    private boolean revisarCuadro(int fila, int columna, int numero) {
        int filaInicio = (fila / TableroSudoku.TAM_CUADRO) * TableroSudoku.TAM_CUADRO;
        int colInicio = (columna / TableroSudoku.TAM_CUADRO) * TableroSudoku.TAM_CUADRO;

        for (int i = filaInicio; i < filaInicio + TableroSudoku.TAM_CUADRO; i++) {
            for (int j = colInicio; j < colInicio + TableroSudoku.TAM_CUADRO; j++) {
                if (tablero.getValor(i, j) == numero)
                    return false;
            }
        }
        return true;
    }

    public boolean validarTableroInicial() {
        for (int fila = 0; fila < TableroSudoku.TAM; fila++) {
            for (int col = 0; col < TableroSudoku.TAM; col++) {
                int valor = tablero.getValor(fila, col);
                if (valor != 0) {
                    tablero.ponerValor(fila, col, 0);
                    boolean esValido = esMovimientoValido(fila, col, valor);
                    tablero.ponerValor(fila, col, valor);

                    if (!esValido)
                        return false;
                }
            }
        }
        return true;
    }
}