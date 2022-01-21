package org.quizbe.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.quizbe.dao.RoleRepository;
import org.quizbe.dao.UserRepository;
import org.quizbe.dto.UserDto;
import org.quizbe.exception.UserNotFoundException;
import org.quizbe.model.Role;
import org.quizbe.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private UserRepository userRepository;
  private RoleRepository roleRepository;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
    super();
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
  }

  @Override
  public User saveUserFromUserDto(UserDto userDto) {
    Role role = roleRepository.findByName("USER");
    User user = new User(userDto.getUsername(),
            userDto.getEmail(),
            passwordEncoder.encode(userDto.getPassword()),
            new HashSet<Role>(Arrays.asList(role)));

    return userRepository.save(user);
  }

  @Override
  public User findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public User findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  public List<User> findAll() {
    return userRepository.findAll();
  }

  @Override
  public UserDto findUserDtoById(long id) {
    Optional<User> user = userRepository.findById(id);
    if (user.isPresent()) {
      return fromUserToUserDto(user.get());
    } else {
      throw new UserNotFoundException("Invalid user Id:" + id);
    }
  }

  @Override
  public void delete(User user) {
    userRepository.delete(user);
  }

  @Override
  public Optional<User> findById(long id) {
    return userRepository.findById(id);
  }

  @Override
  public void save(User user) {
    userRepository.save(user);
  }

  /**
   * Add or remove role of user (flipflop)
   * @param role  role too add or remove
   */
  @Override
  public void flipflopUserRole(User user, Role role) {
     if (user.getRoles().contains(role)) {
       user.getRoles().remove(role);
     } else {
       user.getRoles().add(role);
     }
     userRepository.save(user);
  }

  private UserDto fromUserToUserDto(User user) {
    UserDto userDto = new UserDto();
    userDto.setId(user.getId());
    userDto.setUsername(user.getUsername());
    userDto.setEmail(user.getEmail());
    userDto.setPassword(user.getPassword());
    userDto.setRole(user.getRoles().stream().map(r->r.getName()).collect(Collectors.toSet()));
    return userDto;
  }

}
