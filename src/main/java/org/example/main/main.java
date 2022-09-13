package org.example.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class main extends Application {
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(main.class.getResource("ui.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setTitle("Apache solr RCE(CVE-2019-0193)一键写webshell工具 by jdr 2022-09-13");
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        launch(args);
    }

}
