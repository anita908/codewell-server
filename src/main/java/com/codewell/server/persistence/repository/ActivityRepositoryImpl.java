package com.codewell.server.persistence.repository;

import com.codewell.server.persistence.entity.ActivityEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ActivityRepositoryImpl extends BaseJpaRepositoryImpl<ActivityEntity, Integer> implements ActivityRepository
{
    private static final String SELECT_BY_COURSE_ID = "SELECT a FROM ActivityEntity a WHERE a.courseId = :courseId";

    @Override
    public List<ActivityEntity> selectByCourseId(final int courseId)
    {
        return this.getEntityManager().createQuery(SELECT_BY_COURSE_ID, ActivityEntity.class)
            .setParameter("courseId", courseId)
            .getResultList();
    }
}
