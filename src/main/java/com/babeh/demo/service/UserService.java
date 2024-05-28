package com.babeh.demo.service;


import com.babeh.demo.dto.UserDto;
import com.babeh.demo.model.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findByUsername(String username);
List<UserDto> findAllUsers();
    
    
    public UserDto findUserDtoByUsername(String username);

    void updateUser(UserDto userDto,String username);
    
    void deleteUserByUsername(String username);
}
