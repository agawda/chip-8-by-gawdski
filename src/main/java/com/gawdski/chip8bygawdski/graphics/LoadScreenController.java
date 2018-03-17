package com.gawdski.chip8bygawdski.graphics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.gawdski.chip8bygawdski.Keyboard;
import com.gawdski.chip8bygawdski.config.ChipConfig;
import com.gawdski.chip8bygawdski.exception.GameNotLoadedException;
import com.gawdski.chip8bygawdski.io.GameLoader;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Anna Gawda
 * 10.03.2018
 */
public class LoadScreenController extends Application {
    private static final Logger LOG = LoggerFactory.getLogger(LoadScreenController.class);

    private ChipConfig chipConfig;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/loadScreen.fxml"));

        Scene scene = new Scene(root, 200, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    public void handleLoadGameAction() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        chipConfig = objectMapper.readValue(new File(getClass().getResource("/config/config.yaml").getFile()), ChipConfig.class);

        try {
            File file = new File(getClass().getResource("/games/BLINKY").toURI());
            GameLoader.loadGame(file);
        } catch (GameNotLoadedException | URISyntaxException e) {
            LOG.error("Exception during game loading, exiting...");
            return;
        }
        LOG.info("The game was loaded...");

        prepareGameScreen();
    }

    private void prepareGameScreen() {
        Keyboard keyboard = new Keyboard();
        Screen screen = new Screen(keyboard, chipConfig.getScreenScale());
        screen.run();
    }
}
