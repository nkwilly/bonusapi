package com.systemedebons.bonification.Service;

import com.systemedebons.bonification.Entity.Administrator;
import com.systemedebons.bonification.Repository.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AdminService {


    @Autowired
    private AdministratorRepository administratorRepository;

    public Administrator createAdministrator(Administrator administrator) {
        return  administratorRepository.save(administrator);
    }


    public Administrator login(String username, String password) {
        Administrator administrator = administratorRepository.findByUsername(username);
        if (administrator != null && administrator.getPassword().equals(password)) {

            return administrator;
        }else {
            return null;
        }
    }

    public List<Administrator> getAdmins() {
        return administratorRepository.findAll();
    }

}
