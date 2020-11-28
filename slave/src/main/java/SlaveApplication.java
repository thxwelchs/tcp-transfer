import scheduler.BlockingDestinationScheduler;
import scheduler.DestinationScheduler;
import service.DestinationService;
import service.SourceService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SlaveApplication {
    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(6);

        DestinationService destinationService = new DestinationService();
        SourceService sourceService = new SourceService();

        DestinationScheduler scheduler = new DestinationScheduler(destinationService, sourceService);
        BlockingDestinationScheduler blockingScheduler = new BlockingDestinationScheduler(destinationService, sourceService);

        executor.scheduleAtFixedRate(scheduler, 0, 1, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(blockingScheduler, 0, 5, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(blockingScheduler, 0, 5, TimeUnit.SECONDS);
    }
}
