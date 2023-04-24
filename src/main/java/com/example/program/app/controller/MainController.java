package com.example.program.app.controller;

import com.example.program.app.App;
import com.example.program.app.Launch;
import com.example.program.app.property.OsciDataProperty;
import com.example.program.app.property.OsciToolProperty;
import com.example.program.app.service.OsciDataService;
import com.example.program.app.service.OsciToolService;
import com.example.program.common.screen.NavigationScreen;
import com.example.program.util.*;
import com.example.program.util.Dialog;
import com.example.program.util.widget.combo.ToolComboboxConverter;
import com.example.program.util.widget.combo.ToolComboboxItem;
import com.example.program.util.widget.hotkey.HotKeyListener;
import com.example.program.util.widget.hotkey.HotKeyManager;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Path;
import org.apache.commons.lang.math.NumberUtils;
import org.gillius.jfxutils.chart.ChartPanManager;
import org.gillius.jfxutils.chart.JFXChartUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

public class MainController extends NavigationScreen.Screen{
    @FXML
    private BorderPane lineChartIconPane;
    @FXML
    private BorderPane absoluteErrorPane;
    @FXML
    private BorderPane prevLineChartPane;
    @FXML
    private BorderPane nextLineChartPane;
    @FXML
    private TableColumn<OsciDataProperty, Long> colId;
    @FXML
    private TableColumn<OsciDataProperty, String> colDataName;
    @FXML
    private TableColumn<OsciDataProperty, String> colDate;
    @FXML
    private TableColumn<OsciDataProperty, String> colInfo;
    @FXML
    private TableColumn<OsciDataProperty, Long> colToolId;
    @FXML
    private TableColumn<OsciDataProperty, String> colFileName;
    @FXML
    private DatePicker dpFromDate;
    @FXML
    private DatePicker dpToDate;
    @FXML
    private Button btnFilter;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnDrawLineChart;
    @FXML
    private Button btnAbsoluteError;
    @FXML
    private Button btnPrevChart;
    @FXML
    private Button btnNextChart;
    @FXML
    private TableView<OsciDataProperty> tbData;
    @FXML
    private ComboBox<OsciToolProperty> cbTool;
    @FXML
    private TextArea txtInfoArea;
    @FXML
    private LineChart<Number, Number> lchGraphs;
    @FXML
    private CheckBox chbGraph1;
    @FXML
    private CheckBox chbGraph2;
    @FXML
    private CheckBox chbGraph3;
    @FXML
    private ColorPicker cpGraph1;
    @FXML
    private ColorPicker cpGraph2;
    @FXML
    private ColorPicker cpGraph3;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    private final XYChart.Series<Number, Number> graph1Series = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> graph2Series = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> graph3Series = new XYChart.Series<>();

    double x, y = 0;
    private final Integer MAX_UNIT_SIZE = 500;
    private IntegerProperty currentPage = new SimpleIntegerProperty(0);

    private StringProperty graph1Filename = new SimpleStringProperty();
    private StringProperty graph2Filename = new SimpleStringProperty();
    private StringProperty graph3Filename = new SimpleStringProperty();

    private final OsciToolService osciToolService = new OsciToolService();
    private final OsciDataService osciDataService = new OsciDataService();

    private ListProperty<OsciDataProperty> listData = new SimpleListProperty<>(FXCollections.observableArrayList());

    private HotKeyListener hot = event -> {
        if(event.getCode() == KeyCode.LEFT){
            prevLineChart();
        }
        else if(event.getCode() == KeyCode.RIGHT){
            nextLineChart();
        }
    };

