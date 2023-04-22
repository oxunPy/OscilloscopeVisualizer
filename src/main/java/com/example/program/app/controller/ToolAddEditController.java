package com.example.program.app.controller;

import com.example.program.app.App;
import com.example.program.app.entity.OsciFileEntity;
import com.example.program.app.property.OsciFileProperty;
import com.example.program.app.property.OsciToolProperty;
import com.example.program.app.service.OsciFileService;
import com.example.program.app.service.OsciToolService;
import com.example.program.common.screen.NavigationScreen;
import com.example.program.util.Note;
import com.example.program.util.StringConfig;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class ToolAddEditController extends NavigationScreen.Screen{

    @FXML
    private Button btnBack;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private TextField tfDirImage;
    @FXML
    private TextField txtModel;
    @FXML
    private TextField txtToolName;
    @FXML
    private TextArea txtInfo;
    @FXML
    private Button btnBrowseImage;
    @FXML
    private BorderPane browseImagePane;

    private ObjectProperty<OsciToolProperty> osciToolObject = new SimpleObjectProperty<>(new OsciToolProperty());

    private ObjectProperty<File> browseImgObject = new SimpleObjectProperty<>();
    private OsciToolService osciToolService = new OsciToolService();
    private OsciFileService osciFileService = new OsciFileService();


    public ToolAddEditController(){
        try{
            FXMLLoader fxml = new FXMLLoader(this.getClass().getResource("/view/add_tool.fxml"), StringConfig.getPropertiesFromResource());
            fxml.setController(this);
            fxml.setRoot(this);
            fxml.load();
        } catch (IOException e) {
            Note.error(StringConfig.getValue("err.ui.load"));
        }
    }

    @Override
    public void onStart(){
        btnBack.setOnMouseClicked(event -> finish(RESULT_CANCEL));
        btnCancel.setOnMouseClicked(event -> finish(RESULT_CANCEL));
        btnSave.setOnMouseClicked(event -> saveTool());

        osciToolObject.get().nameProperty().bind(txtToolName.textProperty());
        osciToolObject.get().modelProperty().bind(txtModel.textProperty());
        osciToolObject.get().infoProperty().bind(txtInfo.textProperty());

    }

    @Override
    public void onCreate() {

        NavigationScreen.Dansho dansho = getDansho();
        if(dansho != null && dansho.getBundle().has("tool")){
            osciToolObject.set((OsciToolProperty) dansho.get("tool"));
            txtInfo.setText(osciToolObject.get().getInfo());
            txtModel.setText(osciToolObject.get().getModel());
            txtToolName.setText(osciToolObject.get().getName());
        }


        browseImagePane.setCenter(GlyphsDude.createIcon(FontAwesomeIcon.FILE, "22px"));

        btnBrowseImage.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(App.stage);

            if(selectedFile != null){
                String ext = selectedFile.getName().substring(selectedFile.getName().lastIndexOf(".") + 1);
                if(ext.equals("jpg") || ext.equals("png") || ext.equals("gif")){
                    browseImgObject.set(selectedFile);
                    tfDirImage.setText(selectedFile.getName());
                }
                else{
                    Note.error(StringConfig.getValue("err.file.type"));
                }
            }
        });
    }

    private void saveTool(){
        try {
            if (browseImgObject.get() != null) {
                OsciFileProperty imgProperty = osciFileService.saveFile(browseImgObject.get(), OsciFileEntity.FileType.IMG);
                osciToolObject.get().setToolImage(imgProperty);
                osciToolObject.get().setImageId(imgProperty.getId());
            }
            osciToolService.saveOsciTool(osciToolObject.get());
            finish(RESULT_OK);
            Note.info(StringConfig.getValue("info.saved.successfully"));
        }
        catch(Exception ex){
            Note.error(StringConfig.getValue("err.db.saveOrUpdate"));
        }
    }
}
