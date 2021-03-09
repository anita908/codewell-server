package com.codewell.server.persistence.repository;

import com.codewell.server.persistence.entity.ChapterEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterRepository extends BaseJpaRepository<ChapterEntity, Integer>
{
    List<ChapterEntity> selectByCourseId(final int courseId);
}
