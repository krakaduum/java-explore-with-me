package ru.practicum.service.admin_service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Slf4j
public class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository userRepository;
    private static Logger logger = LoggerFactory.getLogger(AdminUserServiceImpl.class);

    @Override
    public Collection<UserDto> getUsers(Collection<Long> ids, int from, int size) {
        if (ids == null) {
            logger.info("Будет возвращена информация о всех пользователях, так как конкретные идентификаторы не введены");

            return userRepository
                    .findAll(PageRequest.of(from, size))
                    .stream()
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
