package org.quizbe.service;

import org.quizbe.dto.UserRegistrationDto;
import org.quizbe.model.User;

public interface UserService  {
    User saveUserFromUserDto(UserRegistrationDto registrationDto);
    User findByEmail(String email);
    User findByUsername(String username);
}
