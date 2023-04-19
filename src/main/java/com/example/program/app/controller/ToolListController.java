package com.example.program.app.controller;

import com.example.program.app.property.OsciToolProperty;
import com.example.program.app.service.OsciToolService;
import com.example.program.common.navigation.Navigation;
import com.example.program.common.screen.Bundle;
import com.example.program.common.screen.NavigationScreen;
import com.example.program.util.Note;
import com.example.program.util.StringConfig;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;

import java.io.IOException;

public class ToolListController extends NavigationScreen.Screen{
    @FXML
    private TableColumn<OsciToolProperty, Long> colId;
    @FXML
    private TableColumn<OsciToolProperty, String> colName;
    @FXML
    private TableColumn<OsciToolProperty, String> colCharacteristic;
    @FXML
    private TableColumn<OsciToolProperty, String> colInfo;
    @FXML
    private TableColumn<OsciToolProperty, Long> colImageFileId;
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

        btnAdd.setOnMouseClicked(event -> {
            NavigationScreen.Dansho dansho = new NavigationScreen.Dansho(this, ToolAddEditController.class,
                    Bundle.create());
            startScreenForResult(dansho, 15);
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
        colCharacteristic.prefWidthProperty().bind(tbTool.widthProperty().multiply(0.3).subtract(1));
        colInfo.prefWidthProperty().bind(tbTool.widthProperty().multiply(0.3).subtract(1));
        colImageFileId.prefWidthProperty().bind(tbTool.widthProperty().multiply(0.1).subtract(1));

        tbTool.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {
                    itemEdit(tbTool.getSelectionModel().getSelectedItem());
                }
            }
        });
    }

    private void itemEdit(OsciToolProperty property){
        System.out.println(property);
    }
}
