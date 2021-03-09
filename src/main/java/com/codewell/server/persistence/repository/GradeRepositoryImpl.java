package com.codewell.server.persistence.repository;

import com.codewell.server.persistence.entity.GradeEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GradeRepositoryImpl extends BaseJpaRepositoryImpl<GradeEntity, Integer> implements GradeRepository
{
    private static final String SELECT_BY_SESSION_AND_USER = "SELECT g FROM GradeEntity g WHERE g.sessionId = :sessionId AND g.userId = :userId";

    @Override
    public List<GradeEntity> selectBySessionAndUser(final int sessionId, final String userId)
    {
        return this.getEntityManager().createQuery(SELECT_BY_SESSION_AND_USER, GradeEntity.class)
            .setParameter("sessionId", sessionId)
            .setParameter("userId", userId)
            .getResultList();
    }
}
