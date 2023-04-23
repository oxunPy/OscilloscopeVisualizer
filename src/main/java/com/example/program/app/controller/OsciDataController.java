package com.example.program.app.controller;

import com.example.program.app.property.OsciDataProperty;
import com.example.program.app.service.OsciDataService;
import com.example.program.common.screen.Bundle;
import com.example.program.common.screen.NavigationScreen;
import com.example.program.util.*;
import com.example.program.util.Dialog;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OsciDataController extends NavigationScreen.Screen {

    @FXML
    private DatePicker dpFromDate;
    @FXML
    private DatePicker dpToDate;
    @FXML
    private TextField txtSearch;
    @FXML
    private StackPane spData;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnRefresh;
    @FXML
    private Button btnClear;

    private TablePagination<OsciDataProperty> tbData;

    private OsciDataService osciDataService = new OsciDataService();

    public OsciDataController() {
        try {
            FXMLLoader fxml = new FXMLLoader(this.getClass().getResource("/view/osci_data.fxml"), StringConfig.getPropertiesFromResource());
            fxml.setController(this);
            fxml.setRoot(this);
            fxml.load();
        } catch (IOException e) {
            Note.error(StringConfig.getValue("err.ui.load"));
        }
    }

    @Override
    public void onStart() {
        btnAdd.setOnMouseClicked(event -> {
            NavigationScreen.Dansho dansho = new NavigationScreen.Dansho(this, FileUploadController.class,
                    Bundle.create());
            startScreenForResult(dansho, 15);
        });

        btnRefresh.setOnMouseClicked(event -> {
            tbData.reload();
        });
        btnDelete.setOnMouseClicked(event -> {
            if (tbData.getSelectionModel().getSelectedItem() == null) {
                Note.alert(StringConfig.getValue("err.select.item"));
                return;
            }
            Dialog.Answer response = Message.confirm(StringConfig.getValue("item.delete.request"));
            if(response == Dialog.Answer.NO){
                event.consume();
            }
            else{
                deleteAction();
            }
        });

        btnClear.setOnMouseClicked(event -> {
            txtSearch.setText("");
            dpFromDate.setValue(DateUtil.toLocale(DateUtil.atStartOfMonth(new Date())));
            dpToDate.setValue(LocalDate.now());
            tbData.reload();
        });
    }

    @Override
    public void onCreate() {
        dpFromDate.setValue(DateUtil.toLocale(DateUtil.atStartOfMonth(new Date())));
        dpToDate.setValue(LocalDate.now());

        table();

        spData.getChildren().add(tbData);
    }

    private void table() {
        tbData = new TablePagination<OsciDataProperty>(tableViewData()) {
            @Override
            protected Map<String, Object> getParam() {
                Map<String, Object> param = new HashMap<>();
                param.put("name", txtSearch.getText());
                param.put("fromDate", dpFromDate.getValue() == null ? "1970-01-01" : DateUtil.format(DateUtil.fromLocale(dpFromDate.getValue()), DateUtil.PATTERN2));
                param.put("toDate", dpToDate.getValue() == null ? "2100-01-01" : DateUtil.format(DateUtil.fromLocale(dpToDate.getValue()), DateUtil.PATTERN2));
                return param;
            }

            @Override
            protected DataGrid<OsciDataProperty> getData(Map<String, Object> param, int page, int rows) {
                return osciDataService.dataGrid(param, page, rows);
            }
        };
    }

    private TableView<OsciDataProperty> tableViewData() {
        TableView<OsciDataProperty> table = new TableView<>();

        TableColumn<OsciDataProperty, Long> colId = new TableColumn<>(StringConfig.getValue("label.id"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.prefWidthProperty().bind(table.widthProperty().multiply(0.1).subtract(1));

        TableColumn<OsciDataProperty, String> colDataName = new TableColumn<>(StringConfig.getValue("label.name"));
        colDataName.setCellValueFactory(new PropertyValueFactory<>("dataName"));
        colDataName.prefWidthProperty().bind(table.widthProperty().multiply(0.2).subtract(1));

        TableColumn<OsciDataProperty, String> colInfo = new TableColumn<>(StringConfig.getValue("label.info"));
        colInfo.setCellValueFactory(new PropertyValueFactory<>("info"));
        colInfo.prefWidthProperty().bind(table.widthProperty().multiply(0.3).subtract(1));

        TableColumn<OsciDataProperty, Long> colToolId = new TableColumn<>(StringConfig.getValue("label.tool.id"));
        colToolId.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getOsciToolId()));
        colToolId.prefWidthProperty().bind(table.widthProperty().multiply(0.1).subtract(1));

        TableColumn<OsciDataProperty, Long> colFileId = new TableColumn<>(StringConfig.getValue("label.file.id"));
        colFileId.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getOsciFileId()));
        colFileId.prefWidthProperty().bind(table.widthProperty().multiply(0.1).subtract(1));

        TableColumn<OsciDataProperty, String> colFileName = new TableColumn<>(StringConfig.getValue("label.file.name"));
        colFileName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDataFile() != null ? param.getValue().getDataFile().getOriginalName() : ""));
        colFileName.prefWidthProperty().bind(table.widthProperty().multiply(0.2).subtract(1));

        table.getColumns().addAll(colId, colDataName, colInfo, colToolId, colFileId, colFileName);
        table.setTableMenuButtonVisible(false);

        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("12131313");
        });
        return table;
    }

    private void deleteAction() {
        //delete
        if (osciDataService.deleteData(tbData.getSelectionModel().getSelectedItem().getId())) tbData.reload();
        else Note.error(StringConfig.getValue("err.db.delete"));
    }
}
