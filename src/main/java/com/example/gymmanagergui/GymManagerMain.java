package com.example.gymmanagergui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GymManagerMain extends Application {
    public void start (Stage stage) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(GymManagerMain.class.getResource("GymMangagerViewer"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        stage.setTitle("Project 3 - Gym Manager");
        stage.setScene(scene);
        stage.show();
    }
}
