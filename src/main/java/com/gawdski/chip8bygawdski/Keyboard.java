package com.gawdski.chip8bygawdski;

import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;

/*
       |2|3|4|5|      |1|2|3|C|
       |W|E|R|T|  ->  |4|5|6|D|
       |S|D|F|G|      |7|8|9|E|
       |Z|X|C|V|      |A|0|B|F|
 */


public class Keyboard {
    private List<KeyCode> pressedKeys;

    public Keyboard() {
        pressedKeys = new ArrayList<>();
    }

    public void handlePressed(KeyCode keyCode) {
        if(keyCode == KeyCode.ESCAPE)
            System.exit(0);

        if(!pressedKeys.contains(keyCode)) {
            pressedKeys.add(keyCode);
        }
    }

    public void handleReleased(KeyCode keyCode) {
        if(pressedKeys.contains(keyCode)) {
            pressedKeys.remove(keyCode);
        }
    }

    public boolean isPressed(KeyCode keyCode) {
        return pressedKeys.contains(keyCode);
    }
}
