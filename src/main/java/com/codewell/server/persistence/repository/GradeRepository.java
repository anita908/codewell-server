package com.codewell.server.persistence.repository;

import com.codewell.server.persistence.entity.GradeEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends BaseJpaRepository<GradeEntity, Integer>
{
    List<GradeEntity> selectByUserId(final String userId);
    List<GradeEntity> selectByUserAndSession(final String userId, final int sessionId);
    GradeEntity selectByUserSessionAndHomeworkId(final String userId, final int sessionId, final int homeworkId);
}
