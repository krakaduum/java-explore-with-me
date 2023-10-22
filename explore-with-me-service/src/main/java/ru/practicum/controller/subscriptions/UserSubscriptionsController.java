package ru.practicum.controller.subscriptions;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.UserShortDto;
import ru.practicum.service.subscriptions.UserSubscriptionsService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserSubscriptionsController {
    private final UserSubscriptionsService userSubscriptionsService;

    @GetMapping(path = "/{userId}/subscriptions")
    Collection<UserShortDto> getUserSubscriptions(@PathVariable Long userId) {
        return userSubscriptionsService.getUserSubscriptions(userId);
    }

    @GetMapping(path = "/{userId}/subscriptions/{subscriptionId}/events")
    Collection<EventShortDto> getUserSubscriptionEvents(@PathVariable Long userId, @PathVariable Long subscriptionId) {
        return userSubscriptionsService.getUserSubscriptionEvents(userId, subscriptionId);
    }

    @PostMapping(path = "/{userId}/subscriptions/{subscriptionId}")
    @ResponseStatus(HttpStatus.CREATED)
    void addSubscription(@PathVariable Long userId, @PathVariable Long subscriptionId) {
        userSubscriptionsService.addSubscription(userId, subscriptionId);
    }

    @DeleteMapping(path = "/{userId}/subscriptions/{subscriptionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Long userId, @PathVariable Long subscriptionId) {
        userSubscriptionsService.delete(userId, subscriptionId);
    }
}
