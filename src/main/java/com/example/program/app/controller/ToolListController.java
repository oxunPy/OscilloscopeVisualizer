package com.example.program.app.controller;

import com.example.program.app.property.OsciToolProperty;
import com.example.program.app.service.OsciToolService;
import com.example.program.common.screen.Bundle;
import com.example.program.common.screen.NavigationScreen;
import com.example.program.util.Note;
import com.example.program.util.StringConfig;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;

import java.io.IOException;
import java.util.Objects;

public class ToolListController extends NavigationScreen.Screen{
    @FXML
    private TableColumn<OsciToolProperty, Long> colId;
    @FXML
    private TableColumn<OsciToolProperty, String> colName;
    @FXML
    private TableColumn<OsciToolProperty, String> colInfo;
    @FXML
    private TableColumn<OsciToolProperty, Long> colImageId;
    @FXML
    private TableColumn<OsciToolProperty, String> colModel;
    @FXML
    private TableColumn<OsciToolProperty, ImageView> colImageFile;
    @FXML
    private TableView<OsciToolProperty> tbTool;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;

    private OsciToolService osciToolService = new OsciToolService();

    private ListProperty<OsciToolProperty> listTools = new SimpleListProperty<>(FXCollections.observableArrayList());

    public ToolListController(){
        try{
            FXMLLoader fxml = new FXMLLoader(this.getClass().getResource("/view/tool_list.fxml"), StringConfig.getPropertiesFromResource());
            fxml.setController(this);
            fxml.setRoot(this);
            fxml.load();
        } catch (IOException e) {
            Note.error(StringConfig.getValue("err.ui.load"));
        }
    }


    private void syncBase(){
        listTools.clear();

        listTools.setAll(osciToolService.listTools());
        tbTool.scrollTo(0);
    }


    @Override
    public void onStart(){

        if(getDansho().getResultCode() == NavigationScreen.Screen.RESULT_OK){
            syncBase();
        }

        btnAdd.setOnMouseClicked(event -> {
            NavigationScreen.Dansho dansho = new NavigationScreen.Dansho(this, ToolAddEditController.class,
                    Bundle.create());
            startScreenForResult(dansho, 15);
        });

        btnEdit.setOnMouseClicked(event -> {
            if(tbTool.getSelectionModel().getSelectedItem() != null){
                itemEdit(tbTool.getSelectionModel().getSelectedItem());
            }
        });

        btnDelete.setOnMouseClicked(event -> {
            deleteAction();
        });
    }

    @Override
    public void onCreate() {
        syncBase();
        table();

        tbTool.setItems(listTools);
    }

    private void table(){
        colId.prefWidthProperty().bind(tbTool.widthProperty().multiply(0.1).subtract(1));
        colName.prefWidthProperty().bind(tbTool.widthProperty().multiply(0.2).subtract(1));
        colInfo.prefWidthProperty().bind(tbTool.widthProperty().multiply(0.2).subtract(1));
        colImageId.prefWidthProperty().bind(tbTool.widthProperty().multiply(0.1).subtract(1));
        colModel.prefWidthProperty().bind(tbTool.widthProperty().multiply(0.2).subtract(1));
        colImageFile.setCellValueFactory(param -> new SimpleObjectProperty<>(new ImageView(new Image(Objects.requireNonNull(osciToolService.getToolPhoto(param.getValue().getId()))))));
        colImageFile.prefWidthProperty().bind(tbTool.widthProperty().multiply(0.2).subtract(1));

        tbTool.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {
                    itemEdit(tbTool.getSelectionModel().getSelectedItem());
                }
            }
        });
    }

    private void itemEdit(OsciToolProperty property){
        NavigationScreen.Dansho dansho = new NavigationScreen.Dansho(this, ToolAddEditController.class,
                Bundle.create().put("tool", property));
        startScreenForResult(dansho, 15);
    }

    private void deleteAction(){
        OsciToolProperty selected = tbTool.getSelectionModel().getSelectedItem();
        if(selected != null){
            osciToolService.deleteTool(selected.getId());
            Note.info(StringConfig.getValue("info.deleted.successfully"));
            syncBase();
        }
    }
}
