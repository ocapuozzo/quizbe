package org.quizbe.controller;

import org.quizbe.dto.QuestionDto;
import org.quizbe.dto.TopicDto;
import org.quizbe.exception.ScopeNotFoundException;
import org.quizbe.exception.TopicNotFoundException;
import org.quizbe.model.Question;
import org.quizbe.model.Scope;
import org.quizbe.model.Topic;
import org.quizbe.model.User;
import org.quizbe.service.ScopeService;
import org.quizbe.service.TopicService;
import org.quizbe.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequestMapping("/question")
@Controller
public class QuestionController {

  Logger logger = LoggerFactory.getLogger(QuestionController.class);

  private TopicService topicService;
  private UserService userService;
  private ScopeService scopeService;

  @Autowired
  public QuestionController(TopicService topicService, UserService userService, ScopeService scopeService) {
    this.topicService = topicService;
    this.userService = userService;
    this.scopeService = scopeService;
  }

  @GetMapping(value = {"/index", "/", ""})
  public String questions(Model model, HttpServletRequest request) {
    String idSelectedTopic = request.getParameter("id-selected-topic");
    String idSelectedScope = request.getParameter("id-selected-scope");

    Topic selectedTopic = null;
    Scope selectedScope = null;
    List<Question> questions = new ArrayList<>();

    // get idSelectedTopic AND (optional) idSelectedScope

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
    if (selectedTopic != null) {
      if (!topics.contains(selectedTopic)) {
        selectedTopic = null;
        selectedScope = null;
        // throw new TopicNotFoundException("Invalid classroom selected Id:" + idSelectedTopic);
      } else {
        if (idSelectedScope != null) {
          long idScope = Long.parseLong(idSelectedScope);
          selectedScope = scopeService.findById(idScope)
                  .orElseThrow(() -> new ScopeNotFoundException("Invalid id : " + idSelectedScope));
        }
        questions = selectedTopic.getQuestions(selectedScope);
      }

    }

    model.addAttribute("currentUser", currentUser);
    model.addAttribute("topics", topics);
    model.addAttribute("selectedTopic", selectedTopic);
    model.addAttribute("selectedScope", selectedScope);
    model.addAttribute("questions", questions);

    return "/question/index";
  }

  @GetMapping(value={"/new/{idtopic}/{idscope}", "/new/{idtopic}"})
  public String newQuestion(@PathVariable("idtopic") long idTopic,
                            @PathVariable("idscope") Optional<Long> idScope,
                            HttpServletRequest request, Model model) {
    Topic topic = topicService.findTopicById(idTopic)
            .orElseThrow(() -> new TopicNotFoundException("Topic error id : " + idTopic));

    Scope scope = null; // scope may be null
    if (idScope.isPresent()) {
      scope = scopeService.findById(idScope.get()).orElse(null);
    }
   User currentUser = userService.findByUsername(request.getUserPrincipal().getName());

    logger.info("topic = " + topic+" scope = " + scope);

    // placer topic et selectedScope dans une instance de QuestionDtO
    QuestionDto questionDto =
            new QuestionDto(null, topic, scope==null ? null : scope.getId() , currentUser.getUsername());

    model.addAttribute("selectedScope", scope);
    model.addAttribute("questionDto", questionDto);

    return "/question/add-question";
  }


  @PostMapping(value= {"/addupdate"})
  public String addOrUpdateClassroom(QuestionDto questionDto, BindingResult result, Model model, HttpServletRequest request) {

    if (result.hasErrors()) {
      return "question/add-update";
    }
//
//    if (topicDto.getId() == null) {
//      String nameCurrentUser = request.getUserPrincipal().getName();
//      topicDto.setCreatorUsername(nameCurrentUser);
//    }
//
//    topicService.saveTopicFromTopicDto(topicDto, result);
//
//    if (result.hasErrors()) {
//      return "topic/add-update";
//    }

    return "redirect:/question/index";
  }


}
