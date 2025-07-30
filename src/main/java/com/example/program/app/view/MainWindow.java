package com.example.program.app.view;

import com.example.program.app.property.OsciFileProperty;
import com.example.program.app.stages.LayoutAppStage;
import com.example.program.util.*;
import com.example.program.util.widget.hotkey.HotKeyListener;
import com.example.program.util.widget.hotkey.HotKeyManager;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Path;
import javafx.stage.FileChooser;
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

    // File table items
    @FXML
    private TableView<OsciFileProperty> tbDataFiles;
    @FXML
    private TableColumn<OsciFileProperty, String> colFileName;
    @FXML
    private TableColumn<OsciFileProperty, Void> colShowGraphic;
    @FXML
    private TableColumn<OsciFileProperty, Void> colPlayPause;

    // Graph items
    @FXML
    private Button btnPrevChart;
    @FXML
    private Button btnNextChart;
    @FXML
    private LineChart<Number, Number> lchGraphs;

    // Footer items
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
    private Label lbGraph1;
    @FXML
    private Label lbGraph2;
    @FXML
    private Label lbGraph3;

    // Drag-Drop/Browse items
    @FXML
    private ImageView imgDropBox;
    @FXML
    private Button btnBrowseFile;

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
            loadView();
            onCreate();
            onStart();
            FileHandler fileHandler = new FileHandler();
            fileHandler.setup();
        } catch (IOException ex) {
            System.out.println(StringConfig.getValue("err.ui.load") + "\n" + ex);
        }
    }

    private void loadView() throws IOException {
        FXMLLoader fxml = new FXMLLoader(getClass().getResource("/view/main.fxml"), StringConfig.getPropertiesFromResource());

        fxml.setRoot(this);
        fxml.setController(this);
        fxml.load();
    }

    public void onCreate() {
        tableConfig();
        prevLineChartPane.setCenter(GlyphsDude.createIcon(MaterialDesignIcon.ARROW_LEFT, "20px"));
        nextLineChartPane.setCenter(GlyphsDude.createIcon(MaterialDesignIcon.ARROW_RIGHT, "20px"));

        // init base defaults
        toggleCheckboxes();
        baseLineChart();
        lbGraph1.textProperty().bind(graph1Filename);
        lbGraph2.textProperty().bind(graph2Filename);
        lbGraph3.textProperty().bind(graph3Filename);
    }

    public void onStart() {
        // zooming line chart
        zoomLineChart();

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

    private void drawLineChart(String absolutePath) {
        if(!(chbGraph1.isSelected() || chbGraph2.isSelected() || chbGraph3.isSelected())){
            return ;
        }

        if(tbDataFiles.getSelectionModel().getSelectedItem() == null){
            return ;
        }

        File fileToDraw = new File(absolutePath);
        if(!fileToDraw.exists()) {
            return;
        }

        XYChart.Series<Number, Number> linechart = null;
        if(chbGraph1.isSelected()) {
            linechart = graph1Series;
            graph1Filename.setValue(absolutePath);
        }
        else if(chbGraph2.isSelected()){
            linechart = graph2Series;
            graph2Filename.setValue(absolutePath);
        }
        else {
            linechart = graph3Series;
            graph3Filename.setValue(absolutePath);
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

    public void drawSeries(List<Double> data, XYChart.Series<Number, Number> lineChart){
        lineChart.getData().clear();
        for(int i = currentPage.get() * MAX_UNIT_SIZE; (i < currentPage.get() * MAX_UNIT_SIZE + MAX_UNIT_SIZE && i < data.size()) ; i++) {
            lineChart.getData().add(new XYChart.Data<>(i, data.get(i)));
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

    private void nextLineChart(){
        currentPage.setValue(currentPage.get() + 1);
        updateGraphics();
    }

    private void prevLineChart(){
        if(currentPage.get() > 0) currentPage.setValue(currentPage.get() - 1);
        updateGraphics();
    }

    private void updateGraphics(){
        try {
            if (!graph1Series.getData().isEmpty()) {
                File fileToDraw = new File(graph1Filename.get());
                if(!fileToDraw.exists()) {
                    return;
                }
                else drawSeries(readFromFile(Files.newInputStream(fileToDraw.toPath())), graph1Series);
            }
            if (!graph2Series.getData().isEmpty()) {
                File fileToDraw = new File(graph2Filename.get());
                if(!fileToDraw.exists()) {
                    return;
//                    Note.error(StringConfig.getValue("err.file.notFound"));
                }
                else drawSeries(readFromFile(Files.newInputStream(fileToDraw.toPath())), graph2Series);
            }
            if (!graph3Series.getData().isEmpty()) {
                File fileToDraw = new File(graph3Filename.get());
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
        ChartPanManager panner = new ChartPanManager(lchGraphs);
        panner.setMouseFilter(mouseEvent -> {
            if(mouseEvent.getButton() != MouseButton.PRIMARY){
                mouseEvent.consume();
            }
        });
        panner.start();

        JFXChartUtil.setupZooming(lchGraphs, mouseEvent -> {
            if(mouseEvent.getButton() != MouseButton.SECONDARY){
                mouseEvent.consume();
            }
        });

    }

    private void tableConfig() {
        tbDataFiles.setItems(listData);
        tbDataFiles.setEditable(true);

        colFileName.setCellValueFactory(new PropertyValueFactory<>("filename"));
        colShowGraphic.setCellFactory(col -> new TableCell<OsciFileProperty, Void>() {
            final Button btnDrawChart = new Button(StringConfig.getValue("label.draw.graph"));
            {
                btnDrawChart.setOnMouseClicked(event -> {
                    OsciFileProperty property = getTableView().getItems().get(getIndex());
                    drawLineChart(property.getFilename());
                });
                btnDrawChart.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.LINE_CHART, "22px"));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnDrawChart);
                }
            }
        });
    }

    class FileHandler {
        private final ObjectProperty<File> fileOnFly = new SimpleObjectProperty<>();

        void setup() {
            handleDragFile();
            handleClickBrowse();
            handleFileExistCallback();
        }

        private void handleFileExistCallback() {
            fileOnFly.addListener((observable, oldValue, newValue) -> {
                if(newValue != null) {
                    OsciFileProperty property = new OsciFileProperty();
                    property.setFilename(newValue.getAbsolutePath());
                    listData.add(property);
                }
            });
        }

        private void handleClickBrowse() {
            btnBrowseFile.setOnMouseClicked(event -> {
                FileChooser fileChooser = new FileChooser();
                File selectedFile = fileChooser.showOpenDialog(LayoutAppStage.stage);

                if (selectedFile != null) {
                    String ext = selectedFile.getName().substring(selectedFile.getName().lastIndexOf(".") + 1);
                    if (ext.equals("txt") || ext.equals("xlxs") || ext.equals("xls") || ext.equals("csv")) {
                        fileOnFly.set(selectedFile);
                    }
                }
            });
        }

        private void handleDragFile() {
            dragAction();
            dropAction();
        }

        private void dragAction() {
            imgDropBox.setOnDragOver(event -> {
                setCursor(Cursor.CROSSHAIR);
                Dragboard db = event.getDragboard();
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                if (event.getGestureSource() != imgDropBox && db.hasFiles()) {
                    File file = db.getFiles().get(0);
                    fileOnFly.set(file);

                }
            });
        }

        private void dropAction() {
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
}
