package org.quizbe.dao;

import org.quizbe.model.Classroom;
import org.quizbe.model.Scope;
import org.quizbe.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScopeRepository extends CrudRepository<Scope, Long> {

    List<Scope> findByClassroom(Classroom classroom);
    Scope findById(long id);

}
