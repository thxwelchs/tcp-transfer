package scheduler;

import domain.Source;
import domain.TransferableDataQueue;
import service.SourceService;

import java.util.List;

public class ProcessedScheduler implements Runnable{
    private SourceService sourceService;
    private TransferableDataQueue<Source> queue;

    public ProcessedScheduler(SourceService sourceService, TransferableDataQueue<Source> queue) {
        this.sourceService = sourceService;
        this.queue = queue;
    }

    @Override
    public void run() {
        int count = 100;
        while(!queue.isProcessedDataQueueEmpty() && count-- >= 0) {
            Source source = queue.dequeueProcessdData();
            if(source == null) continue;
            if(sourceService.findById(source.getId()) != null) continue;
            queue.done(source);
        }
    }
}
