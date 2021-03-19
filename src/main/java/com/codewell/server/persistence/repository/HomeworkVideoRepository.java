package com.codewell.server.persistence.repository;

import com.codewell.server.persistence.entity.HomeworkVideoEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeworkVideoRepository extends BaseJpaRepository<HomeworkVideoEntity, Integer>
{
    List<HomeworkVideoEntity> selectByHomeworkId(final int homeworkId);
    List<HomeworkVideoEntity> selectByHomeworkIds(final List<Integer> homeworkIds);
}
