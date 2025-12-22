package vn.bluemoon.service;

import vn.bluemoon.exception.DbException;
import vn.bluemoon.model.entity.Resident;
import vn.bluemoon.repository.ResidentRepository;

import java.util.List;

/**
 * Service for Resident management
 */
public class ResidentService {
    private final ResidentRepository residentRepository = new ResidentRepository();
    
    /**
     * Get all residents
     */
    public List<Resident> getAllResidents() throws DbException {
        return residentRepository.findAll();
    }
    
    /**
     * Search residents
     */
    public List<Resident> searchResidents(String name, String apartmentCode, String householdCode) throws DbException {
        return residentRepository.search(name, apartmentCode, householdCode);
    }
    
    /**
     * Get resident by ID
     */
    public Resident getResidentById(Integer id) throws DbException {
        return residentRepository.findById(id);
    }
}


