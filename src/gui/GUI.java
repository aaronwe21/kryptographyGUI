package gui;

import commands.CommandHandler;
import configuration.Configuration;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import network.Channel;
import network.Participant;
import persistence.DataStore;
import persistence.Log;
import persistence.HSQLDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GUI extends Application {
    public void start(Stage primaryStage) {
        primaryStage.setTitle("MSA | Mosbach Security Agency");

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(15, 12, 15, 12));
        hBox.setSpacing(10);
        hBox.setStyle("-fx-background-color: #336699;");

        Button executeButton = new Button("Execute");
        executeButton.setPrefSize(100, 20);

        Button closeButton = new Button("Close");
        closeButton.setPrefSize(100, 20);



        TextArea commandLineArea = new TextArea();
        commandLineArea.setWrapText(true);

        TextArea outputArea = new TextArea();
        outputArea.setWrapText(true);
        outputArea.setEditable(false);

        hBox.getChildren().addAll(executeButton, closeButton);

        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(25, 25, 25, 25));
        vbox.getChildren().addAll(hBox, commandLineArea, outputArea);

        Scene scene = new Scene(vbox, 950, 500);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.F3){
                    Configuration.instance.changeDebugMode();
                    if (Configuration.instance.getDebugModeActive()){
                        System.out.println("Debug mode activated!");
                        vbox.setStyle("-fx-background-color: #990000;");
                    }
                    else {
                        System.out.println("Debug mode deactivated!");
                        vbox.setStyle("-fx-background-color: #F4F4F4;");
                    }
                }
                if (keyEvent.getCode() == KeyCode.F5){
                    System.out.println("--- execute ---");
                    outputArea.setText(CommandHandler.execute(commandLineArea.getText()));
                }
                if (keyEvent.getCode() == KeyCode.F8){
                    outputArea.setText(Log.getNewestLogText());
                }
            }
        });

        executeButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event)
            {
                System.out.println("--- execute ---");
                outputArea.setText(CommandHandler.execute(commandLineArea.getText()));
            }
        });

        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                HSQLDB.instance.shutdown();
                System.exit(0);
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();


        HSQLDB.instance.setupConnection();

        //HSQLDB.instance.resetDatabase();


        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                HSQLDB.instance.shutdown();
            }
        });

    }
}