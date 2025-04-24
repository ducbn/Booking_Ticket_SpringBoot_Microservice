package com.ducbn.authService.service;

import com.ducbn.authService.dto.UserDTO;
import com.ducbn.authService.model.User;

public interface IAuthService {
     User register(UserDTO userDTO);
     String login(String email, String password, Long roleId) throws Exception;
}
