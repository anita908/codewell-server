package com.codewell.server.persistence.repository;

import com.codewell.server.persistence.entity.EnrollmentEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EnrollmentRepositoryImpl extends BaseJpaRepositoryImpl<EnrollmentEntity, Integer> implements EnrollmentRepository
{
    private static final String SELECT_BY_USER_ID = "SELECT e FROM EnrollmentEntity e WHERE e.userId = :userId";
    private static final String SELECT_BY_SESSION_ID = "SELECT e FROM EnrollmentEntity e WHERE e.session.id = :sessionId";
    private static final String SELECT_BY_USER_AND_SESSION = "SELECT e FROM EnrollmentEntity e " +
        "WHERE e.userId = :userId AND e.session.id = :sessionId";

    @Override
    public List<EnrollmentEntity> selectByUserId(final String userId)
    {
        return this.getEntityManager().createQuery(SELECT_BY_USER_ID, EnrollmentEntity.class)
            .setParameter("userId", userId)
            .getResultList();
    }

    @Override
    public List<EnrollmentEntity> selectBySessionId(final int sessionId)
    {
        return this.getEntityManager().createQuery(SELECT_BY_SESSION_ID, EnrollmentEntity.class)
            .setParameter("sessionId", sessionId)
            .getResultList();
    }

    @Override
    public EnrollmentEntity selectByUserAndSession(final String userId, final int sessionId)
    {
        return this.getEntityManager().createQuery(SELECT_BY_USER_AND_SESSION, EnrollmentEntity.class)
            .setParameter("userId", userId)
            .setParameter("sessionId", sessionId)
            .getResultStream()
            .findFirst()
            .orElse(null);
    }
}
