package org.quizbe.controller;

import org.quizbe.dto.UserDto;
import org.quizbe.model.User;
import org.quizbe.service.ClassroomService;
import org.quizbe.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
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
  public String questions(Model model) {
    // TODO get classrooms of user
    model.addAttribute("classrooms", classroomService.getAllClassrooms());
    return "/question/index";
  }

}
