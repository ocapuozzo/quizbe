package org.quizbe.dao;

import org.quizbe.model.Classroom;
import org.quizbe.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClassroomRepository extends CrudRepository<Classroom, Long> {

    List<Classroom> findByName(String nName);

    //Classroom findById(long id);

    List<Classroom> findByTeacher(User user);
}
