package com.codewell.server.persistence.repository;

import com.codewell.server.persistence.entity.UserAuthEntity;
import org.springframework.stereotype.Repository;

@Repository
public class UserAuthRepositoryImpl extends BaseJpaRepositoryImpl<UserAuthEntity, String> implements UserAuthRepository
{
}
