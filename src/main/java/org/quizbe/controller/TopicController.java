package org.quizbe.controller;

import org.quizbe.dto.TopicDto;
import org.quizbe.dto.ScopeDto;
import org.quizbe.exception.TopicNotFoundException;
import org.quizbe.model.Topic;
import org.quizbe.model.User;
import org.quizbe.service.TopicService;
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
import java.util.List;
import java.util.stream.Collectors;


@RequestMapping("/topic")
@Controller
public class TopicController {

  Logger logger = LoggerFactory.getLogger(TopicController.class);

  private TopicService topicService;
  private UserService userService;

  @Autowired
  public TopicController(TopicService topicService, UserService userService) {
    this.topicService = topicService;
    this.userService = userService;
  }

  @GetMapping(value = {"/index","/", ""})
  public String index(Model model, HttpServletRequest request) {
    String nameCurrentUser = request.getUserPrincipal().getName();
    User currentUser = userService.findByUsername(nameCurrentUser);

    model.addAttribute("topics", topicService.getAllTopicsOf(currentUser));
    return "/topic/index";
  }

  @GetMapping(value = {"/add",})
  public String register(@ModelAttribute TopicDto topicDto) {
    if(topicDto.getScopesDtos().isEmpty()) {
      topicDto.getScopesDtos().add(new ScopeDto("Scope1"));
      topicDto.getScopesDtos().add(new ScopeDto("Scope2"));
      topicDto.getScopesDtos().add(new ScopeDto("Scope3"));
    } else {
      // already set
    }
    return "topic/add-update";
  }

  @GetMapping("/edit/{id}")
  public String showUpdateForm(@PathVariable("id") long id, Model model) {
    try {
      TopicDto topicDto = topicService.findTopicDtoById(id);
      model.addAttribute("topicDto", topicDto);
    } catch (TopicNotFoundException ex) {
      throw new ResponseStatusException(
              HttpStatus.NOT_FOUND, "Topic Not Found", ex);
    }
    return "topic/add-update";
  }

  @PostMapping(value= {"/addupdate"})
  public String addOrUpdateClassroom(@Valid TopicDto topicDto, BindingResult result, Model model, HttpServletRequest request) {

    // clean scopesDto by remove scopeDto with name is null
    topicDto.setScopesDtos(topicDto.getScopesDtos().stream().filter(scopeDto -> scopeDto.getName()!= null).collect(Collectors.toList()));

    logger.info("topicDto addupdate :" + topicDto);

    if (result.hasErrors()) {
      return "topic/add-update";
    }

    if (topicDto.getId() == null) {
      String nameCurrentUser = request.getUserPrincipal().getName();
      topicDto.setTeacherUsername(nameCurrentUser);
    }

    topicService.saveTopicFromTopicDto(topicDto, result);

    if (result.hasErrors()) {
      return "topic/add-update";
    }

    return "redirect:/topic/index";
  }

  @PostMapping(value= {"/visible"})
  public String setVisible(HttpServletRequest request)
  {
    Long id = Long.parseLong(request.getParameter("id"));

    Topic topic = topicService.findTopicById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid topic Id:" + id));

    String visible = request.getParameter("visible");
    logger.info("visible = " + visible);
    // checkbox not ckecked => visible == null
    topic.setVisible(visible != null);
    topicService.saveTopic(topic);

    return "redirect:/topic/index";
  }

  @GetMapping("/subscribed")
  public String manageSubscribedTopics(Model model, HttpServletRequest request) {
    List<Topic> allTopics = topicService.getAllTopics();
    String nameCurrentUser = request.getUserPrincipal().getName();
    User currentUser = userService.findByUsername(nameCurrentUser);
    model.addAttribute("topics", allTopics);
    model.addAttribute("currentUser", currentUser);
    return "topic/subscribed";
  }

  @PostMapping("/subscribed")
  public String doSubscribedTopics(Model model, HttpServletRequest request) {
    Long id = Long.parseLong(request.getParameter("id"));

    Topic topic = topicService.findTopicById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid topic Id:" + id));

    String nameCurrentUser = request.getUserPrincipal().getName();
    User currentUser = userService.findByUsername(nameCurrentUser);

    if (currentUser.getTopics().contains(topic)) {
      currentUser.getTopics().remove(topic);
    } else {
      currentUser.getTopics().add(topic);
    }
    userService.save(currentUser);

    return "redirect:/topic/index";
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
