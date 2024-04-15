package ua.nure.cpp_lab_4;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.*;
import java.net.*;

public class Client {
    private int CLIENT_ID = -1;
    private PrintWriter output;
    private BufferedReader input;

    private final Pane wrapperPane;
    public Client(Pane wrapperPane) {
        this.wrapperPane = wrapperPane;
    }
    public void connect(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        output = new PrintWriter(socket.getOutputStream(), true);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        receiveMessages();
    }
    private void receiveMessages() {
        Thread thr = new Thread(() -> {
            try {
                String inputLine, key, value;
                while ((inputLine = input.readLine()) != null) {
                    System.out.println("Received: " + inputLine);
                    key = inputLine.split("=")[0].toLowerCase();
                    value = inputLine.split("=")[1].toLowerCase();
                    action(key, value);
                }
            } catch (IOException e) {
                System.out.println("Server connection lost.");
            }
        });
        thr.setDaemon(true);
        thr.start();
    }
    public void sendMessage(String message) {
        output.println("client=" + CLIENT_ID + "?" + message);
    }

    private void action(String key, String value) {
        switch (key) {
            case "id":
                CLIENT_ID = Integer.parseInt(value);
                break;
            case "figure":
                drawFigure(value);
                break;
            case "shortcut":
                shortcutHandle(value);
                break;
            default:
                break;
        }
    }
    private void drawFigure(String value) {
        switch (value) {
            case "circle":
                drawCircle();
                break;
            case "rectangle":
                drawRectangle();
                break;
            default:
                break;
        }
    }
    private void drawCircle() {
        Circle circle = new Circle(16);
        circle.setFill(Color.BLUE);
        circle.setCenterX(Math.random() * 700 + 60);
        circle.setCenterY(Math.random() * 400 + 60);
        Platform.runLater(() -> wrapperPane.getChildren().add(circle));
    }
    private void drawRectangle() {
        Rectangle rectangle = new Rectangle(36, 24);
        rectangle.setFill(Color.BLUE);
        rectangle.setX(Math.random() * 700 + 60);
        rectangle.setY(Math.random() * 400 + 10);
        Platform.runLater(() -> wrapperPane.getChildren().add(rectangle));
    }
    private void shortcutHandle(String value) {
        switch (value) {
            case "ctrl+q":
                showMessage(Messages.HELLO);
                break;
            case "ctrl+w":
                showMessage(Messages.HOW_ARE_YOU);
                break;
            case "ctrl+e":
                showMessage(Messages.GOOD_BYE);
                break;
            default:
                break;
        }
    }
    private void showMessage(Messages msg) {
        Platform.runLater(() -> wrapperPane.getChildren().clear());
        Label label = new Label(msg.toString());
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: 700");
        GridPane.setConstraints(label, 0, 0);
        Platform.runLater(() -> wrapperPane.getChildren().add(label));
    }
}
