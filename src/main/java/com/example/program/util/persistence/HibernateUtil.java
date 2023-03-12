package com.example.program.util.persistence;

import com.example.program.app.Launch;
import com.example.program.app.entity.DeviceEntity;
import com.example.program.app.entity.OsciDataEntity;
import com.example.program.app.entity.SettingEntity;
import com.example.program.app.entity.UserEntity;
import com.example.program.util.LogUtil;
import com.example.program.util.SQLFile;
import com.example.program.util.StringUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;


/**
 * Hibernate Utility class with a convenient method to get Session Factory object.
 */
public class HibernateUtil {
    // A SessionFactory is set up once for an application!
    private static SessionFactory sessionFactory = null;
    private static Session currentSession;
    private static Transaction currentTransaction;
    private static LogUtil log = LogUtil.getLog(HibernateUtil.class);

    /**
     * Create the SessionFactory from hibernate.cfg.xml
     *
     * @return
     */
    public static SessionFactory buildSessionFactory(String dbHost) {
        try {
            Config config = Config.getPostgresql();

            Properties properties = new Properties();
/**
             * ***************************************
             * Database connection settings
             * ***************************************
             */

            properties.put("hibernate.connection.driver_class", config.driverClass);
            properties.put("hibernate.connection.url", String.format(config.url, dbHost, Launch.properties.getStr("db-name")));
            properties.put("hibernate.connection.username", config.username);
            properties.put("hibernate.connection.password", config.password);
            //JDBC connection pool (use the built-in)
            properties.put("hibernate.connection.pool_size", 2);


/**
             * ***************************************
             * Hibernate properties
             * ***************************************
             */
            //SQL dialect
            properties.put("hibernate.dialect", config.dialect);
            //Enable Hibernate's current session context
            properties.put("hibernate.current_session_context_class", "thread");
            //Disable the second-level cache
            properties.put("hibernate.cache.provider_class", "org.hibernate.cache.internal.NoCachingRegionFactory");
            //Echo all executed SQL to stdout
            properties.put("hibernate.show_sql", true);
            //Drop and re-create the database schema on startup
//            properties.put("hibernate.hbm2ddl.auto", Launch.properties.getStr("hbm-auto"));
            properties.put("hibernate.hbm2ddl.auto", "update");
            //c3p0 settings
            properties.put("hibernate.c3p0.min_size", 5);
            properties.put("hibernate.c3p0.max_size", 20);
            properties.put("hibernate.c3p0.timeout", 1800);
            properties.put("hibernate.c3p0.max_statements", 50);

            properties.put("hibernate.dbcp.validationQuery", "SELECT 1+1");


            Configuration configuration = new Configuration();
            configuration.addProperties(properties);
            configuration.addPackage("com.example.program.app.entity");
            configuration.addAnnotatedClass(DeviceEntity.class);
            configuration.addAnnotatedClass(OsciDataEntity.class);
            configuration.addAnnotatedClass(UserEntity.class);
            configuration.addAnnotatedClass(SettingEntity.class);

            ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(properties).buildServiceRegistry();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            return sessionFactory;
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            log.print(ex.toString());

            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * get SessionFactory
     *
     * @return
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static class Config {
        String driverClass;
        String url;
        String username;
        String password;
        String dialect;

        public static Config getH2() {
            Config config = new Config();
            config.driverClass = "org.h2.Driver";
            config.url = "jdbc:h2:tcp://%s/~/%s";
            config.username = "sa";
            config.password = "";
            config.dialect = "org.hibernate.dialect.H2Dialect";
            return config;
        }

        public static Config getPostgresql() {
            String dbPort;
            if (!Launch.properties.getStr("db-port").isEmpty()) {
                dbPort = Launch.properties.getStr("db-port");
            } else {
                dbPort = "5432";
            }
            Config config = new Config();
            config.driverClass = "org.postgresql.Driver";
            config.url = "jdbc:postgresql://%s:" + dbPort + "/%s";
            config.username = "myadmin";
            config.password = "myadmin";
            config.dialect = "org.hibernate.dialect.PostgreSQLDialect";
            return config;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getUrl() {
            return url;
        }

    }

    /**
     * get Session
     *
     * @return
     */
    public static Session openSession() {
        return sessionFactory.openSession();
    }

    /**
     * Close caches and connection pools
     */
    public static void shutdown() {
        getSessionFactory().close();
    }

    public static Session openCurrentSession() {
        currentSession = sessionFactory.openSession();
        return currentSession;
    }

    public static Session openCurrentSessionwithTransaction() {
        currentSession = sessionFactory.openSession();
        currentTransaction = currentSession.beginTransaction();
        return currentSession;
    }

    public static void closeCurrentSession() {
        currentSession.close();
    }

    public static void closeCurrentSessionwithTransaction() {
        currentTransaction.commit();
        currentSession.close();
    }

    public static Session getCurrentSession() {
        return currentSession;
    }
    public static boolean isDatabaseAvailable() {
        boolean available = false;
        Session session = null;

        try {
            session = sessionFactory.openSession(); //gets a new session from the session factory
            session.createSQLQuery("SELECT 1+1").uniqueResult();
            available = true;
        } catch (Exception ex) {
            //add entry to application log
        } finally {
            if (null != session)
                session.close();
        }

        return available;
    }

    /**
     * filePath - fayldagi keys lar bilan ajratilgan query larni ishlatish!!!
     * bunda keys: -- #<KEY_NOMI> lar ro'yhati
     *
     * @param filePath
     * @param keys
     */
    public static void executeSQLQuery(String filePath, List<String> keys) {
        InputStream is = HibernateUtil.class.getClassLoader().getResourceAsStream(filePath);
        SQLFile sqlFile = new SQLFile(is);
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession(); //gets a new session from the session factory
            transaction = session.beginTransaction();
            for (String key : keys) {
                String query = sqlFile.query(key);
                if (!StringUtil.isNullEmptySpace(query))
                    session.createSQLQuery(query).executeUpdate();
            }
        } catch (Exception ex) {
            //add entry to application log
        } finally {
            if (null != session && null != transaction) {
                transaction.commit();
                session.close();
            }
        }
    }

}
