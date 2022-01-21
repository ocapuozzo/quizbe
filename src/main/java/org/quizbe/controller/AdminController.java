package org.quizbe.controller;

import org.quizbe.dto.UserDto;
import org.quizbe.model.Role;
import org.quizbe.model.User;
import org.quizbe.service.RoleService;
import org.quizbe.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
    UserDto userDto = userService.findUserDtoById(id);
    model.addAttribute("user", userDto);
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

  @PostMapping("/role")
  public String updateRoleUser(HttpServletRequest request) {
    Long id = Long.parseLong(request.getParameter("id"));
    String roleName = request.getParameter("rolename");

    String nameCurrentUser = request.getUserPrincipal().getName();
    User currentUser = userService.findByUsername(nameCurrentUser);

    if (roleName.equals("ADMIN") && currentUser.getId() != 1) {
      // non autorisé à gérer les rôles ADMIN
      return "redirect:/admin/users";
    }

    User userToUpdate = userService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

    Role role = roleService.findByName(roleName);
    if (role == null) {
      throw new IllegalArgumentException("Invalid role name:" + roleName);
    }

    boolean userToUpdateIsAdmin = userToUpdate.getRoles().stream().anyMatch(r -> r.getName().equals("ADMIN"));

    logger.info(" currentUser.getId() : " +  currentUser.getId());
    logger.info(" userToUpdate.getUsername() : " +  userToUpdate.getUsername());
    logger.info("nameCurrentUser : " +  nameCurrentUser);
    logger.info("userToUpdateIsAdmin :" + userToUpdateIsAdmin);

    if (userToUpdateIsAdmin && (!userToUpdate.getUsername().equals(nameCurrentUser) && currentUser.getId() != 1)) {
      // non autorisé à gérer les rôles des autres ADMIN, sauf le SUPER_ADMIN (id=1)
      return "redirect:/admin/users";
    }

    userService.flipflopUserRole(userToUpdate, role);

    return "redirect:/admin/users";
  }

}
