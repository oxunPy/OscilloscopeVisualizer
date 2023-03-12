package com.example.program.app;

import com.example.program.util.StringConfig;
import com.example.program.util.StringUtil;
import com.example.program.util.TypedProperties;
import com.example.program.util.persistence.HibernateUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.io.IOUtils;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Objects;
import java.util.Properties;


public class Launch extends Application {
    public static final Logger log = LoggerFactory.getLogger(Launch.class);

    public static TypedProperties properties;
    public static Stage stage;
    public static Stage launchStage;
    public static Scene scene;
    private AnchorPane page;

    private Screen screen = Screen.getPrimary();
    private Rectangle2D windows = screen.getVisualBounds();

    public static void main(String[] args) {
        if (args == null || args.length < 1) {
            Application.launch(args);
        } else {
            Application.launch(args);
        }

        System.exit(0);
    }

    public static void exit() {
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void init() throws Exception {
    }

    @Override
    public void start(final Stage stage) throws Exception {

        initProperties();

        Launch.stage = stage;
        stage.getIcons().addAll(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/img/oscilloscope/icon.png"))));
        show(new Stage());
    }

    private void show(final Stage stage) {
        try {
            launchStage = stage;
            page = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("/view/launch.fxml")), StringConfig.getPropertiesFromResource());
            scene = new Scene(page);

            stage.initStyle(StageStyle.UNDECORATED);
            stage.setWidth(500);
            stage.setHeight(340);
            stage.setResizable(false);

            stage.getIcons().addAll(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/img/oscilloscope/icon.png"))));

            stage.setScene(scene);
            stage.show();
            flyWayMigrationSqlFiles();
        } catch (Exception ex) {
            System.out.println("Login Initialization Error!" + ex);
        }
    }

    private File configProperties(String pathname, String... keys) {
        File userFile = new File(pathname + "/app.properties");

        // Load all print properties
        if (userFile.isFile())
            properties = TypedProperties.fromFile(userFile);
        else
            properties = new TypedProperties();

        // Add all absent properties
        TypedProperties applicationProperties = TypedProperties.fromResource("application.properties");
        applicationProperties.getProperties()
                .stream()
                .filter(property -> StringUtil.hasKeys(keys, property) || !properties.has(property))
                .forEach(property -> properties.add(property, applicationProperties.getStr(property)));

        // Export all loaded properties
        properties.save(userFile);

        // Copy default properties as sample
        try {
            IOUtils.copy(
                    this.getClass().getResourceAsStream("/application.properties"),
                    new FileOutputStream(pathname + "/application.properties")
            );
        } catch (IOException e) {
            log.info("Unable to copy default.properties");
        }

        return userFile;
    }

    private void initProperties() throws IOException {
        String isFirst = getParameters().getRaw().stream().filter(s -> s.equals("FROM_UPDATE")).findAny().orElse(null);
        File config;
        if (StringUtil.notEmpty(isFirst)) {
            config = configProperties(System.getProperty("user.dir"), "server-port");
        } else {
            config = configProperties(System.getProperty("user.dir"));
        }

        logProperty("Java Version", System.getProperty("java.version"));
        logProperty("IS FIRST", getParameters().getNamed().get(""));
        logProperty("JavaFX Version", com.sun.javafx.runtime.VersionInfo.getRuntimeVersion());
        logProperty("Working Directory", System.getProperty("user.dir"));
        logProperty("Config File", config.getCanonicalPath());

//        System.out.println("\n****************************** START(config properties)\n");
        for (String property : properties.getProperties())
            logProperty(property, properties.getStr(property));
//        System.out.println("\n****************************** END(config properties)\n");
    }

    private void logProperty(String key, String val) {
        if (key.length() > 30)
            key = key.substring(0, 27) + "...";
        System.out.println(String.format("%-30s%s", key, val));
    }



    private void flyWayMigrationSqlFiles() {
        HibernateUtil.Config config = HibernateUtil.Config.getPostgresql();
        Properties prop = new Properties();

        String isFirst = getParameters().getRaw().stream().filter(s -> s.equals("FROM_UPDATE")).findAny().orElse(null);
        File file;
        if (StringUtil.notEmpty(isFirst)) {
            file = configProperties(System.getProperty("user.dir"), "server-port");
        } else {
            file = configProperties(System.getProperty("user.dir"));
        }

        try {
            InputStream input = new FileInputStream(file);
            prop.load(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        Flyway flyway = new Flyway();
        flyway.setDataSource(String.format(config.getUrl()
                        , prop.getProperty("db-host")
                        , prop.getProperty("db-name"))
                , config.getUsername(), config.getPassword());
        flyway.setLocations("db/migration");
        flyway.setSqlMigrationPrefix("T");
        flyway.setBaselineOnMigrate(true);
        flyway.setBaselineVersion(MigrationVersion.fromVersion("0"));
        flyway.migrate();
    }

}
