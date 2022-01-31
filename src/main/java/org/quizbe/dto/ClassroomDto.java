
package org.quizbe.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


public class ClassroomDto {

  @NotBlank(message = "{classroom.name.blank}")
  @Size(min = 3, max = 30, message = "{classroom.name.min.max}")
  private String name;

  private Long id;

  @NotNull
  @Size(min = 1)
  private List<ScopeDto> scopesDtos;

  // auto renseigné = current user
  private String teacherUsername;

  public ClassroomDto() {

  }

  public ClassroomDto(String name) {
    super();
    this.name = name;
    this.scopesDtos = new ArrayList<>();
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

  public List<ScopeDto> getScopesDtos() {
    if (this.scopesDtos == null) {
      this.scopesDtos = new ArrayList<>();
    }
    return this.scopesDtos;
  }

  public void setScopesDtos(List<ScopeDto> scopesDtos) {
    if (scopesDtos != null) {
      this.scopesDtos = scopesDtos;
    } else  {
      this.scopesDtos.clear();
    }
  }

  public String getTeacherUsername() {
    return teacherUsername;
  }

  public void setTeacherUsername(String teacherUsername) {
    this.teacherUsername = teacherUsername;
  }

  @Override
  public String toString() {
    return "ClassroomDto{" +
            "name='" + name + '\'' +
            ", id=" + id +
            ", scopes=" + scopesDtos +
            ", teacherUsername='" + teacherUsername + '\'' +
            '}';
  }
}
