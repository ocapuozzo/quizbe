package org.quizbe.controller;

import org.quizbe.dto.UserDto;
import org.quizbe.model.User;
import org.quizbe.service.TopicService;
import org.quizbe.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.ResourceBundle;

@Controller
public class IndexController {

  Logger logger = LoggerFactory.getLogger(IndexController.class);

  private TopicService topicService;
  private UserService userService;

  @Autowired
  public IndexController(TopicService topicService, UserService userService) {
    this.topicService = topicService;
    this.userService = userService;
  }

  @GetMapping(value = {"/",})
  public String index() {
    return "/main/index";
  }

//
//  @GetMapping(value = {"/changelocale",})
//  public String changeLocale(HttpServletRequest request) {
//    String url = request.getParameter("url");
//    //return "/main/index";
//    return "redirect:"+url;
//  }


  @GetMapping(value = {"/login",})
  public String login() {
    return "/main/login";
  }


  @GetMapping(value = {"/register",})
  public String register(@ModelAttribute UserDto userDto) {
    //model.addAttribute("user", new UserRegistrationDto());
    return "/main/registration";
  }

  @PostMapping(value = {"/register",})
  public String registerPost(@Valid @ModelAttribute UserDto userDto, BindingResult bindingResult) {
    User userExists = userService.findByUsername(userDto.getUsername());
    if (userExists != null) {
      // pour aller chercher le bon message avec la locale
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

  /**
    @See WebSecurityConfiguration
   */
  @GetMapping(value = {"/access-denied",})
  public String acceDenied() {
    return "/error/access-denied";
 }

}
