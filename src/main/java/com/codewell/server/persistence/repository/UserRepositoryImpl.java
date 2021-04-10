package com.codewell.server.persistence.repository;

import com.codewell.server.persistence.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl extends BaseJpaRepositoryImpl<UserEntity, Long> implements UserRepository
{
    private static final String SELECT_BY_USER_ID = "SELECT u FROM UserEntity u WHERE u.userId = :userId";
    private static final String SELECT_BY_USER_IDS = "SELECT u FROM UserEntity u WHERE u.userId IN :userIds";
    private static final String SELECT_BY_EMAIL = "SELECT u FROM UserEntity u WHERE u.email = :email";

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
    public UserEntity selectByEmail(final String email)
    {
        return this.getEntityManager().createQuery(SELECT_BY_EMAIL, UserEntity.class)
            .setParameter("email", email)
            .getResultStream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<UserEntity> selectByUserIds(final List<String> userIds)
    {
        return this.getEntityManager().createQuery(SELECT_BY_USER_IDS, UserEntity.class)
            .setParameter("userIds", userIds)
            .getResultList();
    }
}
