package org.quizbe.service;

import java.util.Arrays;
import java.util.HashSet;

import org.quizbe.dao.RoleRepository;
import org.quizbe.dao.UserRepository;
import org.quizbe.dto.UserRegistrationDto;
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
  public User saveUserFromUserDto(UserRegistrationDto registrationDto) {
    Role role = roleRepository.findByName("USER");
    User user = new User(registrationDto.getUserName(),
            registrationDto.getEmail(),
            passwordEncoder.encode(registrationDto.getPassword()),
            new HashSet<Role>(Arrays.asList(role)));

    return userRepository.save(user);
  }

  @Override
  public User findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public User findByUserName(String userName) {
    return userRepository.findByUserName(userName);
  }

}
