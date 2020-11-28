import domain.Source;
import domain.TransferableDataQueue;
import io.BlockingServer;
import io.Server;
import scheduler.ProcessedScheduler;
import service.SourceService;

import java.util.concurrent.*;

public class MasterApplication {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

        SourceService sourceService = new SourceService();
        TransferableDataQueue<Source> queue = new TransferableDataQueue<>();

        Server server = new Server(10055, sourceService);
        BlockingServer blockingServer = new BlockingServer(10054, sourceService, queue);
        ProcessedScheduler processedScheduler = new ProcessedScheduler(sourceService, queue);

        executorService.execute(server::start);
        executorService.execute(blockingServer::start);
        scheduledExecutorService.scheduleAtFixedRate(processedScheduler, 0, 5, TimeUnit.SECONDS);
    }
}
