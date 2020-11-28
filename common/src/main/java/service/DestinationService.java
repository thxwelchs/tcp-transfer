package service;

import config.MySQLConnector;
import domain.Destination;
import domain.Source;
import domain.TransferableEntity;
import repository.DestinationRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DestinationService {
    private static DestinationRepository destinationRepository;

    public DestinationService() {
        if(destinationRepository == null) {
            destinationRepository = new DestinationRepository(new MySQLConnector(), Destination.class);
        }
    }

    public List<Destination> findByCreatedTime(LocalDateTime createdTime) {
        List<Destination> destinationList = new ArrayList<>();
        try {
            destinationList = destinationRepository.findByCreatedTime(createdTime.withNano(0));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return destinationList;
    }

    public boolean saveAll(List<? extends TransferableEntity> sourceList) {
        if(sourceList == null || sourceList.size() == 0) return false;
        return destinationRepository.saveAll(sourceList);
    }
}
