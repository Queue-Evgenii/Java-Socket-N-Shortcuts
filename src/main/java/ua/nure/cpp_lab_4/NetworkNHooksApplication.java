package ua.nure.cpp_lab_4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NetworkNHooksApplication extends Application {
    private IShortcutHandler shortcutHandler;

    public void setShortcutHandler(IShortcutHandler handler) {
        this.shortcutHandler = handler;
    }
    NetworkNHooksController controller;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(NetworkNHooksApplication.class.getResource("network-n-hooks.fxml"));
        controller = new NetworkNHooksController();
        fxmlLoader.setController(controller);
        setShortcutHandler(controller);

        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        scene.setOnKeyPressed(key -> {
            if (shortcutHandler != null && key.isControlDown()) {
                switch (key.getCode()) {
                    case Q:
                        shortcutHandler.onCtrlQ();
                        return;
                    case W:
                        shortcutHandler.onCtrlW();
                        return;
                    case E:
                        shortcutHandler.onCtrlE();
                        return;
                    case R:
                        shortcutHandler.onCtrlR();
                        return;
                    case D:
                        shortcutHandler.onCtrlD();
                        return;
                    default:
                        break;
                }
            }

        });
        stage.setTitle("Network Interconnection");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}