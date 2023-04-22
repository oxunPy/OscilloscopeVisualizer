package com.example.program.util.widget.combo;

import com.example.program.app.property.OsciToolProperty;
import javafx.scene.control.ListCell;

public class ToolComboboxItem extends ListCell<OsciToolProperty> {
    @Override
    protected void updateItem(OsciToolProperty item, boolean empty){
        super.updateItem(item, empty);
        if(empty){
            setText("");
        }
        else{
            setText(item.getName());
        }
    }
}
