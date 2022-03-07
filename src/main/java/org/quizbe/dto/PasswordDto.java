
package org.quizbe.dto;


import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


public class PasswordDto {

  @NotBlank(message = "{password.name.blank}")
  @Size(min = 8, max = 30, message = "{password.name.min.max}")
  private String password;

  @NotBlank(message = "{password.name.blank}")
  @Size(min = 8, max = 30, message = "{password.name.min.max}")
  private String confirmePassword;

  public PasswordDto() {

  }

  public PasswordDto(String password, String confirmePassword) {
    super();
    this.password = password;
    this.confirmePassword = confirmePassword;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getConfirmePassword() {
    return confirmePassword;
  }

  public void setConfirmePassword(String confirmePassword) {
    this.confirmePassword = confirmePassword;
  }
}
