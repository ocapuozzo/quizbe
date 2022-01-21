package org.quizbe.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;


// https://www.baeldung.com/spring-boot-custom-error-page

@Controller
public class QuizbeErrorController implements ErrorController {

  @RequestMapping("/error")
  public String handleError(HttpServletRequest request, Model model) {
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

    if (status != null) {
      model.addAttribute("status", status);
      Integer statusCode = Integer.valueOf(status.toString());

      if(statusCode == HttpStatus.NOT_FOUND.value()) {
        return "error/error-404";
      }
//      else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
//        return "error/error-500";
//      }
    }
    return "error/error";
  }

  @Override
  public String getErrorPath() {
    return "/error";
  }
}
