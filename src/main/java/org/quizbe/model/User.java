package org.quizbe.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "USER", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "USER_NAME")
  private String userName;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "PASSWORD")
  private String password;

  @Column(name = "ACTIVE")
  private Boolean active;

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(
          name = "USER_ROLES",
          joinColumns = @JoinColumn(
                  name = "USER_ID", referencedColumnName = "id"),
          inverseJoinColumns = @JoinColumn(
                  name = "ROLE_ID", referencedColumnName = "id"))

  private Set< Role > roles;

  public User() {

  }

  public User(String userName, String email, String password, Set < Role > roles) {
    super();
    this.userName = userName;
    this.email = email;
    this.password = password;
    this.roles = roles;
  }
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
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

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public Set < Role > getRoles() {
    return roles;
  }
  public void setRoles(Set < Role > roles) {
    this.roles = roles;
  }

}
