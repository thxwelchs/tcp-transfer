package service;

import config.MySQLConnector;
import domain.Source;
import repository.SourceRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SourceService implements RandomableNumberGenerator<Integer> {
    private static SourceRepository sourceRepository;

    public SourceService() {
        if(sourceRepository == null) {
            sourceRepository = new SourceRepository(new MySQLConnector(), Source.class);
        }
    }

    public void delete(List<Integer> ids) {
        sourceRepository.delete(ids);
    }

    public List<Source> findByCreatedTimeLessThan(LocalDateTime createdTime) {
        List<Source> sourceList = new ArrayList<>();
        try {
            sourceList = sourceRepository.findByCreatedTimeLessThan(createdTime.withNano(0));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sourceList;
    }

    public Source findById(int id) {
        return sourceRepository.findById(id);
    }

    public List<Source> findByCreatedTime(LocalDateTime createdTime) {
        List<Source> sourceList = new ArrayList<>();
        try {
            sourceList = sourceRepository.findByCreatedTime(createdTime.withNano(0));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sourceList;
    }

    public void save() {
        sourceRepository.save(generateRandomNumber());
    }

    @Override
    public Integer generateRandomNumber() {
        return new Random().nextInt();
    }
}
