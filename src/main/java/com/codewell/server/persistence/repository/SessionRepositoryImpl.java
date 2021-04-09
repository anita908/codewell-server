package com.codewell.server.persistence.repository;

import com.codewell.server.persistence.entity.SessionEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SessionRepositoryImpl extends BaseJpaRepositoryImpl<SessionEntity, Integer> implements SessionRepository
{
    private static final String SELECT_BY_TEACHER_ID = "SELECT s FROM SessionEntity s WHERE s.teacherId = :teacherId";

    public List<SessionEntity> selectByTeacherId(final String teacherId)
    {
        return this.getEntityManager().createQuery(SELECT_BY_TEACHER_ID, SessionEntity.class)
            .setParameter("teacherId", teacherId)
            .getResultList();
    }
}
