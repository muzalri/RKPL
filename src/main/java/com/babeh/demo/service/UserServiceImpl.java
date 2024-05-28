package com.babeh.demo.service;

import com.babeh.demo.dto.UserDto;
import com.babeh.demo.model.Role;
import com.babeh.demo.model.User;
import com.babeh.demo.repository.RoleRepository;
import com.babeh.demo.repository.UserRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = convertDtoToEntity(userDto);

        Long roleId = userDto.getRoleId();
        if (roleId != null) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
            user.setRoles(Collections.singletonList(role));
        } else {
            Role defaultRole = roleRepository.findByName("ROLE_ADMIN");
            if (defaultRole == null) {
                defaultRole = checkRoleExist();
            }
            user.setRoles(Collections.singletonList(defaultRole));
        }

        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();

        for (User user : users) {
            UserDto userDto = convertEntityToDto(user);
            if (!user.getRoles().isEmpty()) {
                Role role = user.getRoles().get(0);
                userDto.setRoleId(role.getId());
                userDto.setRolename(role.getName());
            }
            userDtos.add(userDto);
        }

        return userDtos;
    }

    private UserDto convertEntityToDto(User user) {
        UserDto userDto = new UserDto();
        String[] name = user.getName().split(" ");
        userDto.setFirstName(name[0]);
        if (name.length > 1) {
            userDto.setLastName(name[1]);
        }
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword()); // Password tidak sebaiknya dikirimkan ke klien.
        return userDto;
    }

    private User convertDtoToEntity(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return user;
    }

    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }

    @Override
    public UserDto findUserDtoByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return null;
        }

        UserDto userDto = convertEntityToDto(user);
        if (!user.getRoles().isEmpty()) {
            Role role = user.getRoles().get(0);
            userDto.setRoleId(role.getId());
            userDto.setRolename(role.getName());
        }

        return userDto;
    }

    @Override
    public void updateUser(UserDto userDto, String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("User not found with username: " + username);
        }

        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Long roleId = userDto.getRoleId();
        if (roleId != null) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
            user.getRoles().clear();
            user.getRoles().add(role);
        } else {
            Role defaultRole = roleRepository.findByName("ROLE_ADMIN");
            if (defaultRole == null) {
                defaultRole = checkRoleExist();
            }
            user.getRoles().clear();
            user.getRoles().add(defaultRole);
        }

        userRepository.save(user);
    }

   @Override
    public void deleteUserByUsername(String username) {
    try {
        User user = userRepository.findByUsername(username);

        if (user != null) {
            user.getRoles().clear();
            userRepository.delete(user);
        } else {
            throw new RuntimeException("User not found with username: " + username);
        }
    } catch (DataIntegrityViolationException e) {
        throw new RuntimeException("Unable to delete user due to constraint violation: " + e.getMessage(), e);
    }
}

}
