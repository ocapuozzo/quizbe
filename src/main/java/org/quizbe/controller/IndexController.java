package org.quizbe.controller;

import org.quizbe.dto.UserRegistrationDto;
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

import javax.validation.Valid;
import java.util.ResourceBundle;

@Controller
public class IndexController {

  Logger logger = LoggerFactory.getLogger(IndexController.class);

  private ClassroomService classroomService;
  private UserService userService;

  @Autowired
  public IndexController(ClassroomService classroomService, UserService userService) {
    this.classroomService = classroomService;
    this.userService = userService;
  }

  @GetMapping(value = {"/question",})
  public String questions(Model model) {
    // TODO get classrooms of user
    model.addAttribute("classrooms", classroomService.getAllClassrooms());
    return "/main/index";
  }

  @GetMapping(value = {"/",})
  public String index() {
    return "/main/index";
  }


  @GetMapping(value = {"/login",})
  public String login() {
    return "/main/login";
  }


  @GetMapping(value = {"/register",})
  public String register(UserRegistrationDto userRegistrationDto) {
    //model.addAttribute("user", new UserRegistrationDto());
    return "/main/registration";
  }

  @PostMapping(value = {"/register",})
  public String registerPost(@Valid @ModelAttribute UserRegistrationDto userDto, BindingResult bindingResult) {
    User userExists = userService.findByUsername(userDto.getUserName());
    if (userExists != null) {
      // bidouille pour aller chercher le bon message avec la locale
      ResourceBundle bundle = ResourceBundle.getBundle("i18n/validationMessages", LocaleContextHolder.getLocale());
      String errorMessageDefault = bundle.getString("user.username.already.exist");
      String keyNotExists = "user.username.already.exist.constraint";
      bindingResult
              .rejectValue("userName", keyNotExists, errorMessageDefault);
    }
    if (bindingResult.hasErrors()) {
      return "/main/registration";
    }
    userService.saveUserFromUserDto(userDto);
    return "redirect:/";
  }



}
