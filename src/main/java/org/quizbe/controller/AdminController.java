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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ResourceBundle;

@RequestMapping("/admin")
@Controller
public class AdminController {

  Logger logger = LoggerFactory.getLogger(AdminController.class);

  private UserService userService;
  private RoleService roleService;

  @Autowired
  public AdminController(UserService userService, RoleService roleService) {
    this.userService = userService;
    this.roleService = roleService;
  }

  @GetMapping("/users")
  public String showUserList(Model model) {
    model.addAttribute("users", userService.findAll());
    model.addAttribute("allRoles", roleService.findAllByOrderByName());
    return "admin/list-users";
  }

  @PostMapping("/adduser")
  public String addUser(@Valid UserDto userDto, BindingResult result, Model model) {
    if (result.hasErrors()) {
      return "admin/add-user";
    }
    userService.saveUserFromUserDto(userDto);
    return "redirect:/admin/index";
  }

  @GetMapping("/edit/{id}")
  public String showUpdateForm(@PathVariable("id") long id, Model model) {
    try {
      UserDto userDto = userService.findUserDtoById(id);
      model.addAttribute("userDto", userDto);
    } catch (UserNotFoundException ex) {
      throw new UserNotFoundException(ex);
    }
    return "admin/update-user";
  }

  @PostMapping("/update/{id}")
  public String updateUser(@Valid @ModelAttribute UserDto userDto, BindingResult bindingResult) {
    // logger.info("bindingResult :" + bindingResult);
    if (bindingResult.hasErrors()) {
      return "admin/update-user";
    }
    userService.saveUserFromUserDto(userDto);
    return "redirect:/admin/users";
  }

  @GetMapping("/delete/{id}")
  public String deleteUser(@PathVariable("id") long id, Model model) {
    User user = userService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
    userService.delete(user);
    return "redirect:/admin/users";
  }

  @PostMapping("/role")
  public String updateRoleUser(HttpServletRequest request) {
    Long id = Long.parseLong(request.getParameter("id"));
    String roleName = request.getParameter("rolename");

    String nameCurrentUser = request.getUserPrincipal().getName();
    User currentUser = userService.findByUsername(nameCurrentUser);

    User userToUpdate = userService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

    logger.info("userToUpdate : " + userToUpdate);
    logger.info("currentUser : " + currentUser);
    logger.info("roleName : " + roleName);

    // "super admin" stay admin
    if (roleName.equals("ADMIN")) {
      if (currentUser.getId() == 1 && userToUpdate.getId() == 1) {
        // Le super utilisateur reste admin !
        return "redirect:/admin/users";
      }
      if (currentUser.getId() > 1) {
        // non autorisé à gérer les rôles ADMIN
        return "redirect:/admin/users";
      }
    }

    Role role = roleService.findByName(roleName);
    if (role == null) {
      throw new IllegalArgumentException("Invalid role name:" + roleName);
    }

   // boolean userToUpdateIsAdmin = userToUpdate.getRoles().stream().anyMatch(r -> r.getName().equals("ADMIN"));

    userService.flipflopUserRole(userToUpdate, role);

    return "redirect:/admin/users";
  }


  @GetMapping(value = {"/register",})
  public String register(@ModelAttribute UserDto userDto) {
    return "/admin/registration";
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
      return "/admin/registration";
    }
    userService.saveUserFromUserDto(userDto);
    return "redirect:/";
  }


}
