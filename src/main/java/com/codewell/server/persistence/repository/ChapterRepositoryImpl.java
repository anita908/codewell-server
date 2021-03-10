package com.codewell.server.persistence.repository;

import com.codewell.server.persistence.entity.ChapterEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChapterRepositoryImpl extends BaseJpaRepositoryImpl<ChapterEntity, Integer> implements ChapterRepository
{
    private static final String SELECT_BY_COURSE_ID = "SELECT c FROM ChapterEntity c WHERE c.courseId = :courseId";

    @Override
    public List<ChapterEntity> selectByCourseId(final int courseId)
    {
        return this.getEntityManager().createQuery(SELECT_BY_COURSE_ID, ChapterEntity.class)
            .setParameter("courseId", courseId)
            .getResultList();
    }
}
