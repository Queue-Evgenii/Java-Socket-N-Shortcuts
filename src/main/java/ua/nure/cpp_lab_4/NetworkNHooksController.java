package ua.nure.cpp_lab_4;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NetworkNHooksController implements ShortcutHandler {
    @FXML
    public Button openPortButton = new Button();
    @FXML
    public Button connectToPortButton = new Button();
    @FXML
    public Button sendMessageButton = new Button();
    @FXML
    public Button clearButton = new Button();
    @FXML
    public Pane wrapperPane = new Pane();
    Client client = null;
    RelayServer server = null;
    List<Messages> sentMessages;

    @FXML
    public void initialize() {
        openPortButton.setOnAction(e -> openPort());
        connectToPortButton.setOnAction(e -> connectToPort());
        sendMessageButton.setOnAction(e -> sendMessage());
        clearButton.setOnAction(e -> clear());
        sentMessages = new ArrayList<>();
    }
    @Override
    public void onCtrlQ() {
        if (client == null) return;
        client.sendMessage("shortcut=ctrl+q");
        sentMessages.add(Messages.HELLO);
    }
    @Override
    public void onCtrlW() {
        if (client == null) return;
        client.sendMessage("shortcut=ctrl+w");
        sentMessages.add(Messages.HOW_ARE_YOU);
    }
    @Override
    public void onCtrlE() {
        if (client == null) return;
        client.sendMessage("shortcut=ctrl+e");
        sentMessages.add(Messages.GOOD_BYE);
    }
    @Override
    public void onCtrlR() {
        wrapperPane.getChildren().clear();
        Label label = new Label(sentMessages.size() > 0 ? sentMessages.toString() : "Registry is empty");
        label.setMaxWidth(780);
        label.setWrapText(true);
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: 700");
        GridPane.setConstraints(label, 0, 0);
        wrapperPane.getChildren().add(label);
    }
    @Override
    public void onCtrlD() {
        sentMessages = new ArrayList<>();
        clear();
    }
    private void openPort() {
        if (this.server != null) return;
        new Thread(() -> {
            try {
                server = new RelayServer();
                server.startServer();
            } catch (IOException e) {
                System.out.println("Error server creating: " + e.getMessage());
            }
        }).start();
    }
    private void connectToPort() {
        if (client != null) return;
        new Thread(() -> {
            try {
                client = new Client(wrapperPane);
                client.connect("localhost", 8800);
            } catch (IOException e) {
                client = null;
                System.out.println("Error connecting to the server: " + e.getMessage());
            }
        }).start();
    }
    private void sendMessage() {
        if (client == null) return;
        if (server != null) {
            client.sendMessage("figure=rectangle");
            return;
        }
        client.sendMessage("figure=circle");
    }
    private void clear() {
        wrapperPane.getChildren().clear();
    }


}