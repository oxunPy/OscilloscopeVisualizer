package com.example.program.util.widget.combo;

import com.example.program.app.property.OsciLanguageProperty;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class LanguageComboboxItem extends ListCell<OsciLanguageProperty> {

    @Override
    protected void updateItem(OsciLanguageProperty item, boolean empty){
        super.updateItem(item, empty);
        if(empty){
            setText("");
        }
        else{
            setText(item.getCode());
        }
        if(item != null) setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(String.format("/img/flags/%s.png", item.getCode()))))));   // set flag image
    }
}
