package com.example.program.app;

import com.example.program.app.entity.OsciUserEntity;
import com.example.program.app.property.SettingProperty;
import com.example.program.app.service.SettingService;
import com.example.program.app.service.UserService;
import com.example.program.common.status.EntityStatus;
import com.example.program.util.Encryption;
import com.example.program.util.LogUtil;
import com.example.program.util.SQLFile;
import com.example.program.util.StringConfig;
import com.example.program.util.persistence.HibernateUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

/**
 * This class displays a Authentication using a wizard
 */


/**
 * basic wizard infrastructure class
 */
class Wizard extends StackPane {
    private static final int UNDEFINED = -1;
    private ObservableList<WizardPage> pages = FXCollections.observableArrayList();

    Wizard(WizardPage... nodes) {
        pages.addAll(nodes);
//        navTo(0);//todo:txt
        setStyle("-fx-padding: 10; -fx-background-color: cornsilk;");
    }

    void navTo(int nextPageIdx) {
        if (nextPageIdx < 0 || nextPageIdx >= pages.size()) return;

        WizardPage nextPage = pages.get(nextPageIdx);
        getChildren().clear();
        getChildren().add(nextPage);
        nextPage.manageButtons();
    }

    void navTo(String id) {
        Node page = lookup("#" + id);
        if (page != null) {
            int nextPageIdx = pages.indexOf(page);
            if (nextPageIdx != UNDEFINED) {
                navTo(nextPageIdx);
            }
        }
    }

    public void finish() {
    }

    public void cancel() {
    }
}

/**
 * basic wizard page class
 */
abstract class WizardPage extends VBox {
    Button databaseConnectionButton = new Button("Connect");
    Button authenticationButton = new Button("Send");
    Button cancelButton = new Button("Cancel");

    WizardPage(String title, boolean showDatabaseConnection, boolean showAuthentication) {
        VBox topBlock = VBoxBuilder.create().alignment(Pos.TOP_CENTER).build();
        Image image = new Image("/img/terminal_logo.png");
        topBlock.getChildren().add(ImageViewBuilder.create().image(image).fitWidth(image.getWidth() / 2).fitHeight(image.getHeight() / 2).build());
        topBlock.getChildren().add(LabelBuilder.create().text(title).style("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 0 0 5 0;").build());
        Label lblError = LabelBuilder.create().build();
        lblError.textProperty().bind(AuthenticationData.instance.errors);
        topBlock.getChildren().add(lblError);
        getChildren().add(topBlock);

        setId(title);
        setSpacing(5);
        setStyle("-fx-padding:10; -fx-background-color: honeydew; -fx-border-color: derive(honeydew, -30%); -fx-border-width: 3;");

        Region spring = new Region();
        VBox.setVgrow(spring, Priority.ALWAYS);
        getChildren().addAll(getContent(), spring, getButtons(showDatabaseConnection, showAuthentication));

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                getWizard().cancel();
            }
        });
    }

    HBox getButtons(boolean showDatabaseConnection, boolean showAuthentication) {
        Region spring = new Region();
        HBox.setHgrow(spring, Priority.ALWAYS);
        HBox buttonBar = new HBox(5);
        cancelButton.setCancelButton(true);
        databaseConnectionButton.setVisible(showDatabaseConnection);
        authenticationButton.setVisible(showAuthentication);
        buttonBar.getChildren().addAll(spring, new StackPane(databaseConnectionButton, authenticationButton), cancelButton);
        return buttonBar;
    }

    abstract Parent getContent();

    void navTo(int nextPageIdx) {
        getWizard().navTo(nextPageIdx);
    }

    void navTo(String id) {
        getWizard().navTo(id);
    }

    Wizard getWizard() {
        return (Wizard) getParent();
    }

    public void manageButtons() {
//        if (!hasPriorPage()) {
//            priorButton.setDisable(true);
//        }
//
//        if (!hasNextPage()) {
//            nextButton.setDisable(true);
//        }
    }
}

/**
 * This class shows a AuthenticationWizard
 */
class AuthenticationWizard extends Wizard {
    Stage owner;

    public AuthenticationWizard(Stage owner, int pageIndex) {
        super(new DatabaseConnectionPage());
        this.owner = owner;
        owner.initStyle(StageStyle.UNDECORATED);

        if (pageIndex >= 0)
            navTo(pageIndex);
    }

    public void finish() {
//        System.out.println("Had error? " + AuthenticationData.instance.hasErrors.get());
        if (AuthenticationData.instance.hasErrors.get()) {
//            System.out.println("Errors: " + (AuthenticationData.instance.errors.get().isEmpty() ? "No Details" : "\n" + AuthenticationData.instance.errors.get()));
        }
        owner.close();
    }

    public void cancel() {
//        System.out.println("Cancelled");
        owner.close();
    }
}

/**
 * dealer authentication
 */
class AuthenticationData {
    BooleanProperty hasErrors = new SimpleBooleanProperty();
    StringProperty errors = new SimpleStringProperty();
    static AuthenticationData instance = new AuthenticationData();

