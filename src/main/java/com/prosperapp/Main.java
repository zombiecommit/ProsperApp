package com.prosperapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Label titulo = new Label("Bienvenido a ProsperApp");

        StackPane root = new StackPane(titulo);

        Scene scene = new Scene(root, 800, 500);

        stage.setTitle("ProsperApp");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}