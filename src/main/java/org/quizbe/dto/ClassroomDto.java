
package org.quizbe.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


public class ClassroomDto {

  @NotBlank(message = "{classroom.name.blank}")
  @Size(min = 3, max = 30, message = "{classroom.name.min.max}")
  private String name;

  private Long id;

  @NotNull
  @Size(min = 1)
  private List<@NotBlank String> scopes;

  // auto renseigné = current user
  private String teacherUsername;

  public ClassroomDto() {

  }

  public ClassroomDto(String name) {
    super();
    this.name = name;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return this.id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getScopes() {
    return scopes;
  }

  public void setScopes(List<String> scopes) {
    this.scopes = scopes;
  }

  public String getTeacherUsername() {
    return teacherUsername;
  }

  public void setTeacherUsername(String teacherUsername) {
    this.teacherUsername = teacherUsername;
  }
}
