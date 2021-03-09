package com.codewell.server.persistence.repository;

import com.codewell.server.persistence.entity.HomeworkEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeworkRepository extends BaseJpaRepository<HomeworkEntity, Integer>
{
    List<HomeworkEntity> selectByCourseId(final int courseId);
}
