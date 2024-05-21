package com.mouad.stockmanagement.controller.api;

import static com.mouad.stockmanagement.utils.Constants.USER_ENDPOINT;

import com.mouad.stockmanagement.dto.ChangeUserPasswordDto;
import com.mouad.stockmanagement.dto.UserDto;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserApi {

    @PostMapping(USER_ENDPOINT + "/create")
    UserDto save(@RequestBody UserDto dto);

    @PostMapping(USER_ENDPOINT + "/update/password")
    UserDto changerMotDePasse(@RequestBody ChangeUserPasswordDto dto);

    @GetMapping(USER_ENDPOINT + "/{userId}")
    UserDto findById(@PathVariable("userId") Integer id);

    @GetMapping(USER_ENDPOINT + "/find/{email}")
    UserDto findByEmail(@PathVariable("email") String email);

    @GetMapping(USER_ENDPOINT + "/all")
    List<UserDto> findAll();

    @DeleteMapping(USER_ENDPOINT + "/delete/{userId}")
    void delete(@PathVariable("userId") Integer id);

}
