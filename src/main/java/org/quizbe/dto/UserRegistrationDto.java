package org.quizbe.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserRegistrationDto {

  @NotBlank(message = "{user.username.blank}")
  @Size(min = 3, max = 30, message = "{user.username.min.max}")
  private String userName;

  @NotBlank(message = "{user.email.blank}")
  @Email(message = "{user.email.invalid}")
  private String email;

  @NotBlank(message = "{user.password.blank}")
  @Size(min = 6, message = "{user.password.min}")
  private String password;

  public UserRegistrationDto() {

  }

  public UserRegistrationDto(String userName, String email, String password) {
    super();
    this.userName = userName;
    this.email = email;
    this.password = password;
  }

  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "UserRegistrationDto {" +
            "userName='" + userName + '\'' +
            ", email='" + email + '\'' +
            '}';
  }
}
