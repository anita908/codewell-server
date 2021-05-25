package com.codewell.server.persistence.repository;

import com.codewell.server.persistence.entity.RecordingEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RecordingRepositoryImpl extends BaseJpaRepositoryImpl<RecordingEntity, Integer> implements RecordingRepository
{
    private static final String SELECT_BY_SESSION_ID = "SELECT r FROM RecordingEntity r WHERE r.sessionId = :sessionId";

    public List<RecordingEntity> selectBySessionId(final Integer sessionId)
    {
        return this.getEntityManager().createQuery(SELECT_BY_SESSION_ID, RecordingEntity.class)
            .setParameter("sessionId", sessionId)
            .getResultList();
    }
}
