package hello.flyway;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;

public class App
{
    private final Flyway flyway;

    public App(String url, String user, String password) {
        this.flyway = new Flyway();
        flyway.setDataSource(url, user, password);
        flyway.setLocations("classpath:hello/flyway/migration");
        flyway.setTable("flyway_jvm_migrations");

        flyway.setInitOnMigrate(true);
    }

    public static void main( String[] args )
    {
        final App app = new App("jdbc:h2:mem:" +  App.class.getName() + ";DB_CLOSE_DELAY=-1", "sa", null);
        app.migrate();
    }

    public void migrate() {


        // Start the migration
        try {
            flyway.migrate();
        }
        catch (FlywayException ex) {
            System.err.println("Failed to migrate: " + ex.getMessage());
        }


    }
}
