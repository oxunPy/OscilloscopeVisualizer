package com.example.program.app.view;

import com.example.program.app.property.OsciFileProperty;
import com.example.program.app.stages.LayoutAppStage;
import com.example.program.util.*;
import com.example.program.util.widget.hotkey.HotKeyListener;
import com.example.program.util.widget.hotkey.HotKeyManager;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Path;
import javafx.stage.FileChooser;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.gillius.jfxutils.chart.ChartPanManager;
import org.gillius.jfxutils.chart.JFXChartUtil;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class MainWindow extends AnchorPane {
    @FXML
    private BorderPane prevLineChartPane;
    @FXML
    private BorderPane nextLineChartPane;

    @FXML
    private TableView<OsciFileProperty> tbDataFiles;
    @FXML
    private TableColumn<OsciFileProperty, String> colFileName;

    // Graph items
    @FXML
    private Button btnPrevChart;
    @FXML
    private Button btnNextChart;
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

    @FXML
    private ImageView imgDropBox;
    @FXML
    private Button btnBrowseFile;
    private final ObjectProperty<File> fileOnFly = new SimpleObjectProperty<>();

    private final XYChart.Series<Number, Number> graph1Series = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> graph2Series = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> graph3Series = new XYChart.Series<>();

    double x, y = 0;
    private final Integer MAX_UNIT_SIZE = 500;
    private final Integer MAX_WAVE_DATA = 250_000;
    private final IntegerProperty currentPage = new SimpleIntegerProperty(0);

    private final StringProperty graph1Filename = new SimpleStringProperty();
    private final StringProperty graph2Filename = new SimpleStringProperty();
    private final StringProperty graph3Filename = new SimpleStringProperty();

    private final ListProperty<OsciFileProperty> listData = new SimpleListProperty<>(FXCollections.observableArrayList());

    private final HotKeyListener hot = event -> {
        if(event.getCode() == KeyCode.LEFT){
            prevLineChart();
        }
        else if(event.getCode() == KeyCode.RIGHT){
            nextLineChart();
        }
    };

    public MainWindow() {
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/main.fxml"), StringConfig.getPropertiesFromResource());

            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();

            onCreate();
            onStart();
            // handleMousePosition();
        } catch (IOException ex) {
            System.out.println(StringConfig.getValue("err.ui.load") + "\n" + ex);
        }
    }

    public void onCreate() {
        tbDataFiles.setItems(listData);
        tbDataFiles.setEditable(true);
        prevLineChartPane.setCenter(GlyphsDude.createIcon(MaterialDesignIcon.ARROW_LEFT, "20px"));
        nextLineChartPane.setCenter(GlyphsDude.createIcon(MaterialDesignIcon.ARROW_RIGHT, "20px"));

        fileOnFly.addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                OsciFileProperty property = new OsciFileProperty();
                property.setFilename(newValue.getAbsolutePath());
                listData.add(property);
            }
        });

