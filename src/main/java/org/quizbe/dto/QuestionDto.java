
package org.quizbe.dto;


import javax.persistence.Basic;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


public class QuestionDto {

  private Long id;
  private Long idTopic;
  private Long idScope;
  // auto set = current user (username)
  private String creatorUsername;

  @NotBlank(message = "{question.title.blank}")
  @Size(min = 3, max = 30, message = "{question.title.min.max}")
  private String title;

  @NotBlank(message = "{question.sentence.blank}")
  @Size(min = 3, max = 255, message = "{question.sentence.min.max}")
  private String sentence;

  @NotBlank(message = "{question.designer.blank}")
  @Size(min = 3, max = 30, message = "{question.designer.min.max}")
  private String designer;

  @Basic
  private String codesigner;

  @NotNull
  @Size(min = 1)
  @Valid private List<ResponseDto> responseDtos;

  public QuestionDto() {
  }

  public Long getIdTopic() {
    return idTopic;
  }

  public void setIdTopic(Long idTopic) {
    this.idTopic = idTopic;
  }

  public Long getIdScope() {
    return idScope;
  }

  public void setIdScope(Long idScope) {
    this.idScope = idScope;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<ResponseDto> getResponseDtos() {
    return responseDtos;
  }

  public void setResponseDtos(List<ResponseDto> responseDtos) {
    this.responseDtos = responseDtos;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return this.id;
  }

  public String getCreatorUsername() {
    return creatorUsername;
  }

  public void setCreatorUsername(String creatorUsername) {
    this.creatorUsername = creatorUsername;
  }

  public QuestionDto(Long id, Long idTopic, Long idScope, String creatorUsername, String title, List<ResponseDto> responseDtos) {
    this.id = id;
    this.idTopic = idTopic;
    this.idScope = idScope;
    this.creatorUsername = creatorUsername;
    this.title = title;
    this.responseDtos = responseDtos;
  }
}
