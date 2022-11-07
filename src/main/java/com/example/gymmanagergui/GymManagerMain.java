package com.example.gymmanagergui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class GymManagerMain extends Application {
    public void start (Stage stage) throws IOException{
        try{
            BorderPane root = (BorderPane)FXMLLoader.load((getClass().getResource("GymManagerView.fxml")));
            Scene scene = new Scene(root);
            stage.setTitle("Project 3 - Gym Manager");
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch();
    }
}
