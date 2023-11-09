package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.UserSubscriptions;

import java.util.Collection;
import java.util.Optional;

public interface UserSubscriptionsRepository extends JpaRepository<UserSubscriptions, Long> {
    Collection<UserSubscriptions> findAllByUserId(Long userId);

    Optional<UserSubscriptions> findByUserIdAndSubscriptionId(Long userId, Long subscriptionId);

    void deleteByUserIdAndSubscriptionId(Long userId, Long subscriptionId);
}
