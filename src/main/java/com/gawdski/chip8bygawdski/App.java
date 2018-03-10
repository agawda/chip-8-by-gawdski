package com.gawdski.chip8bygawdski;

import com.gawdski.chip8bygawdski.graphics.LoadScreenController;
import javafx.application.Application;

class App {
    public static void main(String[] args) {
        new Thread(() -> Application.launch(LoadScreenController.class)).start();
    }
}