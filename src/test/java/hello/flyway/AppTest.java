package hello.flyway;

import hello.flyway.migration.V2__Java_Migration_hotness;
import hello.flyway.migration.V3__More_Java_Migration_hotness;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class AppTest {

    @Test
    public void testMigrate() {
        final App app = newApp();

        final int v2before = V2__Java_Migration_hotness.getMigrationCount();
        final int v3before = V3__More_Java_Migration_hotness.getMigrationCount();

        app.migrate();

        final int v2after = V2__Java_Migration_hotness.getMigrationCount();
        final int v3after = V3__More_Java_Migration_hotness.getMigrationCount();

        assertEquals(1, v2after - v2before);
        assertEquals(1, v3after - v3before);
    }

    @Test
    public void testMigrate_Multiple_Serial() {
        final App app = newApp();

        final int v2before = V2__Java_Migration_hotness.getMigrationCount();
        final int v3before = V3__More_Java_Migration_hotness.getMigrationCount();

        app.migrate();
        app.migrate();
        app.migrate();

        final int v2after = V2__Java_Migration_hotness.getMigrationCount();
        final int v3after = V3__More_Java_Migration_hotness.getMigrationCount();

        assertEquals(1 /* should still be just one */, v2after - v2before);
        assertEquals(1 /* should still be just one */, v3after - v3before);
    }

    @Test
    public void testMigrate_Multiple_Concurrent() throws InterruptedException {
        final App app = newApp();

        final int v2before = V2__Java_Migration_hotness.getMigrationCount();
        final int v3before = V3__More_Java_Migration_hotness.getMigrationCount();

        final int numThreads = 25;
        final ExecutorService exeSvc = Executors.newFixedThreadPool(numThreads);
        try {
            for (int i = 0; i < numThreads; i++) {
                exeSvc.submit(new Runnable() {
                    @Override
                    public void run() {
                        app.migrate();
                    }
                });
            }
        } finally {
            exeSvc.shutdown();
            exeSvc.awaitTermination(5, TimeUnit.SECONDS);
        }

        final int v2after = V2__Java_Migration_hotness.getMigrationCount();
        final int v3after = V3__More_Java_Migration_hotness.getMigrationCount();

        assertEquals(1 /* should still be just one */, v2after - v2before);
        assertEquals(1 /* should still be just one */, v3after - v3before);
    }

    private App newApp() {
        final String url = String.format("jdbc:h2:mem:%s.%s;DB_CLOSE_DELAY=-1",
                                         App.class.getName(),
                                         UUID.randomUUID().toString());
        return new App(url, "sa", null);
    }
}
