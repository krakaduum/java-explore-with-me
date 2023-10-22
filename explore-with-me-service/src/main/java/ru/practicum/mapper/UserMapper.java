package ru.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.UserDto;
import ru.practicum.dto.UserShortDto;
import ru.practicum.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail());
    }

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName());
    }

    public static User toUser(NewUserRequest userRequest) {
        return new User(
                userRequest.getName(),
                userRequest.getEmail());
    }

    public static User toUser(UserShortDto userShortDto) {
        return new User(
                userShortDto.getId(),
                userShortDto.getName()
        );
    }
}
