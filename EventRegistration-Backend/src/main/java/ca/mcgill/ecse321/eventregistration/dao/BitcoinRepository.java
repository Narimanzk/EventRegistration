package ca.mcgill.ecse321.eventregistration.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.eventregistration.model.Bitcoin;

public interface BitcoinRepository extends CrudRepository<Bitcoin, String> {
	
	Bitcoin findByUserID(String userID);

}
