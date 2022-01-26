package org.quizbe.service;

import com.google.common.collect.Lists;
import org.quizbe.controller.AdminController;
import org.quizbe.dao.ClassroomRepository;
import org.quizbe.dao.ScopeRepository;
import org.quizbe.dao.UserRepository;
import org.quizbe.dto.ClassroomDto;
import org.quizbe.exception.UserNotFoundException;
import org.quizbe.model.Classroom;
import org.quizbe.model.Scope;
import org.quizbe.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassroomService {
  ClassroomRepository classroomRepository;
  UserRepository userRepository;
  ScopeRepository scopeRepository;
  Logger logger = LoggerFactory.getLogger(ClassroomService.class);

  @Autowired
  public ClassroomService(ClassroomRepository classroomRepository, UserRepository userRepository, ScopeRepository scopeRepository) {
    this.classroomRepository = classroomRepository;
    this.userRepository = userRepository;
    this.scopeRepository = scopeRepository;
  }

  public List<Classroom> getAllClassrooms() {
    return Lists.newArrayList(classroomRepository.findAll());
  }

  public List<Classroom> getAllClassroomsOf(User user) {
    return classroomRepository.findByTeacher(user);
  }

  public boolean saveClassroomFromClassroomDto(ClassroomDto classroomDto) {
    // TODO
    // rechercher si il n'existe pas une classe de même nom associée au courrent user
    User teacher = userRepository.findByUsername(classroomDto.getTeacherUsername());
    if (teacher == null)
      throw new UserNotFoundException("Teacher not found");
    List<Classroom> classrooms = classroomRepository.findByTeacher(teacher);
    if (classrooms != null) {
      if (classrooms.stream().anyMatch(classroom -> classroom.getName().equals(classroomDto.getName()))) {
        // a classroom exists whith same name
        return false;
      }
    }
    Classroom classroom = new Classroom();
    classroom.setName(classroomDto.getName());
    classroom.setTeacher(teacher);
    Scope scope = new Scope();
    scope.setName(classroomDto.getScopes().get(0));
    scope.setClassroom(classroom);
    classroom.addScope(scope);
    classroomRepository.save(classroom);
    // logger.info("classroom.getScopes() : " + classroom.getScopes().toString());
    return true;
  }
}