    public void setErrors(String errors) {
        this.errors.set(errors);
    }

    public void log(Class<?> clazz, String logStr) {
        LogUtil.getLog(clazz).print(logStr);
        setErrors(logStr);
    }
}

class AuthenticationUtil {
    private static SettingService settingsService = new SettingService();

    private static UserService userService = new UserService();

    public static boolean isDatabaseAvailable(String ipAddress) {
        //  1-STEP
//        AuthenticationData.instance.log(AuthenticationUtil.class, StringConfig.getValue("auth.db.checking"));
        HibernateUtil.buildSessionFactory(ipAddress);
        if (!HibernateUtil.isDatabaseAvailable()) {
//            AuthenticationData.instance.log(AuthenticationUtil.class, StringConfig.getValue("auth.db.failed"));
            return false;
        }
//        AuthenticationData.instance.log(AuthenticationUtil.class, StringConfig.getValue("auth.db.succ"));

        //  2-STEP
        if (!Launch.properties.getStr("db-host").equals(ipAddress)) {
//            AuthenticationData.instance.log(AuthenticationUtil.class, StringConfig.getValue("auth.db.saveIP"));
            Launch.properties.add("db-host", ipAddress);
            File userFile = new File(System.getProperty("user.dir") + "/app.properties");
            Launch.properties.save(userFile);
        }
        return true;
    }

    public static void createDefaultFunction(SQLFile sqlFile){

        String ostatok = sqlFile.query("getProductOstatok");
        String rate = sqlFile.query("getProductRate");

        String jdbcUrl = String.format("jdbc:postgresql://%s:5432/%s", Launch.properties.getStr("db-host"), Launch.properties.getStr("db-name"));
        String username = "myadmin";
        String password = "myadmin";

        Connection conn = null;

        try {
            // Step 1 - Load driver
             Class.forName("org.postgresql.Driver"); // Class.forName() is not needed since JDBC 4.0

            // Step 2 - Open connection
            conn = DriverManager.getConnection(jdbcUrl, username, password);

            conn.createStatement().execute(ostatok);
            conn.createStatement().execute(rate);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {

                // Step 5 Close connection
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void start() {
        initDefaultUser();
        initDefaultSettings();

        new Login().start(new Stage());
        Launch.stage.close();
    }

    private static void initDefaultSettings() {
        SettingProperty setting = settingsService.getApplicationSetting();
        if (setting == null){
            setting = new SettingProperty();
            setting.setCreatedDate(new Date());
            setting.setTechSupport("csharpoxun@gmail.com");
            setting.setAppName("0SC1LL0SC0PE!");
            setting.setVersion("OSCILLOSCOPE_1.0");
            setting.setAuthorContact("+998 91 407-34-38");
            setting.setAuthorName("https://t.me/programmer_anarchy");
            settingsService.insert(setting);
        }
    }

    private static void initDefaultUser() {
        String login = "admin";
        OsciUserEntity adminUser = userService.getByLogin(login);
        if (adminUser == null) {
            adminUser = new OsciUserEntity();
            adminUser.setFirstName(StringConfig.getValue("user.firstName"));
            adminUser.setLastName(StringConfig.getValue("user.middleName"));
            adminUser.setMiddleName(StringConfig.getValue("user.lastName"));
            adminUser.setPrintableName(StringConfig.getValue("user.printableName"));
            adminUser.setLogin(login);
            adminUser.setPass(Encryption.convert("1"));
            adminUser.setPin(Encryption.convert("0000"));
            adminUser.setInfo("---");
            adminUser.setStatus(EntityStatus.ACTIVE);

            userService.insert(adminUser);
        }
    }

    public static OsciUserEntity getAdminUser(){
        return userService.getUser();
    }
}



class DatabaseConnectionPage extends WizardPage {
    public DatabaseConnectionPage() {
        super(StringConfig.getValue("auth.db.form.header"), true, false);
    }

    Parent getContent() {
        TextField txtIPAddress = TextFieldBuilder.create().text(Launch.properties.getStr("db-host")).build();

        databaseConnectionButton
                .disableProperty()
                .bind(Bindings.createBooleanBinding(() ->
                        txtIPAddress.getText().trim().isEmpty(), txtIPAddress.textProperty()));
        databaseConnectionButton.setOnAction(event -> {
            String ipAddress = txtIPAddress.getText().trim();

            //  1-STEP
            if (!AuthenticationUtil.isDatabaseAvailable(ipAddress)) return;
            //  2-STEP
            OsciUserEntity adminUser = AuthenticationUtil.getAdminUser();
            if (adminUser == null) {
                AuthenticationData.instance.log(this.getClass(), StringConfig.getValue("auth.user.noExistInLocale"));
                navTo(1);
                return;
            }

            //  4-STEP
            AuthenticationData.instance.log(this.getClass(), StringConfig.getValue("auth.terminal.activeInServerAndLocale"));
            AuthenticationUtil.start();
        });

        return VBoxBuilder.create().spacing(5).children(
                new Label(StringConfig.getValue("auth.db.form.hostName")),
                txtIPAddress
        ).build();
    }
}