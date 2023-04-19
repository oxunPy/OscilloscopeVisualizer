package com.example.program.util;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import java.util.Map;

public abstract class TablePagination<T> extends Pagination {

    //    private int page = 1;// current page
    private int rows = 20;// number of records in each page
    private TableView<T> table;

    public TablePagination(TableView<T> table) {
        this.table = table;

        createPagination();
    }

    public TableView.TableViewSelectionModel<T> getSelectionModel() {
        return table.getSelectionModel();
    }

//    public void setTable(TableView<T> table) {
//        this.table = table;
//    }

    protected abstract Map<String, Object> getParam();

    protected abstract DataGrid<T> getData(Map<String, Object> param, int page, int rows);

    private Node createPage(int page, int rows) {
        DataGrid<T> datagrid = getData(getParam(), page + 1, rows);

        setPageCount((datagrid.getTotal() / rows + 1));
        setCurrentPageIndex(page);

        table.setItems(FXCollections.observableArrayList(datagrid.getRows()));

        return new BorderPane(table);
    }

    private Pagination createPagination() {
        setPageFactory(param -> createPage(param, rows));
        return this;
    }

    public void reload() {
        createPagination();
//        getPageFactory().call(0);
    }
}