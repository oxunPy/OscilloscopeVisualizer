package com.example.program.util.widget.hotkey;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

/**
 * Created by User on 3/6/2018.
 */
public abstract class HotKeyControlListener implements HotKeyListener {

    private KeyCombination kc;

    public HotKeyControlListener(KeyCode code) {
        kc = new KeyCodeCombination(code, KeyCombination.CONTROL_DOWN);
    }

    @Override
    public void onKeyPressed(KeyEvent event) {

        if (kc.match(event) && !event.isConsumed()) {
            event.consume();
            onAction();
        }
    }


    protected abstract void onAction();

}
