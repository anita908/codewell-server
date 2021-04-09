package com.codewell.server.persistence.repository;

import com.codewell.server.persistence.entity.SessionEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends BaseJpaRepository<SessionEntity, Integer>
{
    List<SessionEntity> selectByTeacherId(final String teacherId);
}
