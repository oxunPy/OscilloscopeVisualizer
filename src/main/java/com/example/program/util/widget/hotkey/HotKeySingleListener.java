package com.example.program.util.widget.hotkey;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Created by User on 3/6/2018.
 */
public abstract class HotKeySingleListener implements HotKeyListener {

    private KeyCode kc[];

    public HotKeySingleListener(KeyCode... codes) {
        kc = codes;
    }

    @Override
    public void onKeyPressed(KeyEvent event) {
        if (event.isAltDown() || event.isControlDown() || event.isMetaDown() || event.isShiftDown() || event.isShortcutDown())
            return;
        if (kc != null) {
            for (KeyCode code : kc) {
                if (code == event.getCode()) {
                    onAction();
                    return;
                }
            }

        }
    }


    protected abstract void onAction();

}