//        btnDrawLineChart.setOnMouseClicked(event -> {
//            drawLineChart();
//        });

        handleClickBrowse();
        handleDragFile();

        // init base defaults
        toggleCheckboxes();
        baseLineChart();
    }

    public void onStart() {
        // zooming line chart
        zoomLineChart();
        tbDataFiles.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {

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
            LayoutAppStage.stage.setX(event.getScreenX() - x);
            LayoutAppStage.stage.setY(event.getScreenY() - y);
        });
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
            // Note.alert(StringConfig.getValue("select.item.checkbox"));
            return ;
        }

        if(tbDataFiles.getSelectionModel().getSelectedItem() == null){
            // Note.alert(StringConfig.getValue("err.select.item"));
            return ;
        }

        String fileName = tbDataFiles.getSelectionModel().getSelectedItem().getFilename();
        File fileToDraw = new File("osci.upload.file.path" + File.separator + fileName);
        if(!fileToDraw.exists()) {
            // Note.error(StringConfig.getValue("err.file.notFound"));
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
        return Math.abs(percentComparisonOfGraphs());
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
                File fileToDraw = new File("osci.upload.file.path" + File.separator + graph1Filename.get());
                if(!fileToDraw.exists()) {
                    return;
//                    Note.error(StringConfig.getValue("err.file.notFound"));
                }
                else drawSeries(readFromFile(Files.newInputStream(fileToDraw.toPath())), graph1Series);
            }
            if (!graph2Series.getData().isEmpty()) {
                File fileToDraw = new File("osci.upload.file.path" + File.separator + graph2Filename.get());
                if(!fileToDraw.exists()) {
                    return;
//                    Note.error(StringConfig.getValue("err.file.notFound"));
                }
                else drawSeries(readFromFile(Files.newInputStream(fileToDraw.toPath())), graph2Series);
            }
            if (!graph3Series.getData().isEmpty()) {
                File fileToDraw = new File("osci.upload.file.path" + File.separator + graph3Filename.get());
                if(!fileToDraw.exists()) {
                    return;
//                    Note.error(StringConfig.getValue("err.file.notFound"));
                }
                else drawSeries(readFromFile(Files.newInputStream(fileToDraw.toPath())), graph3Series);
            }
        }
        catch(IOException ex){
            System.out.println(StringConfig.getValue("err.file.notFound"));
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

    public Double percentComparisonOfGraphs(){
        int count = 0;
        if(StringUtils.isEmpty(graph1Filename.get())) count++;
        if(StringUtils.isEmpty(graph2Filename.get())) count++;
        if(StringUtils.isEmpty(graph3Filename.get())) count++;

        if(count >= 2){
            // Note.error(StringConfig.getValue("err.ui.load"));
            return 0.0;
        }
        File oldFileData = null;
        File newFileData = null;

        if(!graph1Filename.get().isEmpty()) oldFileData = new File("osci.upload.file.path" + File.separator + graph1Filename.get());
        if(!graph2Filename.get().isEmpty()) newFileData = new File("osci.upload.file.path" + File.separator + graph2Filename.get());

        if(oldFileData == null || newFileData == null || !oldFileData.exists() || !newFileData.exists()) {
            // Note.error(StringConfig.getValue("err.file.notFound"));
            return 0.0;
        }
        try{
            List<Double> oldData = readFromFile(Files.newInputStream(oldFileData.toPath()));
            List<Double> newData = readFromFile(Files.newInputStream(newFileData.toPath()));

            Double sumSigmas = 0.0;
            int NUMBER_POINTS = Math.min(oldData.size(), newData.size());
            for(int i = NUMBER_POINTS - MAX_WAVE_DATA; i < NUMBER_POINTS; i++){
                double sigma = oldData.get(i) == 0 ? 0 : (newData.get(i) - oldData.get(i)) / oldData.get(i);
                sumSigmas += Math.abs(sigma);
            }
            return sumSigmas / MAX_WAVE_DATA;
        }
        catch (IOException ex){
            System.out.println(StringConfig.getValue("err.calculate.absolute") + "\n " + ex);
        }
        return 0.0;
    }


    private void handleClickBrowse() {
        btnBrowseFile.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(LayoutAppStage.stage);

            if (selectedFile != null) {
                String ext = selectedFile.getName().substring(selectedFile.getName().lastIndexOf(".") + 1);
                if (ext.equals("txt") || ext.equals("xlxs") || ext.equals("xls") || ext.equals("csv")) {
                    fileOnFly.set(selectedFile);
                } else {
                    System.out.println(StringConfig.getValue("err.file.type"));
                }
            }
        });
    }

    private void handleDragFile() {
        imgDropBox.setOnDragOver(event -> {
            setCursor(Cursor.CROSSHAIR);
            Dragboard db = event.getDragboard();
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            if (event.getGestureSource() != imgDropBox && db.hasFiles()) {
                File file = db.getFiles().get(0);
                fileOnFly.set(file);

            }
        });

        imgDropBox.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                System.out.println(db.getFiles().get(0).getName());
                success = true;
            }

            event.setDropCompleted(success);
            setCursor(Cursor.DEFAULT);
            event.consume();
        });

    }
}
