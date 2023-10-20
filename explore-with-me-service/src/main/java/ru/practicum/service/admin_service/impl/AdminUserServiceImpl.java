package ru.practicum.service.admin_service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.UserDto;
import ru.practicum.exception.InvalidParamException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.admin_service.AdminUserService;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository userRepository;

    @Override
    public Collection<UserDto> getUsers(Collection<Long> ids, int from, int size) {
        if (ids == null) {
            return userRepository.findAll(PageRequest.of(from, size)).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }

        return userRepository.findAllByIdIn(ids, PageRequest.of(from, size))
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserDto registerUser(NewUserRequest newUserRequest) {
        Optional<User> user = userRepository.findByEmail(newUserRequest.getEmail());

        if (user.isPresent()) {
            throw new InvalidParamException("Пользователь с электронным адресом " + newUserRequest.getEmail() +
                    " уже существует");
        }

        return UserMapper
                .toUserDto(userRepository
                        .save(UserMapper.toUser(newUserRequest)));
    }

    @Override
    public void delete(long userId) {
        userRepository.deleteById(userId);
    }
}
