package org.quizbe.service;

import com.google.common.collect.Lists;
import org.quizbe.dao.ClassroomRepository;
import org.quizbe.dao.ScopeRepository;
import org.quizbe.dao.UserRepository;
import org.quizbe.dto.ClassroomDto;
import org.quizbe.dto.ScopeDto;
import org.quizbe.exception.ClassroomNotFoundException;
import org.quizbe.exception.UserNotFoundException;
import org.quizbe.model.Classroom;
import org.quizbe.model.Scope;
import org.quizbe.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ValidationUtils;

import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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

  public boolean saveClassroomFromClassroomDto(ClassroomDto classroomDto, BindingResult result) {
    // rechercher si il n'existe pas une classe de même nom associée au courrent user

    User teacher = userRepository.findByUsername(classroomDto.getTeacherUsername());

    if (teacher == null) {
      throw new UserNotFoundException("Teacher not found :" + classroomDto.getTeacherUsername());
    }

    List<Classroom> classrooms = classroomRepository.findByTeacher(teacher);

    // create classroom ?
    if (classrooms != null && classroomDto.getId() == null) {
      if (classrooms.stream().anyMatch(classroom -> classroom.getName().equals(classroomDto.getName()))) {
        // a classroom exists whith same name
        //result.rejectValue("name", "classroom.name.already.exists", "already exists");
        ResourceBundle bundle = ResourceBundle.getBundle("i18n/validationMessages", LocaleContextHolder.getLocale());
        String errorMessageDefault = bundle.getString("classroom.name.already.exists");
        String keyNotExists = "classroom.name.already.exists";
        result.rejectValue("name", keyNotExists, errorMessageDefault);
        return false;
      }
    }
    Classroom classroom;
    if (classroomDto.getId() != null) {
      // update
      classroom = classroomRepository.findById(classroomDto.getId()).orElseThrow(
              ClassroomNotFoundException::new);
      classroom.removeScopes();
      classroom.setName(classroomDto.getName());
    } else {
      classroom = new Classroom();
      classroom.setName(classroomDto.getName());
      classroom.setTeacher(teacher);
    }
    // add/update all scopes
    for (ScopeDto scopeDto : classroomDto.getScopesDtos()) {
      Scope scope = new Scope();
      scope.setName(scopeDto.getName());
      if (scopeDto.getId() != null) {
        scope.setId(scopeDto.getId());
      }
      classroom.addScope(scope);
      logger.info("scope add : " + scope);
    }

    classroom.setVisible(classroomDto.isVisible());
    classroomRepository.save(classroom);

    return true;
  }

  public ClassroomDto findClassroomDtoById(long id) {
    Optional<Classroom> classroom = classroomRepository.findById(id);
    if (classroom.isPresent()) {
      return fromClassroomToClassroomDto(classroom.get());
    } else {
      throw new ClassroomNotFoundException("Invalid classroom Id:" + id);
    }
  }

  private ClassroomDto fromClassroomToClassroomDto(Classroom classroom) {
    ClassroomDto classroomDto = new ClassroomDto();
    classroomDto.setId(classroom.getId());
    classroomDto.setName(classroom.getName());
    classroomDto.setVisible(classroom.isVisible());
    classroomDto.setTeacherUsername(classroom.getTeacher().getUsername());
    classroomDto.setScopesDtos(classroom.getScopes().stream().map(scope -> new ScopeDto(scope.getId(), scope.getName())).collect(Collectors.toList()));
    return classroomDto;
  }

  public Optional<Classroom> findClassroomById(long id) {
    return classroomRepository.findById(id);
  }

  public void saveClassroom(Classroom classroom) {
    classroomRepository.save(classroom);
  }

  public Optional<Classroom> findById(long idClassroom) {
    return classroomRepository.findById(idClassroom);
  }
}

