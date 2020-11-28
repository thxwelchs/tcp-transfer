import domain.HashLinkedBlockingQueue;
import domain.Source;
import scheduler.SourceScheduler;
import service.SourceService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DataApplication {
    public static void main(String[] args) throws IOException {
        SourceScheduler sourceScheduler = new SourceScheduler(new SourceService());

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        System.out.println("Data Generator Start!");
        executor.scheduleAtFixedRate(sourceScheduler, 0, 100, TimeUnit.MILLISECONDS);
    }
}
