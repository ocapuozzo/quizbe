package org.quizbe.controller;

import org.quizbe.exception.TopicNotFoundException;
import org.quizbe.model.Topic;
import org.quizbe.model.User;
import org.quizbe.service.TopicService;
import org.quizbe.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;


@RequestMapping("/question")
@Controller
public class QuestionController {

  Logger logger = LoggerFactory.getLogger(QuestionController.class);

  private TopicService topicService;
  private UserService userService;

  @Autowired
  public QuestionController(TopicService topicService, UserService userService) {
    this.topicService = topicService;
    this.userService = userService;
  }

  @GetMapping(value = {"/index","/", ""})
  public String questions(Model model, HttpServletRequest request) {
    String idSelectedTopic = request.getParameter("id-selected-topic");

    Topic selectedTopic = null;

    if (idSelectedTopic != null) {
      long idTopic = Long.parseLong(idSelectedTopic);
      selectedTopic = topicService.findById(idTopic)
              .orElseThrow(() -> new TopicNotFoundException("Invalid topic id : " + idTopic));
    }

    User currentUser = userService.findByUsername(request.getUserPrincipal().getName());

    List<Topic> topics =
            request.isUserInRole("TEACHER")
                    ? currentUser.getSubscribedTopics().stream().collect(Collectors.toList())
                    : currentUser.getSubscribedTopicsVisibles();

    // user can only see these topics (hack)
    // only TEACHER can view not visible topics
    if (selectedTopic != null && !topics.contains(selectedTopic)){
      selectedTopic = null;
      // throw new TopicNotFoundException("Invalid classroom selected Id:" + idSelectedTopic);
    }

    model.addAttribute("currentUser", currentUser);
    model.addAttribute("topics", topics);
    model.addAttribute("selectedTopic", selectedTopic);
    return "/question/index";
  }

}
