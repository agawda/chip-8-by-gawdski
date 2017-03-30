package com.gawdski.chip8bygawdski;

import javafx.application.Application;
import javafx.stage.Stage;

public class Emulator extends Application {
    private Screen screen;
    @Override
    public void start(Stage primaryStage) throws Exception {
        Keyboard keyboard = new Keyboard();
        screen = new Screen(keyboard);
        screen.drawOnePixel(0, 0);
        screen.drawOnePixel(2, 2);
        screen.clearDisplay();
    }
}
