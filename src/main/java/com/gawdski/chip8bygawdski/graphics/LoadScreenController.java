package com.gawdski.chip8bygawdski.graphics;

import com.gawdski.chip8bygawdski.exception.GameNotLoadedException;
import com.gawdski.chip8bygawdski.io.GameLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;

/**
 * @author Anna Gawda
 * 10.03.2018
 */
public class LoadScreenController extends Application {
    private static final Logger LOG = LoggerFactory.getLogger(LoadScreenController.class);

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/mainScreen.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void handleLoadGameAction() {
        try {
            File file = new File(getClass().getResource("/games/BLINKY").toURI());
            GameLoader.loadGame(file);
        } catch (GameNotLoadedException | URISyntaxException e) {
            LOG.error("Exception during game loading, exiting...");
            return;
        }
        LOG.info("The game was loaded...");
    }
}
