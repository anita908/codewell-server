package com.codewell.server.persistence.repository;

import com.codewell.server.persistence.entity.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl extends BaseJpaRepositoryImpl<UserEntity, Long> implements UserRepository
{
    private static final String SELECT_BY_USER_ID = "SELECT u FROM UserEntity u WHERE u.userId = :userId";
    private static final String SELECT_BY_USERNAME = "SELECT u FROM UserEntity u WHERE u.username = :username";

    @Override
    public UserEntity selectByUserId(final String userId)
    {
        return this.getEntityManager().createQuery(SELECT_BY_USER_ID, UserEntity.class)
            .setParameter("userId", userId)
            .getResultStream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public UserEntity selectByUsername(final String username)
    {
        return this.getEntityManager().createQuery(SELECT_BY_USERNAME, UserEntity.class)
            .setParameter("username", username)
            .getResultStream()
            .findFirst()
            .orElse(null);
    }
}
