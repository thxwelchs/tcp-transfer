package scheduler;

import domain.TransferableEntity;
import io.BlockingClient;
import io.Datagram;
import io.DatagramHeader;
import service.DestinationService;
import service.SourceService;

import java.util.List;
import java.util.stream.Collectors;

public class BlockingDestinationScheduler implements Runnable {
    private DestinationService destinationService;
    private SourceService sourceService;

    public BlockingDestinationScheduler(DestinationService destinationService, SourceService sourceService) {
        this.destinationService = destinationService;
        this.sourceService = sourceService;
    }

    @Override
    public void run() {
        System.out.println("Slave Blocking Client Task Starting...");
        BlockingClient client = null;
        try {
            client = new BlockingClient("localhost", 10054);
            client.connect();
            client.sendData(new Datagram(DatagramHeader.TEN_SECONDS_REQ, ""));
            Datagram datagram = client.readData();
            List<TransferableEntity> receivedData = (List<TransferableEntity>) datagram.getPayload();
            boolean saveCompleted = destinationService.saveAll(receivedData);
            if(saveCompleted) {
                List<Integer> idList = receivedData.stream().map(TransferableEntity::getId).collect(Collectors.toList());
                sourceService.delete(idList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Slave Blocking Client Task Finish!");
            client.close();
        }
    }
}
