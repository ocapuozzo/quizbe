package org.quizbe.controller;

import org.quizbe.dto.UserDto;
import org.quizbe.exception.UserNotFoundException;
import org.quizbe.model.Role;
import org.quizbe.model.User;
import org.quizbe.service.RoleService;
import org.quizbe.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;
import java.util.ResourceBundle;

@RequestMapping("/user")
@Controller
public class UserController {

  Logger logger = LoggerFactory.getLogger(UserController.class);

  private UserService userService;
  private RoleService roleService;

  @Autowired
  public UserController(UserService userService, RoleService roleService) {
    this.userService = userService;
    this.roleService = roleService;
  }

  @GetMapping(value = {"/edit/{id}", "/edit/"})
  public String showUpdateForm(@PathVariable("id") Optional<Long> id, Model model, HttpServletRequest request) {
    if (id.isPresent()) {
      try {
        User currentUser = userService.findByUsername(request.getUserPrincipal().getName());
        if (!request.isUserInRole("ADMIN") && currentUser.getId() != id.get()) {
          throw new AccessDeniedException("");
        }
        UserDto userDto = userService.findUserDtoById(id.get());
        model.addAttribute("userDto", userDto);
      } catch (UserNotFoundException ex) {
        throw new UserNotFoundException(ex);
      }
    } else {
      try {
        User currentUser = userService.findByUsername(request.getUserPrincipal().getName());
        UserDto userDto = userService.findUserDtoById(currentUser.getId());
        model.addAttribute("userDto", userDto);
      } catch (UserNotFoundException ex) {
        throw new UserNotFoundException(ex);
      }
    }
    return "admin/update-user";
  }

  @PostMapping("/update/{id}")
  public String updateUser(@Valid @ModelAttribute UserDto userDto,
                           BindingResult bindingResult, HttpServletRequest request) {
    // logger.info("bindingResult :" + bindingResult);
    User currentUser = userService.findByUsername(request.getUserPrincipal().getName());
    if (!request.isUserInRole("ADMIN") && currentUser.getId() != userDto.getId()) {
      throw new AccessDeniedException("");
    }
    if (bindingResult.hasErrors()) {
      return "admin/update-user";
    }

    userService.saveUserFromUserDto(userDto);

    if (request.isUserInRole("ADMIN")) {
      return "redirect:/admin/users";
    } else {
      return "redirect:/";
    }
  }

}