package org.quizbe.controller;

import org.quizbe.dto.ClassroomDto;
import org.quizbe.dto.ScopeDto;
import org.quizbe.exception.ClassroomNotFoundException;
import org.quizbe.model.Classroom;
import org.quizbe.model.User;
import org.quizbe.service.ClassroomService;
import org.quizbe.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.stream.Collectors;


@RequestMapping("/classroom")
@Controller
public class ClassroomController {

  Logger logger = LoggerFactory.getLogger(ClassroomController.class);

  private ClassroomService classroomService;
  private UserService userService;

  @Autowired
  public ClassroomController(ClassroomService classroomService, UserService userService) {
    this.classroomService = classroomService;
    this.userService = userService;
  }

  @GetMapping(value = {"/index","/", ""})
  public String index(Model model, HttpServletRequest request) {
    String nameCurrentUser = request.getUserPrincipal().getName();
    User currentUser = userService.findByUsername(nameCurrentUser);

    model.addAttribute("classrooms", classroomService.getAllClassroomsOf(currentUser));
    return "/classroom/index";
  }

  @GetMapping(value = {"/add",})
  public String register(@ModelAttribute ClassroomDto classroomDto) {
    if(classroomDto.getScopesDtos().isEmpty()) {
      classroomDto.getScopesDtos().add(new ScopeDto("Scope1"));
      classroomDto.getScopesDtos().add(new ScopeDto("Scope2"));
      classroomDto.getScopesDtos().add(new ScopeDto("Scope3"));
    } else {
      // already set
    }
    return "classroom/add-update";
  }

  @GetMapping("/edit/{id}")
  public String showUpdateForm(@PathVariable("id") long id, Model model) {
    try {
      ClassroomDto classroomDto = classroomService.findClassroomDtoById(id);
      model.addAttribute("classroomDto", classroomDto);
    } catch (ClassroomNotFoundException ex) {
      throw new ResponseStatusException(
              HttpStatus.NOT_FOUND, "Classrooù Not Found", ex);
    }
    return "classroom/add-update";
  }

  @PostMapping(value= {"/addupdate"})
  public String addOrUpdateClassroom(@Valid ClassroomDto classroomDto, BindingResult result, Model model, HttpServletRequest request) {

    // clean scopesDto by remove scopeDto with name is null
    classroomDto.setScopesDtos(classroomDto.getScopesDtos().stream().filter(scopeDto -> scopeDto.getName()!= null).collect(Collectors.toList()));

    logger.info("classroomDto addupdate :" + classroomDto);

    if (result.hasErrors()) {
      return "classroom/add-update";
    }

    if (classroomDto.getId() == null) {
      String nameCurrentUser = request.getUserPrincipal().getName();
      classroomDto.setTeacherUsername(nameCurrentUser);
    }

    classroomService.saveClassroomFromClassroomDto(classroomDto, result);

    if (result.hasErrors()) {
      return "classroom/add-update";
    }

    return "redirect:/classroom/index";
  }

  @PostMapping(value= {"/visible"})
  public String setVisible(HttpServletRequest request)
  {
    Long id = Long.parseLong(request.getParameter("id"));

    Classroom classroom = classroomService.findClassroomById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid classroom Id:" + id));

    String visible = request.getParameter("visible");
    logger.info("visible = " + visible);
    // checkbox not ckecked => visible == null
    classroom.setVisible(visible != null);
    classroomService.saveClassroom(classroom);

    return "redirect:/classroom/index";
  }

/*
  @GetMapping("/edit/{id}")
  public String showUpdateForm(@PathVariable("id") long id, Model model) {
    try {
      UserDto userDto = userService.findUserDtoById(id);
      model.addAttribute("user", userDto);
    } catch (UserNotFoundException ex) {
      throw new ResponseStatusException(
              HttpStatus.NOT_FOUND, "User Not Found", ex);
    }
    return "admin/update-user";
  }

  @PostMapping("/update/{id}")
  public String updateUser(@Valid @ModelAttribute UserDto userDto, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "admin/update-user";
    }
    userService.saveUserFromUserDto(userDto);
    return "redirect:/index";
  }

  @GetMapping("/delete/{id}")
  public String deleteUser(@PathVariable("id") long id, Model model) {
    User user = userService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
    userService.delete(user);
    return "redirect:/admin/index";
  }
*/

}
