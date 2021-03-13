package com.codewell.server.persistence.repository;

import com.codewell.server.persistence.entity.SessionEntity;
import org.springframework.stereotype.Repository;

@Repository
public class SessionRepositoryImpl extends BaseJpaRepositoryImpl<SessionEntity, Integer> implements SessionRepository
{
}
