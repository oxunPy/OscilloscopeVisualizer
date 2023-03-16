package com.example.program.common.screen;

import com.example.program.app.controller.AppController;
import com.example.program.util.Note;
import com.example.program.util.Resize;
import com.example.program.util.StringConfig;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;
import java.util.Optional;
import java.util.Stack;

public class NavigationScreen {

    public static final int SINGLE_TOP_TASK = 13;

    public static final int NEW_TASK = 15;

    private AnchorPane content;

    private Screen welcomeScreen;

    private int task = NEW_TASK;

    private static NavigationScreen instance;

    public static NavigationScreen getInstance(){
        if(instance == null){
            instance = new NavigationScreen();
        }
        return instance;
    }

    private Stack<Dansho> screens = new Stack<>();

    public void startScreen(Dansho newDansho) {

        if (!screens.isEmpty()) {
            Dansho oldDansho = screens.peek();
            oldDansho.currentScreen.context = null;
        }
        Screen screen = null;
        Dansho topDansho = null;
        Optional<Dansho> danshoOptional = screens.stream().filter(dansho -> dansho.equals(newDansho)).findFirst();
        if (danshoOptional.isPresent()) {
            topDansho = danshoOptional.get();
        }

        if (topDansho != null) {
            if (task == SINGLE_TOP_TASK) {
                screen = topDansho.currentScreen;
                screens.remove(topDansho);
            }
        }

        screens.push(newDansho);

        if (screen != null) {
            screen.context = AppController.getInstance();
            newDansho.setCurrentScreen(screen);
            newDansho.currentStart();
            newDansho.currentResume();
        } else {
            Class<? extends Screen> clazz = newDansho.clazz;

            try {
                screen = clazz.newInstance();
                screen.context = AppController.getInstance();
                newDansho.setCurrentScreen(screen);
                newDansho.currentCreate();
                newDansho.currentStart();
                newDansho.currentResume();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        changeContent(screen);
    }

    private void changeContent(AnchorPane child) {
        if (child == null)
            return;
        content.getChildren().clear();
        content.getChildren().add(child);
        Resize.margin(child, 0);
    }

    public void finish() {

        if (!screens.empty()) {
            Dansho currentDansho = screens.pop();
            currentDansho.currentScreen.context = null;
            if (currentDansho.isForResult()) {
                currentDansho.previousSetResult();
            }
        }

        if (!screens.isEmpty()) {
            Dansho previousDansho = screens.peek();
            previousDansho.currentResume();
            previousDansho.currentScreen.context = AppController.getInstance();
            changeContent(previousDansho.currentScreen);
        } else {
            if (welcomeScreen != null)
                changeContent(welcomeScreen);
            else
                AppController.getInstance().exit();
        }

    }

    public void removeScreen(String clazzName){
        Optional<Dansho> danshoOptional = screens.stream().filter(dansho -> dansho.clazz.getName().equals(clazzName)).findFirst();
        danshoOptional.ifPresent(dansho -> screens.remove(dansho));
    }

    public void finish(int resultCode, Dansho dansho) {
        if (dansho != null) dansho.resultCode = resultCode;

    }

    public static class Builder{
        private AnchorPane content;

        private int task;

        private Screen screen;

        public Builder content(AnchorPane content){
            this.content = content;
            return this;
        }

        public Builder task(int task){
            this.task = task;
            return this;
        }

        public Builder welcomeScreen(Screen screen){
            this.screen = screen;
            return this;
        }

        public NavigationScreen build(){
            NavigationScreen nScreen = NavigationScreen.getInstance();
            nScreen.content = content;
            nScreen.task = task;
            nScreen.welcomeScreen = screen;
            return nScreen;
        }
    }




    public abstract static class Screen extends AnchorPane{
        private AppController context;

        public static final int RESULT_OK = 0;

        public static final int RESULT_CANCEL = -1;

        private NavigationScreen.Dansho dansho;

        public abstract void onCreate();

        public void onStart(){}

        public void onResume(){}

        public void onDispose(){}

        public void finish(int resultCode) {
            if (dansho != null) dansho.resultCode = resultCode;
            context.getNavigateScreen().finish();
        }

        public void onScreenResult(int requestCode, int resultCode, Bundle data){

        }

        public void startScreenForResult(NavigationScreen.Dansho dansho, int requestCode){
            if(dansho != null){
                dansho.requestCode = requestCode;
                startScreen(dansho);
            }
        }

        public void startScreen(NavigationScreen.Dansho dansho){
            if(context != null){
                context.getNavigateScreen().startScreen(dansho);
            }
            else{
                Note.error(StringConfig.getValue("err.ui.load"));
            }
        }
    }


    public static class Dansho {
        private Class<? extends Screen> clazz;
        private Screen currentScreen;
        private Bundle bundle = new Bundle();
        private Bundle result = new Bundle();
        private int requestCode = 0; /* 1 - 16 */
        private int resultCode = Screen.RESULT_CANCEL;
        private Screen previousScreen;


        public Dansho(Screen previousScreen, Class<? extends Screen> clazz) {
            this.clazz = clazz;
            this.previousScreen = previousScreen;
        }

        public Dansho(Class<? extends Screen> clazz, Bundle bundle) {
            this.clazz = clazz;
            if (bundle != null)
                this.bundle = bundle;
        }

        public Dansho(Class<? extends Screen> clazz) {
            this.clazz = clazz;
        }

        public Dansho(Screen previousScreen, Class<? extends Screen> clazz, Bundle bundle) {
            this.clazz = clazz;
            this.previousScreen = previousScreen;
            if (bundle != null)
                this.bundle = bundle;
        }

        private boolean equals(Dansho dansho) {
            return Objects.equals(dansho.clazz.getName(), clazz.getName());
        }

        private void setResult(Bundle bundle) {
            result.copy(bundle);
        }

        public Bundle getBundle() {
            return bundle;
        }

        public Object get(String key) {
            return bundle.get(key);
        }

        private boolean isForResult() {
            return requestCode >= 1 && requestCode <= 16;
        }

        private void setCurrentScreen(Screen currentScreen) {
            this.currentScreen = currentScreen;
            currentScreen.dansho = this;
        }

        public int getRequestCode() {
            return requestCode;
        }

        private void previousSetResult() {
            if (previousScreen != null) previousScreen.onScreenResult(requestCode, resultCode, result);
        }

        private void currentCreate() {
            if (currentScreen != null) currentScreen.onCreate();
        }

        private void currentResume() {
            if (currentScreen != null) currentScreen.onResume();
        }

        private void currentStart() {
            if (currentScreen != null) currentScreen.onStart();
        }
    }
}
