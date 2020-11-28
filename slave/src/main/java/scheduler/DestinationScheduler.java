package scheduler;

import domain.TransferableEntity;
import io.Client;
import io.Datagram;
import io.DatagramHeader;
import service.DestinationService;
import service.SourceService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DestinationScheduler implements Runnable {
    private DestinationService destinationService;
    private SourceService sourceService;

    public DestinationScheduler (DestinationService destinationService, SourceService sourceService) {
        this.destinationService = destinationService;
        this.sourceService = sourceService;
    }

    @Override
    public void run() {
        System.out.println("Slave Client Task Starting...");
        Client client = null;
        try {
            client = new Client(1024);
            client.connect();
            client.sendData(new Datagram(DatagramHeader.ONE_SECONDS_REQ, ""));
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
            System.out.println("Slave Client Task Finish!");
            try {
                if(client != null)
                    client.stop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
