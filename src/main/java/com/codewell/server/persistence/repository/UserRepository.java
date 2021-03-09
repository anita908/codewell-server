package com.codewell.server.persistence.repository;

import com.codewell.server.persistence.entity.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseJpaRepository<UserEntity, Long>
{
    UserEntity selectByUserId(final String userId);
}