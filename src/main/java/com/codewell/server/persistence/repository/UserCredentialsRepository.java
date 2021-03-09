package com.codewell.server.persistence.repository;

import com.codewell.server.persistence.entity.UserCredentialsEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredentialsRepository extends BaseJpaRepository<UserCredentialsEntity, String>
{
    UserCredentialsEntity selectByUsername(final String username);
}
