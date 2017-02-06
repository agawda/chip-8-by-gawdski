package com.gawdski.chip8bygawdski;


import javafx.scene.paint.Color;

public class Screen {
    public static final int SC_WIDTH = 64;
    public static final int SC_HEIGHT = 32;

    private Color foregroundColor;
    private Color backgroundColor;

    public Screen() {
        this.foregroundColor = Color.WHITE;
        this.backgroundColor = Color.BLACK;
    }

    public void drawOnePixel(int x, int y) {

    }

}
