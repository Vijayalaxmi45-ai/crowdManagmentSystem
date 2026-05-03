package com.crowd.mapper.service;

import com.crowd.mapper.model.CrowdLocation;
import com.crowd.mapper.repository.CrowdLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CrowdService {

    @Autowired
    private CrowdLocationRepository repository;

    public List<CrowdLocation> getAllLocations() {
        return repository.findAll();
    }

    public void saveLocation(CrowdLocation location) {
        repository.save(location);
    }
}
