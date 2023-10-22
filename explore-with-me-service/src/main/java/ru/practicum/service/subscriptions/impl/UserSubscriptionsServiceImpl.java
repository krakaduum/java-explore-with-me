package ru.practicum.service.subscriptions.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.UserShortDto;
import ru.practicum.exception.InvalidParamException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.UserSubscriptions;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.repository.UserSubscriptionsRepository;
import ru.practicum.service.subscriptions.UserSubscriptionsService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserSubscriptionsServiceImpl implements UserSubscriptionsService {
    private final UserSubscriptionsRepository userSubscriptionsRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public Collection<UserShortDto> getUserSubscriptions(Long userId) {
        var user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new NoSuchElementException("Пользователя " + userId + " не существует");
        }

        return userSubscriptionsRepository.findAllByUserId(userId)
                .stream()
                .map(UserSubscriptions::getSubscription)
                .map(UserMapper::toUserShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<EventShortDto> getUserSubscriptionEvents(Long userId, Long subscriptionId) {
        Optional<UserSubscriptions> user = userSubscriptionsRepository.findByUserIdAndSubscriptionId(userId, subscriptionId);
        if (user.isEmpty()) {
            throw new NoSuchElementException("Не найдено подписки " + subscriptionId + " у пользователя " + userId);
        }

        return eventRepository.findEventsByInitiatorId(subscriptionId)
                .stream()
                .map(EventMapper::toEventShortDto)
                .filter(event -> event.getEventDate().isAfter(LocalDate.now().atStartOfDay()))
                .collect(Collectors.toList());
    }

    @Override
    public void addSubscription(Long userId, Long subscriptionId) {
        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NoSuchElementException("Пользователя " + userId + " не существует");
        }

        var subscription = userRepository.findById(subscriptionId);
        if (subscription.isEmpty()) {
            throw new NoSuchElementException("Пользователя " + subscriptionId + " не существует");
        }

        Optional<UserSubscriptions> userSubscription = userSubscriptionsRepository
                .findByUserIdAndSubscriptionId(userId, subscriptionId);
        if (userSubscription.isPresent()) {
            throw new InvalidParamException(
                    "Пользователь " + userId + " уже подписан на пользователя " + subscriptionId);
        }

        userSubscriptionsRepository.save(new UserSubscriptions(user.get(), subscription.get()));
    }

    @Override
    @Transactional
    public void delete(Long userId, Long subscriptionId) {
        Optional<UserSubscriptions> user = userSubscriptionsRepository.findByUserIdAndSubscriptionId(userId, subscriptionId);
        if (user.isEmpty()) {
            throw new NoSuchElementException("Не найдено подписки " + subscriptionId + " у пользователя " + userId);
        }

        userSubscriptionsRepository.deleteByUserIdAndSubscriptionId(userId, subscriptionId);
    }
}
