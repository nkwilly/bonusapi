package com.systemedebons.bonification.Repository;

import com.systemedebons.bonification.Entity.Administrator;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministratorRepository extends MongoRepository<Administrator, String> {

            Administrator findByUsername(String Username);

}
