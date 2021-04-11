package com.codewell.server.persistence.repository;

import com.codewell.server.persistence.entity.GradeEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GradeRepositoryImpl extends BaseJpaRepositoryImpl<GradeEntity, Integer> implements GradeRepository
{
    private static final String SELECT_BY_USER_ID = "SELECT g FROM GradeEntity g WHERE g.userId = :userId";
    private static final String SELECT_BY_USER_AND_SESSION = "SELECT g FROM GradeEntity g " +
        "WHERE g.userId = :userId " +
        "AND g.sessionId = :sessionId " +
        "ORDER BY g.homework.chapterNo";
    private static final String SELECT_BY_USER_SESSION_AND_HOMEWORK = "SELECT g FROM GradeEntity g " +
        "WHERE g.userId = :userId " +
        "AND g.sessionId = :sessionId " +
        "AND g.homework.id = :homeworkId";

    @Override
    public List<GradeEntity> selectByUserId(final String userId)
    {
        return this.getEntityManager().createQuery(SELECT_BY_USER_ID, GradeEntity.class)
            .setParameter("userId", userId)
            .getResultList();
    }

    @Override
    public List<GradeEntity> selectByUserAndSession(final String userId, final int sessionId)
    {
        return this.getEntityManager().createQuery(SELECT_BY_USER_AND_SESSION, GradeEntity.class)
            .setParameter("sessionId", sessionId)
            .setParameter("userId", userId)
            .getResultList();
    }

    @Override
    public GradeEntity selectByUserSessionAndHomeworkId(final String userId, final int sessionId, final int homeworkId)
    {
        return this.getEntityManager().createQuery(SELECT_BY_USER_SESSION_AND_HOMEWORK, GradeEntity.class)
            .setParameter("userId", userId)
            .setParameter("sessionId", sessionId)
            .setParameter("homeworkId", homeworkId)
            .getResultStream()
            .findFirst()
            .orElse(null);
    }
}
