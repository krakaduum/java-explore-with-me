package ru.practicum.service.admin_service;

import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.UserDto;

import java.util.Collection;

public interface AdminUserService {
    Collection<UserDto> getUsers(Collection<Long> ids, int from, int size);

    UserDto registerUser(NewUserRequest newUserRequest);

    void delete(long userId);
}
