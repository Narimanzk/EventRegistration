package ca.mcgill.ecse321.eventregistration.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.eventregistration.model.CarShow;

public interface CarShowRepository extends CrudRepository<CarShow, String> {

	CarShow findByName(String name);
}
