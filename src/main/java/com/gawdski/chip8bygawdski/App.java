package com.gawdski.chip8bygawdski;

import com.gawdski.chip8bygawdski.graphics.LoadScreenController;
import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        LOG.info("\n ██████╗██╗  ██╗██╗██████╗        █████╗ \n" +
                "██╔════╝██║  ██║██║██╔══██╗      ██╔══██╗\n" +
                "██║     ███████║██║██████╔╝█████╗╚█████╔╝\n" +
                "██║     ██╔══██║██║██╔═══╝ ╚════╝██╔══██╗\n" +
                "╚██████╗██║  ██║██║██║           ╚█████╔╝\n" +
                " ╚═════╝╚═╝  ╚═╝╚═╝╚═╝            ╚════╝ \n" +
                "                                         ");
        new Thread(() -> Application.launch(LoadScreenController.class)).start();
    }
}