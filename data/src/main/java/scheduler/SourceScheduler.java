package scheduler;

import service.SourceService;

public class SourceScheduler implements Runnable {

    private SourceService sourceService;

    public SourceScheduler(SourceService sourceService) {
        this.sourceService = sourceService;
    }

    @Override
    public void run() {
        sourceService.save();
    }
}
