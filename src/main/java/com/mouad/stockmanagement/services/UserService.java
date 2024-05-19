package com.mouad.stockmanagement.services;

import com.mouad.stockmanagement.dto.ChangeUserPasswordDto;
import com.mouad.stockmanagement.dto.UserDto;
import java.util.List;

public interface UserService {
    UserDto save(UserDto dto);
    UserDto findById(Integer id);
    List<UserDto> findAll();
    void delete(Integer id);
    UserDto findByEmail(String email);
    UserDto changeUserPasswor(ChangeUserPasswordDto dto);
}
