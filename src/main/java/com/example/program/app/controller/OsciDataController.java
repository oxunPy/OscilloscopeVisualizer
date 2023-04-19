package com.example.program.app.controller;

import com.example.program.app.property.OsciDataProperty;
import com.example.program.app.service.OsciDataService;
import com.example.program.common.screen.NavigationScreen;
import com.example.program.util.*;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;

import java.io.IOException;
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

    private TablePagination<OsciDataProperty> tbData;

    private ListProperty<OsciDataProperty> listData = new SimpleListProperty<>(FXCollections.observableArrayList());

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

    }

    @Override
    public void onCreate() {
        table();

        spData.getChildren().add(tbData);
    }

    private void table() {
        tbData = new TablePagination<OsciDataProperty>(tableViewData()) {
            @Override
            protected Map<String, Object> getParam() {
                Map<String, Object> param = new HashMap<>();
                param.put("name", txtSearch.getText());
                param.put("fromDate", dpFromDate.getValue() == null ? DateUtil.parse("1970-01-01", "yyyy-mm-dd") : dpFromDate.getValue());
                param.put("toDate", dpToDate.getValue() == null ? DateUtil.parse("2100-01-01", "yyyy-mm-dd") : dpToDate.getValue());
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

        table.getColumns().addAll(colId, colDataName, colInfo, colToolId, colFileId);
        table.setTableMenuButtonVisible(false);

        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("12131313");
        });
        return table;
    }
}
