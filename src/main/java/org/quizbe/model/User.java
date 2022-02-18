package org.quizbe.model;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "USER", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "USERNAME")
  private String username;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "PASSWORD")
  private String password;

  @Column(name = "ACTIVE")
  private Boolean enabled;

  @Basic
  private Boolean accountNonExpired;

  @Basic
  private Boolean credentialsNonExpired;

  @Basic
  private Boolean accountNonLocked;


  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(
          name = "USER_ROLES",
          joinColumns = @JoinColumn(
                  name = "USER_ID", referencedColumnName = "id"),
          inverseJoinColumns = @JoinColumn(
                  name = "ROLE_ID", referencedColumnName = "id"))
  private Set< Role > roles;

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(
          name = "USER_TOPICS",
          joinColumns = @JoinColumn(
                  name = "USER_ID", referencedColumnName = "id"),
          inverseJoinColumns = @JoinColumn(
                  name = "TOPIC_ID", referencedColumnName = "id"))
  private Set<Topic> subscribedTopics;

  @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
  private List<Topic> topics;

  public User() {
    this.enabled = true;
    this.accountNonExpired = true;
    this.accountNonLocked = true;
    this.credentialsNonExpired = true;
  }

  public User(String userName, String email, String password, Set < Role > roles) {
    super();
    this.username = userName;
    this.email = email;
    this.password = password;
    this.roles = roles;
    this.enabled = true;
  }
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public String getUsername() {
    return username;
  }
  public void setUsername(String userName) {
    this.username = userName;
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

  public Boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean active) {
    this.enabled = active;
  }

  public Set < Role > getRoles() {
    return roles;
  }
  public void setRoles(Set < Role > roles) {
    this.roles = roles;
  }

  public Set<Topic> getSubscribedTopics() {
    return subscribedTopics;
  }

  public List<Topic> getSubscribedTopicsVisibles() {
    return subscribedTopics.stream().filter(topic -> topic.isVisible()).collect(Collectors.toList());
  }

  public void setSubscribedTopics(Set<Topic> subscribedTopics) {
    this.subscribedTopics = subscribedTopics;
  }

  public boolean isSubscribed(Topic topic) {
    return this.subscribedTopics.contains(topic);
  }


  public Boolean isAccountNonExpired() {
    return accountNonExpired;
  }

  public void setAccountNonExpired(Boolean accountNonExpired) {
    this.accountNonExpired = accountNonExpired;
  }

  public Boolean isCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
    this.credentialsNonExpired = credentialsNonExpired;
  }

  public Boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  public void setAccountNonLocked(Boolean accountNonLocked) {
    this.accountNonLocked = accountNonLocked;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public Boolean getAccountNonExpired() {
    return accountNonExpired;
  }

  public Boolean getCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  public Boolean getAccountNonLocked() {
    return accountNonLocked;
  }

  public List<Topic> getTopics() {
    return topics;
  }

  public List<Topic> getVisibleTopics() {
    return topics.stream().filter(topic -> topic.isVisible()).collect(Collectors.toList());
  }

  public void setTopics(List<Topic> topics) {
    this.topics = topics;
  }

  @Override
  public String toString() {
    return "User{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", enabled=" + enabled +
            '}';
  }
}
