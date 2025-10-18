package org.example.sudoku;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class SudokuController {
    @FXML
    private GridPane gridSudoku;

    @FXML
    private Button btnResolver;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnEjemplo;

    private TextField[][] celdas;
    private TableroSudoku tablero;
    private Validador validador;
    private Resolvedor resolvedor;

    @FXML
    public void initialize() {
        tablero = new TableroSudoku();
        validador = new Validador(tablero);
        resolvedor = new Resolvedor(tablero);
        celdas = new TextField[9][9];

        crearTablero();
    }

    private void crearTablero() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                TextField celda = new TextField();
                celda.setPrefWidth(50);
                celda.setPrefHeight(50);
                celda.setAlignment(Pos.CENTER);
                celda.setStyle(obtenerEstiloCelda(i, j));

                final int fila = i;
                final int col = j;
                celda.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("[1-9]?")) {
                        celda.setText(oldValue);
                    } else if (newValue.length() > 1) {
                        celda.setText(newValue.substring(0, 1));
                    }
                });

                celdas[i][j] = celda;

                StackPane contenedor = new StackPane(celda);
                gridSudoku.add(contenedor, j, i);
            }
        }
    }

    private String obtenerEstiloCelda(int fila, int col) {
        String estilo = "-fx-font-size: 18px; -fx-font-weight: bold;";

        String borderTop = ((fila % 3) == 0) ? "2px" : "0.5px";
        String borderLeft = ((col % 3) == 0) ? "2px" : "0.5px";
        String borderBottom = (fila == 8) ? "2px" : "0.5px";
        String borderRight = (col == 8) ? "2px" : "0.5px";

        estilo += String.format(
                "-fx-border-color: black; " +
                        "-fx-border-width: %s %s %s %s;",
                borderTop, borderRight, borderBottom, borderLeft
        );

        return estilo;
    }

    @FXML
    private void resolverSudoku() {

        if (!capturarTablero()) {
            return;
        }

        if (!validador.validarTableroInicial()) {
            mostrarAlerta("Error", "El tablero inicial no es válido. Hay números repetidos en filas, columnas o cuadros.", Alert.AlertType.ERROR);
            return;
        }


        int[][] tableroOriginal = copiarTablero();

        if (resolvedor.resolver()) {
            mostrarSolucion();
            mostrarAlerta("Éxito", "¡Sudoku resuelto correctamente!", Alert.AlertType.INFORMATION);
        } else {
            tablero.cargarTablero(tableroOriginal);
            restaurarVistaTablero(tableroOriginal);
            mostrarAlerta("Sin solución", "No existe solución para este Sudoku.\n\nEl tablero ha sido restaurado a su estado inicial.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void limpiarTablero() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                celdas[i][j].clear();
                celdas[i][j].setEditable(true);
                celdas[i][j].setStyle(obtenerEstiloCelda(i, j));
            }
        }
        tablero.limpiarTablero();
    }

    @FXML
    private void cargarEjemplo() {
        limpiarTablero();

        int[][] ejemplo = {
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (ejemplo[i][j] != 0) {
                    celdas[i][j].setText(String.valueOf(ejemplo[i][j]));
                }
            }
        }
    }

    private boolean capturarTablero() {
        tablero.limpiarTablero();
        boolean hayNumeros = false;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String texto = celdas[i][j].getText().trim();

                if (!texto.isEmpty()) {
                    hayNumeros = true;
                    try {
                        int valor = Integer.parseInt(texto);
                        if (valor < 1 || valor > 9) {
                            mostrarAlerta("Error",
                                    String.format("Valor inválido en fila %d, columna %d. Debe ser entre 1 y 9.", i + 1, j + 1),
                                    Alert.AlertType.ERROR);
                            return false;
                        }
                        tablero.ponerValor(i, j, valor);
                        tablero.marcarComoInicial(i, j, true);
                    } catch (NumberFormatException e) {
                        mostrarAlerta("Error",
                                String.format("Valor no numérico en fila %d, columna %d.", i + 1, j + 1),
                                Alert.AlertType.ERROR);
                        return false;
                    }
                }
            }
        }

        if (!hayNumeros) {
            mostrarAlerta("Error",
                    "Debes ingresar al menos un número inicial para resolver el Sudoku.",
                    Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private void mostrarSolucion() {
        int[][] solucion = tablero.getTablero();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                celdas[i][j].setText(String.valueOf(solucion[i][j]));

                if (tablero.esInicial(i, j)) {
                    celdas[i][j].setStyle(obtenerEstiloCelda(i, j) +
                            "-fx-background-color: #E8F5E9; -fx-text-fill: #1B5E20;");
                    celdas[i][j].setEditable(false);
                } else {
                    celdas[i][j].setStyle(obtenerEstiloCelda(i, j) +
                            "-fx-background-color: #BBDEFB; -fx-text-fill: #0D47A1;");
                    celdas[i][j].setEditable(false);
                }
            }
        }
    }

    private void restaurarVistaTablero(int[][] tableroOriginal) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (tableroOriginal[i][j] != 0) {
                    celdas[i][j].setText(String.valueOf(tableroOriginal[i][j]));
                } else {
                    celdas[i][j].clear();
                }
                celdas[i][j].setStyle(obtenerEstiloCelda(i, j));
                celdas[i][j].setEditable(true);
            }
        }
    }

    private int[][] copiarTablero() {
        int[][] copia = new int[9][9];
        int[][] original = tablero.getTablero();

        for (int i = 0; i < 9; i++) {
            System.arraycopy(original[i], 0, copia[i], 0, 9);
        }

        return copia;
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}