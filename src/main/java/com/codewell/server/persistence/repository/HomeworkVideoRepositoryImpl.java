package com.codewell.server.persistence.repository;

import com.codewell.server.persistence.entity.HomeworkVideoEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HomeworkVideoRepositoryImpl extends BaseJpaRepositoryImpl<HomeworkVideoEntity, Integer> implements HomeworkVideoRepository
{
    private static final String SELECT_BY_HOMEWORK_ID = "SELECT h FROM HomeworkVideoEntity h WHERE h.homeworkId = :homeworkId";
    private static final String SELECT_BY_HOMEWORK_IDS = "SELECT h FROM HomeworkVideoEntity h WHERE h.homeworkId IN :homeworkIds";

    @Override
    public List<HomeworkVideoEntity> selectByHomeworkIds(final List<Integer> homeworkIds)
    {
        return this.getEntityManager().createQuery(SELECT_BY_HOMEWORK_IDS, HomeworkVideoEntity.class)
            .setParameter("homeworkIds", homeworkIds)
            .getResultList();
    }

    @Override
    public List<HomeworkVideoEntity> selectByHomeworkId(final int homeworkId)
    {
        return this.getEntityManager().createQuery(SELECT_BY_HOMEWORK_ID, HomeworkVideoEntity.class)
            .setParameter("homeworkId", homeworkId)
            .getResultList();
    }
}
