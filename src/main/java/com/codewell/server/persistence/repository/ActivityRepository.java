package com.codewell.server.persistence.repository;

import com.codewell.server.persistence.entity.ActivityEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends BaseJpaRepository<ActivityEntity, Integer>
{
    List<ActivityEntity> selectByCourseId(final int courseId);
}
