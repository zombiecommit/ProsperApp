package com.prosperapp;

import com.prosperapp.view.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        LoginView loginView = new LoginView(stage);

        stage.setTitle("ProsperApp");
        stage.setScene(loginView.construir());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}