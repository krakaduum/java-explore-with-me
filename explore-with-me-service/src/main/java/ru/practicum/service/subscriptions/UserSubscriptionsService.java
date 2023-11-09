package ru.practicum.service.subscriptions;

import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.UserShortDto;

import java.util.Collection;

public interface UserSubscriptionsService {
    Collection<UserShortDto> getUserSubscriptions(Long userId);

    Collection<EventShortDto> getUserSubscriptionEvents(Long userId, Long subscriptionId);

    void addSubscription(Long userId, Long subscriptionId);

    void delete(Long userId, Long subscriptionId);
}
