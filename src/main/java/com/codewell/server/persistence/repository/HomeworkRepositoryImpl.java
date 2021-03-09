package com.codewell.server.persistence.repository;

import com.codewell.server.persistence.entity.HomeworkEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HomeworkRepositoryImpl extends BaseJpaRepositoryImpl<HomeworkEntity, Integer> implements HomeworkRepository
{
    private static final String SELECT_BY_COURSE_ID = "SELECT h FROM HomeworkEntity h WHERE h.courseId = :courseId";

    @Override
    public List<HomeworkEntity> selectByCourseId(int courseId)
    {
        return this.getEntityManager().createQuery(SELECT_BY_COURSE_ID, HomeworkEntity.class)
            .setParameter("courseId", courseId)
            .getResultList();
    }
}
