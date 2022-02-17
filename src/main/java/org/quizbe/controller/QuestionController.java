package org.quizbe.controller;

import org.quizbe.dto.UserDto;
import org.quizbe.exception.ClassroomNotFoundException;
import org.quizbe.model.Classroom;
import org.quizbe.model.User;
import org.quizbe.service.ClassroomService;
import org.quizbe.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.ResourceBundle;


@RequestMapping("/question")
@Controller
public class QuestionController {

  Logger logger = LoggerFactory.getLogger(QuestionController.class);

  private ClassroomService classroomService;
  private UserService userService;

  @Autowired
  public QuestionController(ClassroomService classroomService, UserService userService) {
    this.classroomService = classroomService;
    this.userService = userService;
  }

  @GetMapping(value = {"/index","/", ""})
  public String questions(Model model, HttpServletRequest request) {
    String idSelectedClassroom = request.getParameter("id-selected-classroom");

    Classroom selectedClassroom = null;

    if (idSelectedClassroom != null) {
      long idClassroom = Long.parseLong(idSelectedClassroom);
      selectedClassroom = classroomService.findById(idClassroom)
              .orElseThrow(() -> new ClassroomNotFoundException("Invalid classroom Id:" + idClassroom));
    }

    User currentUser = userService.findByUsername(request.getUserPrincipal().getName());

    List<Classroom> classrooms =
            request.isUserInRole("TEACHER")
                    ? currentUser.getClassrooms()
                    : currentUser.getVisibleClassrooms();

    // user can only see these classrooms (hack)
    // only TEACHER can view not visible classrooms
    if (selectedClassroom != null && !classrooms.contains(selectedClassroom)){
      selectedClassroom = null;
      // throw new ClassroomNotFoundException("Invalid classroom selected Id:" + idSelectedClassroom);
    }

    model.addAttribute("user", currentUser);
    model.addAttribute("classrooms", classrooms);
    model.addAttribute("selectedClassroom", selectedClassroom);
    return "/question/index";
  }

}
