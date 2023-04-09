package com.example.program.app.controller;

import com.example.program.app.App;
import com.example.program.common.screen.NavigationScreen;
import com.example.program.util.Note;
import com.example.program.util.StringConfig;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FileUploadController extends NavigationScreen.Screen{

    @FXML
    private ImageView imgDragFiles;
    @FXML
    private Button btnBrowseFile;

    public FileUploadController(){
        try{
            FXMLLoader fxml = new FXMLLoader(this.getClass().getResource("/view/fileupload.fxml"), StringConfig.getPropertiesFromResource());
            fxml.setController(this);
            fxml.setRoot(this);
            fxml.load();
        } catch (IOException e) {
            Note.error(StringConfig.getValue("err.ui.load"));
        }
    }

    @Override
    public void onCreate() {
        imgDragFiles.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (event.getGestureSource() != imgDragFiles && db.hasFiles()) {
//                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                File file = db.getFiles().get(0);

            }
//            event.consume();
        });

        imgDragFiles.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
//                dropped.setText(db.getFiles().toString());
                Note.alert(db.getFiles().get(0).getName());
                success = true;
            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);

            event.consume();
        });


        btnBrowseFile.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(App.stage);

            if(selectedFile != null){
                String ext = selectedFile.getName().substring(selectedFile.getName().lastIndexOf(".") + 1);
                if(ext.equals("txt") || ext.equals("xlxs") || ext.equals("xls") || ext.equals("csv")){
                    System.out.println(selectedFile);
                }
                else{
                    Note.error(StringConfig.getValue("err.file.type"));
                }
            }
        });
    }
}
