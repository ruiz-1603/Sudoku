package org.example.sudoku;

import java.awt.*;

public class TableroSudoku {
    private int[][] tablero;
    private boolean[][] esInicial;
    public static final int TAM = 9;
    public static final int TAM_CUADRO = 3;

    public TableroSudoku() {
        tablero = new int[TAM][TAM];
        esInicial = new boolean[TAM][TAM];
        limpiarTablero();
    }

    public void limpiarTablero() {
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                tablero[i][j] = 0;
                esInicial[i][j] = false;
            }
        }
    }

    public int getValor(int fila, int columna) {
        if (fila >= 0 && fila < TAM && columna >= 0 && columna < TAM)
            return tablero[fila][columna];
        return 0;
    }

    public void ponerValor(int fila, int columna, int valor) {
        if (fila >= 0 && fila < TAM && columna >= 0 && columna < TAM) {
            if (valor >= 0 && valor <= 9) {
                tablero[fila][columna] = valor;
            }
        }
    }

    public void marcarComoInicial(int fila, int columna, boolean inicial) {
        if (fila >= 0 && fila < TAM && columna >= 0 && columna < TAM) {
            esInicial[fila][columna] = inicial;
        }
    }

    public boolean esInicial(int fila, int columna) {
        if (fila >= 0 && fila < TAM && columna >= 0 && columna < TAM) {
            return esInicial[fila][columna];
        }
        return false;
    }

    public boolean estaCompleto() {
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                if (tablero[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public Point siguienteCeldaVacia(Point actual) {
        int fila = actual.x;
        int columna = actual.y;

        for (int i = fila; i < TAM; i++) {
            for (int j = (i == fila ? columna : 0); j < TAM; j++) {
                if (tablero[i][j] == 0) {
                    return new Point(i, j);
                }
            }
        }
        return null;
    }

    public void colocar(Point posicion, int numero) {
        if (posicion != null && numero >= 1 && numero <= 9) {
            tablero[posicion.x][posicion.y] = numero;
        }
    }

    public void limpiar(Point posicion) {
        if (posicion != null && !esInicial[posicion.x][posicion.y]) {
            tablero[posicion.x][posicion.y] = 0;
        }
    }

    public int[][] getTablero() {
        return tablero;
    }

    public void cargarTablero(int[][] nuevoTablero) {
        if (nuevoTablero != null && nuevoTablero.length == TAM) {
            for (int i = 0; i < TAM; i++) {
                if (nuevoTablero[i].length == TAM) {
                    for (int j = 0; j < TAM; j++) {
                        tablero[i][j] = nuevoTablero[i][j];
                        if (nuevoTablero[i][j] != 0) {
                            esInicial[i][j] = true;
                        }
                    }
                }
            }
        }
    }
}