package com.codewell.server.persistence.repository;

import com.codewell.server.persistence.entity.UserTokenEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenRepository extends BaseJpaRepository<UserTokenEntity, String>
{

}
