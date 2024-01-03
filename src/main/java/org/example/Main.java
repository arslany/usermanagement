package org.example;

import com.gitlab.mvysny.jdbiorm.JdbiOrm;
import io.javalin.Javalin;
import io.javalin.event.EventListener;
import org.example.database.DatabaseUtil;
import org.example.controller.UserController;
import org.example.exception.CustomException;
import org.example.exception.CustomExceptionHandler;
import org.example.utils.PropertyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.javalin.apibuilder.ApiBuilder.crud;
import static io.javalin.apibuilder.ApiBuilder.get;
import static java.util.Calendar.PM;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final PropertyManager PM = PropertyManager.getInstance();

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
            UserController userController = new  UserController();
            crud("/hello/{hello-id}", userController);
            get("/user/email/{email}", ctx -> {
                userController.findUserByEmail(ctx, ctx.pathParam("email"));
            });
            crud("/user/{user-id}", userController);
            //Use get method for the root path
            get("/", ctx -> { ctx.result("up and running");
            });
        });

        app.exception(CustomException.class, new CustomExceptionHandler());
        //Start the app on the specified port
        app.start(PM.getPropertyAsInt("REST_PORT"));
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
        DatabaseUtil.configureJdbiOrm();

        //run updated script
        DatabaseUtil.updateDatabase();
    }
}