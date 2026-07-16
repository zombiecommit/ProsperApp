package com.prosperapp.util;

import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class Toast {

    public static void mostrar(StackPane contenedorRaiz, String mensaje) {

        Label label = new Label("✓ " + mensaje);
        label.setStyle(
                "-fx-background-color: #2E7D32; -fx-text-fill: white; " +
                        "-fx-padding: 10 20; -fx-background-radius: 8px; -fx-font-size: 13px;"
        );

        StackPane.setAlignment(label, Pos.BOTTOM_CENTER);
        StackPane.setMargin(label, new javafx.geometry.Insets(0, 0, 30, 0));

        contenedorRaiz.getChildren().add(label);

        PauseTransition pausa = new PauseTransition(Duration.seconds(2));
        pausa.setOnFinished(e -> contenedorRaiz.getChildren().remove(label));
        pausa.play();
    }
}