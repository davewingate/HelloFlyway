package hello.flyway.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;

public class V1__Init_Metadata implements JdbcMigration {
    @Override
    public void migrate(Connection connection) throws Exception {
        // just make sure flyway meta data is established
        // ... ensures that all further migrations are gated by lock on schema table
    }
}
