package com.example.program.app.controller;

import com.example.program.app.App;
import com.example.program.app.Launch;
import com.example.program.app.entity.OsciFileEntity;
import com.example.program.app.property.OsciDataProperty;
import com.example.program.app.property.OsciFileProperty;
import com.example.program.app.property.OsciToolProperty;
import com.example.program.app.service.OsciDataService;
import com.example.program.app.service.OsciFileService;
import com.example.program.app.service.OsciToolService;
import com.example.program.common.screen.Bundle;
import com.example.program.common.screen.NavigationScreen;
import com.example.program.util.DateUtil;
import com.example.program.util.Note;
import com.example.program.util.StringConfig;
import com.example.program.util.widget.combo.ToolComboboxConverter;
import com.example.program.util.widget.combo.ToolComboboxItem;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.stage.FileChooser;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class FileUploadController extends NavigationScreen.Screen{

    @FXML
    private Button btnBrowseFile;
    @FXML
    private ImageView imgDropBox;
    @FXML
    private TextField tfDirFiles;
    @FXML
    private DatePicker dpDate;
    @FXML
    private ComboBox<OsciToolProperty> cbTool;
    @FXML
    private TextField txtDataName;
    @FXML
    private TextArea txtInfo;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;

    private ObjectProperty<File> draggedFile = new SimpleObjectProperty<>();
    private ObjectProperty<OsciDataProperty> osciDataObject = new SimpleObjectProperty<>(new OsciDataProperty(true));
    private OsciDataService osciDataService = new OsciDataService();
    private OsciFileService osciFileService = new OsciFileService();
    private OsciToolService osciToolService = new OsciToolService();


    public FileUploadController(){
        try{
            FXMLLoader fxml = new FXMLLoader(this.getClass().getResource("/view/file_upload.fxml"), StringConfig.getPropertiesFromResource());
            fxml.setController(this);
            fxml.setRoot(this);
            fxml.load();
        } catch (IOException e) {
            Note.error(StringConfig.getValue("err.ui.load"));
        }
    }

    @Override
    public void onStart(){
        tfDirFiles.setText(Launch.properties.getStr("osci.upload.file.path"));
        tfDirFiles.setDisable(true);

        dpDate.setValue(LocalDate.now());
        txtDataName.textProperty().addListener((observable, oldValue, newValue) -> {
            osciDataObject.get().setDataName(txtDataName.getText());
        });
        txtInfo.textProperty().addListener((observable, oldValue, newValue) -> {
            osciDataObject.get().setInfo(txtInfo.getText());
        });

        cbTool.setCellFactory(param -> new ToolComboboxItem());
        cbTool.setConverter(new ToolComboboxConverter());
        cbTool.setButtonCell(new ToolComboboxItem());

        comboTools();
    }

    @Override
    public void onCreate() {

        btnSave.disableProperty().bind(new BooleanBinding() {
            {
                bind(txtDataName.textProperty(), dpDate.valueProperty(), draggedFile, cbTool.valueProperty());
            }
            @Override
            protected boolean computeValue() {
                return StringUtils.isEmpty(txtDataName.getText()) || dpDate.getValue() == null || draggedFile.get() == null || cbTool.getValue() == null;
            }
        });

        imgDropBox.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (event.getGestureSource() != imgDropBox && db.hasFiles()) {
//                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                File file = db.getFiles().get(0);
                draggedFile.set(file);
//                Note.info(file.getName());
            }
//            event.consume();
        });

        imgDropBox.setOnDragDropped(event -> {
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
                    draggedFile.set(selectedFile);
                }
                else{
                    Note.error(StringConfig.getValue("err.file.type"));
                }
            }
        });

        btnSave.setOnMouseClicked(event -> {
            saveOsciData();
            NavigationScreen.Dansho dansho = new NavigationScreen.Dansho(MainController.class, Bundle.create());
            startScreenForResult(dansho, 15);
        });
        btnCancel.setOnMouseClicked(event -> {
            NavigationScreen.Dansho dansho = new NavigationScreen.Dansho(OsciDataController.class, Bundle.create());
            startScreenForResult(dansho, 11);
        });
    }

    private void saveOsciData(){

        try {
            if (draggedFile.get() != null) {
                OsciFileProperty fileProperty = osciFileService.saveFile(draggedFile.get(), OsciFileEntity.FileType.OSCI_DATA);
                osciDataObject.get().setDataFile(fileProperty);
                osciDataObject.get().setOsciFileId(fileProperty.getId());
            }
            osciDataObject.get().setDate(DateUtil.fromLocale(dpDate.getValue()));
            osciDataObject.get().setOsciToolId(cbTool.getValue().getId());
            osciDataService.saveOsciData(osciDataObject.get());
            finish(RESULT_OK);
            Note.info(StringConfig.getValue("info.saved.successfully"));
        }
        catch(Exception ex){
            Note.error(StringConfig.getValue("err.db.saveOrUpdate"));
        }
    }

    private void comboTools(){
        Platform.runLater(() -> {
            List<OsciToolProperty> listTools = osciToolService.listTools(null);
            cbTool.setItems(FXCollections.observableArrayList(listTools));
        });
    }
}
