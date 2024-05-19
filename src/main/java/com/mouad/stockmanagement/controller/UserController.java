package com.mouad.stockmanagement.controller;

import com.mouad.stockmanagement.controller.api.UserApi;
import com.mouad.stockmanagement.dto.ChangeUserPasswordDto;
import com.mouad.stockmanagement.dto.UserDto;
import com.mouad.stockmanagement.services.UserService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserApi {

    private UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDto save(UserDto dto) {
        return userService.save(dto);
    }

    @Override
    public UserDto changerMotDePasse(ChangeUserPasswordDto dto) {
        return userService.changeUserPasswor(dto);
    }

    @Override
    public UserDto findById(Integer id) {
        return userService.findById(id);
    }

    @Override
    public UserDto findByEmail(String email) {
        return userService.findByEmail(email);
    }

    @Override
    public List<UserDto> findAll() {
        return userService.findAll();
    }

    @Override
    public void delete(Integer id) {
        userService.delete(id);
    }
}
