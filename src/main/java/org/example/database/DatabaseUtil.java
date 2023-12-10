package org.example.database;

import com.gitlab.mvysny.jdbiorm.JdbiOrm;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.gitlab.mvysny.jdbiorm.JdbiOrm.jdbi;

/**
 * Utility class implementing support for most popular databases.
 * @author mavi
 */
public class DatabaseUtil {
    private static final Logger log = LoggerFactory.getLogger(DatabaseUtil.class);
    public static void ddl(@Language("sql") @NotNull String sql) {
        jdbi().withHandle(handle -> handle
                .createUpdate(sql)
                .execute());
    }

    public static void configureJdbiOrm(@NotNull String jdbcUrl, @NotNull String username, @NotNull String password, @NotNull String driver) {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setMinimumIdle(0);
        hikariConfig.setDriverClassName(driver);
        log.info("Connecting to " + jdbcUrl);
        JdbiOrm.setDataSource(new HikariDataSource(hikariConfig));
    }

    /**
     * This method will execute the newly added database scripts on startup.
     */
    public static void updateDatabase(){
        log.info("Migrating database to newest version");
        // see https://flywaydb.org/ for more information. In short, Flyway will
        // apply scripts from src/main/resources/database/h2/, but only those that
        // haven't been applied yet.
        final Flyway flyway = Flyway.configure()
                .dataSource(JdbiOrm.getDataSource())
                .locations("database/h2")
                .load();
        //flyway.repair();
        flyway.migrate();

        log.info("Database updated");
    }


    /**
     * Starts an in-memory H2 database. After the main() method finishes, the database is gone, along with its contents.
     */
    public static void h2(@NotNull Runnable block) {
        configureJdbiOrm("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "","org.h2.Driver");
        try {
            ddl("create table if not exists Person (\n" +
                    "                id bigint primary key auto_increment,\n" +
                    "                name varchar not null,\n" +
                    "                age integer not null,\n" +
                    "                dateOfBirth date,\n" +
                    "                created timestamp,\n" +
                    "                modified timestamp,\n" +
                    "                alive boolean,\n" +
                    "                maritalStatus varchar" +
                    ")");
            block.run();
        } finally {
            JdbiOrm.destroy();
        }
    }

}
