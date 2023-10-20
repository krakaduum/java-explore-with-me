package ru.practicum.controller.admin_api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.UserDto;
import ru.practicum.service.admin_service.AdminUserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "admin/users")
public class AdminUserController {
    private final AdminUserService adminUserService;

    @GetMapping
    Collection<UserDto> getUsers(@RequestParam(required = false) Collection<Long> ids,
                                 @RequestParam(defaultValue = "0") Integer from,
                                 @RequestParam(defaultValue = "10") Integer size) {
        return adminUserService.getUsers(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserDto registerUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        return adminUserService.registerUser(newUserRequest);
    }

    @DeleteMapping(path = "/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable long userId) {
        adminUserService.delete(userId);
    }
}
