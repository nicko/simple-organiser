package com.cremaconsulting.simpleorganiser.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {

    Task findById(long id);

    List<Task> findByStatus(String status);

}
