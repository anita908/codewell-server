package com.codewell.server.persistence.repository;

import com.codewell.server.persistence.entity.UserCredentialsEntity;
import org.springframework.stereotype.Repository;

@Repository
public class UserCredentialsRepositoryImpl extends BaseJpaRepositoryImpl<UserCredentialsEntity, String> implements UserCredentialsRepository
{
    private static final String SELECT_BY_USERNAME = "SELECT u FROM UserCredentialsEntity u WHERE u.username = :username";

    @Override
    public UserCredentialsEntity selectByUsername(final String username)
    {
        return this.getEntityManager().createQuery(SELECT_BY_USERNAME, UserCredentialsEntity.class)
            .setParameter("username", username)
            .getResultStream()
            .findFirst()
            .orElse(null);
    }
}
