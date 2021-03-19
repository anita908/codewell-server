package com.codewell.server.persistence.repository;

import com.codewell.server.persistence.entity.HomeworkEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HomeworkRepositoryImpl extends BaseJpaRepositoryImpl<HomeworkEntity, Integer> implements HomeworkRepository
{
    private static final String SELECT_BY_COURSE_ID = "SELECT h FROM HomeworkEntity h WHERE h.courseId = :courseId";
    private static final String SELECT_BY_COURSE_ID_AND_CHAPTER_NO = "SELECT h FROM HomeworkEntity h " +
        "WHERE h.courseId = :courseId AND h.chapterNo = :chapterNo";

    @Override
    public List<HomeworkEntity> selectByCourseId(int courseId)
    {
        return this.getEntityManager().createQuery(SELECT_BY_COURSE_ID, HomeworkEntity.class)
            .setParameter("courseId", courseId)
            .getResultList();
    }

    @Override
    public List<HomeworkEntity> selectByCourseIdAndChapterNo(final int courseId, final int chapterNo)
    {
        return this.getEntityManager().createQuery(SELECT_BY_COURSE_ID_AND_CHAPTER_NO, HomeworkEntity.class)
            .setParameter("courseId", courseId)
            .setParameter("chapterNo", chapterNo)
            .getResultList();
    }
}
