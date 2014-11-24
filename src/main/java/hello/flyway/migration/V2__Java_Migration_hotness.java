package hello.flyway.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class V2__Java_Migration_hotness implements JdbcMigration {

    private static final AtomicInteger migrationCount = new AtomicInteger(0);

    @Override
    public void migrate(Connection connection) throws Exception {
        System.out.println("Executing " + this.getClass().getSimpleName() + ": " + migrationCount.incrementAndGet());
        TimeUnit.MILLISECONDS.sleep(250);
    }

    public static int getMigrationCount() {
        return migrationCount.get();
    }
}
