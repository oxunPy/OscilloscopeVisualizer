package com.example.program.common.navigation;

import com.example.program.app.AppManager;
import com.example.program.app.controller.AppController;
import com.example.program.common.screen.Bundle;
import com.example.program.common.screen.NavigationScreen;
import com.sun.istack.NotNull;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Navigation {


    public interface Action {
        void onAction();
    }

    private ListProperty<NavigationItem> children = new SimpleListProperty<>(FXCollections.observableArrayList());
    private AppController context;
    private AppManager manager;
    private boolean singleOpen = false;

    public Navigation(@NotNull AppController context, @NotNull AppManager manager) {
        this.context = context;
        this.manager = manager;
    }

    public Navigation add(NavigationItem item) {
        this.children.add(item);
        return this;
    }


    public Navigation singleOpen() {
        singleOpen = true;
        return this;
    }

    public void update(VBox content) {
        if (content == null)
            return;

        ToggleGroup group = new ToggleGroup();
        ToggleGroup child = new ToggleGroup();

        for (NavigationItem item : children) {

            if (item.isGroup()) {
                groupView(content, item, group, child);
            } else {
                itemView(content, item, child);
            }

            if (item.button != null && item.isGroup()) {
                item.button.setSelected(item.select);
                if (!item.select) {
                    item.hideContent();
                }
            }
        }


    }

    private void groupView(VBox content, NavigationItem group, ToggleGroup groups, ToggleGroup child) {

/*        if (!group.checkPermission(this))
            return;*/

        ToggleButton groupButton = new ToggleButton();
        groupButton.getStyleClass().add(0, "menus");
        groupButton.getStyleClass().add(1, "menu-grupo");
        groupButton.setMaxWidth(1.7976931348623157E308);
        if (singleOpen)
            groupButton.setToggleGroup(groups);
        groupButton.setText(group.title.get());
        groupButton.setId(group.title.get() + "-group");
        groupButton.setAlignment(Pos.CENTER_LEFT);
        groupButton.setSelected(group.select);
        group.button = groupButton;


        groupButton.selectedProperty().addListener((observable, oldValue, newValue) -> showOrHide(group, content));


        if (group.iconStyleClass.get() != null)
            groupButton.getStyleClass().add(group.iconStyleClass.get());


        VBox childrenContent = new VBox();
        childrenContent.setId(group.title.get() + "-content");
        childrenContent.getStyleClass().add("box-submenus");


        group.childrenContent = childrenContent;
        ToggleGroup childGroup = null;
        for (NavigationItem item : group.children) {

            if (childGroup == null)
                childGroup = new ToggleGroup();
            if (item.isGroup()) {
                groupView(childrenContent, item, childGroup, child);
            } else {
                itemView(childrenContent, item, child);
            }
        }
        content.getChildren().add(groupButton);
        content.getChildren().add(childrenContent);
    }

    private void itemView(VBox childrenContent, NavigationItem item, ToggleGroup child) {
/*        if (!item.checkPermission(this))
            return;*/

        HBox hBox = new HBox();


        ToggleButton itemButton = new ToggleButton();
        itemButton.getStyleClass().add("submenus");
        itemButton.setToggleGroup(child);
        itemButton.setId(item.title.get() + "-item");
        itemButton.setText(item.title.get());
        itemButton.setMnemonicParsing(false);
        itemButton.setAlignment(Pos.CENTER_LEFT);
        itemButton.setSelected(item.select);
        itemButton.setMaxWidth(1.7976931348623157E308);
        item.button = itemButton;

//        if (item.iconStyleClass.get() != null)
//            itemButton.getStyleClass().add(item.iconStyleClass.get());

        itemButton.setOnAction(event -> itemOnAction(item));

        item.itemContent = hBox;
        hBox.getChildren().add(itemButton);

        if (item.badge.get() != null && !item.badge.get().equals("0")){

            Label label = new Label(item.badge.get());
            label.getStyleClass().addAll("lbl", "lbl-notification");
            hBox.getChildren().add(label);
        }


        childrenContent.getChildren().add(hBox);

    }

    private void itemOnAction(NavigationItem item) {
        if (item.action != null)
            item.action.onAction();
        else if (item.screenClazz != null) {
            NavigationScreen.Dansho dansho = new NavigationScreen.Dansho(item.screenClazz, item.bundle.get());
            if (context != null) {
                context.startScreen(dansho);
            }
        }
    }

    private void showOrHide(NavigationItem item, VBox content) {

        if (item.button.isSelected()) {
            item.button.getStyleClass().remove(2);
            item.button.getStyleClass().add(2, "menu-grupo");
            item.showContent(this);
        } else {
            item.button.getStyleClass().remove(2);
            item.button.getStyleClass().add(2, "menu-grupo-inativo");
            item.hideContent();
        }
    }

    public static class NavigationItem {

        private StringProperty title = new SimpleStringProperty();
        private StringProperty badge = new SimpleStringProperty();
        private ObjectProperty<Bundle> bundle = new SimpleObjectProperty<>();
        private Class<? extends NavigationScreen.Screen> screenClazz;
//        private Permission accessPermission;
        private boolean visible = true;
//        private Permission.Group group;
        private Action action;
        private StringProperty iconStyleClass = new SimpleStringProperty();
        private ListProperty<NavigationItem> children = new SimpleListProperty<>(FXCollections.observableArrayList());
        private boolean select;

        private ToggleButton button;
        private HBox itemContent;
        private VBox childrenContent;


        NavigationItem() {

        }

        public NavigationItem(Class<? extends NavigationScreen.Screen> screenClazz, String title, Bundle bundle) {
            this.title.setValue(title);
            this.bundle.setValue(bundle);
            this.screenClazz = screenClazz;
        }

        public NavigationItem(Class<? extends NavigationScreen.Screen> screenClazz, String title, String badge, Bundle bundle) {
            this.title.setValue(title);
            this.badge.setValue(badge);
            this.bundle.setValue(bundle);
            this.screenClazz = screenClazz;
        }


        public NavigationItem title(String title) {
            this.title.setValue(title);
            return this;
        }
        public NavigationItem badge(String badge) {
            this.badge.setValue(badge);
            return this;
        }

        public NavigationItem select() {
            this.select = true;
            return this;
        }

        public NavigationItem screen(Class<? extends NavigationScreen.Screen> screenClazz, Bundle bundle) {
            this.bundle.setValue(bundle);
            this.screenClazz = screenClazz;
            return this;
        }

/*        public NavigationItem permission(Permission permission) {
            this.accessPermission = permission;
            return this;
        }*/

        /*public NavigationItem group(Permission.Group group) {
            this.group = group;
            return this;
        }*/

        public NavigationItem visible(boolean visible) {
            this.visible = visible;
            return this;
        }

        public NavigationItem iconStyleClass(String iconStyleClass) {
            this.iconStyleClass.setValue(iconStyleClass);
            return this;
        }

        public NavigationItem action(Action action) {
            this.action = action;
            return this;
        }


        public NavigationItem addChild(NavigationItem item) {
            children.add(item);
            return this;
        }

        private boolean isGroup() {
            return children.size() > 0;
        }

        private void hideContent() {
            if (childrenContent == null)
                return;
            for (NavigationItem child : children) {
                if (child.itemContent == null)
                    continue;
                if (childrenContent.getChildren().contains(child.itemContent)) {
                    childrenContent.getChildren().remove(child.itemContent);
                }
            }
        }

        private void showContent(Navigation navigation) {
            if (childrenContent == null)
                return;
            for (NavigationItem child : children) {
                if (child.itemContent == null /*&& !child.checkPermission(navigation)*/)
                    continue;
                if (!childrenContent.getChildren().contains(child.itemContent)) {
                    childrenContent.getChildren().add(child.itemContent);
                }
            }
        }

/*
        private boolean checkPermission(Navigation navigation) {
            if (navigation == null || navigation.manager == null)
                return false;
            if (isGroup()) {
                for (NavigationItem child : children) {
                    if (visible && child.checkPermission(navigation))
                        return true;
                }
            } else {
                return visible && (navigation.manager.hasPermission(accessPermission) || navigation.manager.hasGroup(group));
            }
            return false;
        }
*/


    }

    public static NavigationItem item() {
        return new NavigationItem();
    }
}

