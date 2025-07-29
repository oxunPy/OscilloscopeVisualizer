package com.example.program.util.widget.hotkey;

import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

public abstract class HotKeyCombinationListener implements HotKeyListener {

    private KeyCombination kc;

    public HotKeyCombinationListener(KeyCodeCombination codeCombination) {
        kc = codeCombination;
    }

    @Override
    public void onKeyPressed(KeyEvent event) {
        if (kc.match(event)) {
            onAction();
        }
    }


    public abstract void onAction();

}