module com.example.gymmanagergui {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.testng;
    requires org.junit.jupiter.api;


    opens com.example.gymmanagergui to javafx.fxml;
    exports com.example.gymmanagergui;
}