package org.example;

import com.gitlab.mvysny.jdbiorm.JdbiOrm;
import io.javalin.Javalin;
import io.javalin.event.EventListener;
import org.example.database.DatabaseUtil;
import org.example.database.controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.javalin.apibuilder.ApiBuilder.crud;
import static io.javalin.apibuilder.ApiBuilder.get;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("Starting application");

        //Create a Javalin instance with some configuration options
        Javalin app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
            config.http.generateEtags = true;
            config.http.asyncTimeout = 10000L;
        });

        app.routes(() -> {
            //Use crud methods for hello and user resources
            crud("/hello/{hello-id}", new HelloWorldController());
            crud("/user/{user-id}", new UserController());
            //Use get method for the root path
            get("/", ctx -> { ctx.result("up and running");
            });
        });

        //Start the app on the specified port
        app.start(7000);
        Runtime.getRuntime().addShutdownHook(new Thread(app::stop));
        app.events(Main::accept);
        connectToDatabase();

    }

    /**
     * Event listeners for server
     *
     * @param event
     */

    private static void accept(EventListener event) {
        event.serverStarting(() -> {
            log.info("Server Starting");
        });
        event.serverStarted(() -> {
            log.info("Server Started");
        });
        event.serverStartFailed(() -> {
            log.info("Server start failed");
        });
        event.serverStopping(() -> {
            log.info("Server Stopping");
            JdbiOrm.destroy();
        });
        event.serverStopped(() -> {
            log.info("Server Stopped");
        });
    }

    /**
     * Connect to H2 database. It will not create a file in memory but on user's desktop
     */
    private static void connectToDatabase() {
        log.info("Connecting to database");
        String jdbcUrl = "jdbc:h2:file:~/Desktop/userDatabase;DB_CLOSE_DELAY=-1";
        String jdbcUsername = "sa";
        String jdbcPassword = "";
        String driver = "org.h2.Driver";
        //Connect to database
        DatabaseUtil.configureJdbiOrm(jdbcUrl, jdbcUsername, jdbcPassword, driver);

        //run updated script
        DatabaseUtil.updateDatabase();
    }
}