    public MainController() {
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/main.fxml"), StringConfig.getPropertiesFromResource());

            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();
//            handleMousePosition();
        } catch (IOException ex) {
            Message.error(StringConfig.getValue("err.ui.load") + "\n" + ex);
        }
    }

    private void syncBase() {
        listData.clear();
        Map<String, Object> param = new HashMap<>();
        param.put("fromDate", DateUtil.format(DateUtil.fromLocale(dpFromDate.getValue() == null ? LocalDate.of(1970, 1, 1) : dpFromDate.getValue()), DateUtil.PATTERN2));
        param.put("toDate", DateUtil.format(DateUtil.fromLocale(dpToDate.getValue() == null ? LocalDate.of(2100, 1, 1) : dpToDate.getValue()), DateUtil.PATTERN2));
        param.put("toolId", cbTool.getValue() != null ? cbTool.getValue().getId() : null);

        listData.setAll(osciDataService.listData(param, 0, Integer.MAX_VALUE));
        tbData.scrollTo(0);
    }

    @Override
    public void onCreate() {

        dpFromDate.setValue(DateUtil.toLocale(DateUtil.atStartOfMonth(new Date())));
        dpToDate.setValue(LocalDate.now());

        syncBase();
        table();

        tbData.setItems(listData);
        tbData.setEditable(true);
        lineChartIconPane.setCenter(GlyphsDude.createIcon(FontAwesomeIcon.LINE_CHART, "22px"));
        absoluteErrorPane.setCenter(GlyphsDude.createIcon(MaterialDesignIcon.DELTA, "22px"));
        prevLineChartPane.setCenter(GlyphsDude.createIcon(MaterialDesignIcon.ARROW_LEFT, "20px"));
        nextLineChartPane.setCenter(GlyphsDude.createIcon(MaterialDesignIcon.ARROW_RIGHT, "20px"));
        btnFilter.setOnMouseClicked(event -> syncBase());
        btnClear.setOnMouseClicked(event -> {
            dpFromDate.setValue(DateUtil.toLocale(DateUtil.atStartOfMonth(new Date())));
            dpToDate.setValue(LocalDate.now());
        });

        btnDelete.setOnMouseClicked(event -> {
            if (tbData.getSelectionModel().getSelectedItem() == null) {
                Note.alert(StringConfig.getValue("err.select.item"));
                return;
            }
            Dialog.Answer response = Message.confirm(StringConfig.getValue("item.delete.request"));
            if (response == Dialog.Answer.NO) {
                event.consume();
            } else {
                deleteDataAction();
            }
        });
        btnDrawLineChart.setOnMouseClicked(event -> {
            drawLineChart();
        });
        btnAbsoluteError.setOnMouseClicked(event -> {
            int count = 0;
            if(graph1Series.getData().isEmpty()) count++;
            if(graph2Series.getData().isEmpty()) count++;
            if(graph3Series.getData().isEmpty()) count++;

            if(count >= 2){
                Note.error(StringConfig.getValue("err.calculate.absolute"));
                return;
            }

            Dialog.messageAbsoluteError(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/absolute_error_formula.png")))), absoluteErrorPercent());
        });

        // #init base defaults
        toggleCheckboxes();
        baseLineChart();
    }

    @Override
    public void onStart() {
        // zooming line chart
        zoomLineChart();

        cbTool.setCellFactory(param -> new ToolComboboxItem());
        cbTool.setConverter(new ToolComboboxConverter());
        cbTool.setButtonCell(new ToolComboboxItem());

        comboTools();

        tbData.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue != null) txtInfoArea.setText(newValue.getInfo());
        }));

        lchGraphs.getData().add(graph1Series);
        lchGraphs.getData().add(graph2Series);
        lchGraphs.getData().add(graph3Series);

        cpGraph1.valueProperty().addListener((observable, oldValue, newValue) -> {
                Node line = graph1Series.getNode().lookup(".default-color0.chart-series-line");
                String rgb = FXUtils.toRGBCode(newValue);
//                line.setStyle("-fx-stroke: " + rgb);
                ((Path) line).setStroke(newValue);
        });
        cpGraph2.valueProperty().addListener((observable, oldValue, newValue) -> {
            Node line = graph2Series.getNode().lookup(".default-color1.chart-series-line");
            String rgb = FXUtils.toRGBCode(newValue);
//            line.setStyle("-fx-stroke: " + rgb);
                ((Path) line).setStroke(newValue);
        });
        cpGraph3.valueProperty().addListener((observable, oldValue, newValue) -> {
            Node line = graph3Series.getNode().lookup(".default-color2.chart-series-line");
            String rgb = FXUtils.toRGBCode(newValue);
//            line.setStyle("-fx-stroke: " + rgb);
                ((Path) line).setStroke(newValue);
        });

        btnPrevChart.setOnMouseClicked(event -> {
            prevLineChart();
        });
        btnNextChart.setOnMouseClicked(event -> {
            nextLineChart();
        });

        HotKeyManager.getInstance().addListener(hot);
    }

    void handleMousePosition() {
        this.setOnMouseClicked(event -> {
            x = event.getX();
            y = event.getY();
        });

        this.setOnMouseDragged(event -> {
            App.stage.setX(event.getScreenX() - x);
            App.stage.setY(event.getScreenY() - y);
        });
    }

    private void table() {
        colId.prefWidthProperty().bind(tbData.widthProperty().multiply(0.1).subtract(1));
        colDataName.prefWidthProperty().bind(tbData.widthProperty().multiply(0.15).subtract(1));
        colDate.prefWidthProperty().bind(tbData.widthProperty().multiply(0.15).subtract(1));
        colDate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue() != null ? DateUtil.format(param.getValue().getDate(), DateUtil.PATTERN2) : ""));
        colInfo.prefWidthProperty().bind(tbData.widthProperty().multiply(0.3).subtract(1));
        colToolId.prefWidthProperty().bind(tbData.widthProperty().multiply(0.1).subtract(1));
        colFileName.prefWidthProperty().bind(tbData.widthProperty().multiply(0.2).subtract(1));
        colFileName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDataFile() != null ? param.getValue().getDataFile().getOriginalName() : ""));
    }

    private void comboTools() {
        Platform.runLater(() -> {
            List<OsciToolProperty> listTools = osciToolService.listTools(null);
            cbTool.setItems(FXCollections.observableArrayList(listTools));
        });
    }

    private void deleteDataAction() {
        if (osciDataService.deleteData(tbData.getSelectionModel().getSelectedItem().getId())) {
            syncBase();
            Note.info(StringConfig.getValue("info.deleted.successfully"));
        } else Note.error(StringConfig.getValue("err.db.delete"));

    }


    private void toggleCheckboxes(){
        chbGraph1.setOnMouseClicked(event -> {
            if(chbGraph1.isSelected()) {
                chbGraph2.setSelected(false);
                chbGraph3.setSelected(false);
            }
        });

        chbGraph2.setOnMouseClicked(event -> {
            if(chbGraph2.isSelected()) {
                chbGraph1.setSelected(false);
                chbGraph3.setSelected(false);
            }
        });

        chbGraph3.setOnMouseClicked(event -> {
            if(chbGraph3.isSelected()) {
                chbGraph1.setSelected(false);
                chbGraph2.setSelected(false);
            }
        });
    }

    private void baseLineChart(){
        lchGraphs.setAnimated(true);
        lchGraphs.setStyle("-fx-font-weight: bold;");
        lchGraphs.setCreateSymbols(false);
        lchGraphs.setTitle(StringConfig.getValue("label.oscilloscope.graphics"));
    }

    private void refreshLineChartColors(){

    }


    private void drawLineChart(){
        if(!(chbGraph1.isSelected() || chbGraph2.isSelected() || chbGraph3.isSelected())){
            Note.alert(StringConfig.getValue("select.item.checkbox"));
            return ;
        }

        if(tbData.getSelectionModel().getSelectedItem() == null){
            Note.alert(StringConfig.getValue("err.select.item"));
            return ;
        }

        String fileName = tbData.getSelectionModel().getSelectedItem().getDataFile().getFilename();
        File fileToDraw = new File(Launch.properties.getStr("osci.upload.file.path") + File.separator + fileName);
        if(!fileToDraw.exists()) {
            Note.error(StringConfig.getValue("err.file.notFound"));
            return;
        }

        XYChart.Series<Number, Number> linechart = null;
        if(chbGraph1.isSelected()) {
            linechart = graph1Series;
            graph1Filename.setValue(fileName);
        }
        else if(chbGraph2.isSelected()){
            linechart = graph2Series;
            graph2Filename.setValue(fileName);
        }
        else {
            linechart = graph3Series;
            graph3Filename.setValue(fileName);
        }

        List<Double> readDataFromFile = null;
        try {
            // reading data from file...
            readDataFromFile = readFromFile(new FileInputStream(fileToDraw));

           drawSeries(readDataFromFile, linechart);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void drawSeries(List<Double> data, XYChart.Series<Number, Number> linechart){
        linechart.getData().clear();
        for(int i = currentPage.get() * MAX_UNIT_SIZE; (i < currentPage.get() * MAX_UNIT_SIZE + MAX_UNIT_SIZE && i < data.size()) ; i++){
            linechart.getData().add(new XYChart.Data<>(i, data.get(i)));
        }
    }



    private List<Double> readFromFile(InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        List<Double> osciDataFileList = new ArrayList<>();
        try {
            String line;
            osciDataFileList = new ArrayList<>();
            while ((line = nextLine(reader)) != null) {
                if(isValidNumber(line)){
                    osciDataFileList.add(Double.parseDouble(line));
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("SQL file could not be read", e);
        }
        return osciDataFileList;
    }


    private String nextLine(BufferedReader reader) throws IOException {
        String line;
        do {
            line = reader.readLine();
        } while (line != null && !isValidLine(line) );
        return line;
    }

    private boolean isValidLine(String line) {
        return !line.trim().isEmpty();
    }

    private boolean isValidNumber(String line){
        return isValidLine(line) && NumberUtils.isNumber(line);
    }

    private Double absoluteErrorPercent(){
        return 0.34;
    }


    private void nextLineChart(){
//        Note.info("NEXT LINE CHART");
        currentPage.setValue(currentPage.get() + 1);
        updateGraphics();
    }

    private void prevLineChart(){
//        Note.info("PREV LINE CHART");
        if(currentPage.get() > 0) currentPage.setValue(currentPage.get() - 1);
        updateGraphics();
    }

    private void updateGraphics(){
        try {
            if (!graph1Series.getData().isEmpty()) {
                File fileToDraw = new File(Launch.properties.getStr("osci.upload.file.path") + File.separator + graph1Filename.get());
                if(!fileToDraw.exists()) Note.error(StringConfig.getValue("err.file.notFound"));
                else drawSeries(readFromFile(Files.newInputStream(fileToDraw.toPath())), graph1Series);
            }
            if (!graph2Series.getData().isEmpty()) {
                File fileToDraw = new File(Launch.properties.getStr("osci.upload.file.path") + File.separator + graph2Filename.get());
                if(!fileToDraw.exists()) Note.error(StringConfig.getValue("err.file.notFound"));
                else drawSeries(readFromFile(Files.newInputStream(fileToDraw.toPath())), graph2Series);
            }
            if (!graph3Series.getData().isEmpty()) {
                File fileToDraw = new File(Launch.properties.getStr("osci.upload.file.path") + File.separator + graph3Filename.get());
                if(!fileToDraw.exists()) Note.error(StringConfig.getValue("err.file.notFound"));
                else drawSeries(readFromFile(Files.newInputStream(fileToDraw.toPath())), graph3Series);
            }
        }
        catch(IOException ex){
            Note.error(StringConfig.getValue("err.file.notFound"));
        }
    }

    private void zoomLineChart(){
        //zooming linechart
        ChartPanManager panner = new ChartPanManager(lchGraphs);
        //while presssing the left mouse button, you can drag to navigate
        panner.setMouseFilter(mouseEvent -> {
            if(mouseEvent.getButton() == MouseButton.PRIMARY){ //set your custom combination to trigger navigation

            }
            else{
                mouseEvent.consume();
            }
        });
        panner.start();

        JFXChartUtil.setupZooming(lchGraphs, mouseEvent -> {
            if(mouseEvent.getButton() != MouseButton.SECONDARY){ //set your custom combination to trigger rectangle zooming
                mouseEvent.consume();
            }
        });

    }
}
