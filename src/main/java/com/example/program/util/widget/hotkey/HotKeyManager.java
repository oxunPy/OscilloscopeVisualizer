package com.example.program.util.widget.hotkey;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 3/6/2018.
 */
public class HotKeyManager {
    private List<HotKeyListener> mListeners = new ArrayList<>();
    private EventHandler<KeyEvent> handler = event -> {

//        if (event.getTarget() instanceof ComboBoxPopupControl.FakeFocusTextField)
//            return;

//        System.out.println(event);

        if (mListeners != null && !mListeners.isEmpty()) {
            for (HotKeyListener listener : mListeners)
                Platform.runLater(() -> listener.onKeyPressed(event));
        }
    };
    private static HotKeyManager instance;

    public void addListener(HotKeyListener listener) {
        if (mListeners == null)
            mListeners = new ArrayList<>();
        if (!mListeners.contains(listener))
            mListeners.add(listener);
    }

    public void removeListener(HotKeyListener listener) {
        if (mListeners.contains(listener))
            mListeners.remove(listener);
    }

    public static HotKeyManager getInstance() {
        if (instance == null)
            instance = new HotKeyManager();
        return instance;
    }

    public void registerHandler(Scene scene) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, handler);
    }


}
