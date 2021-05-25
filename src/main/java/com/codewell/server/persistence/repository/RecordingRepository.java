package com.codewell.server.persistence.repository;

import com.codewell.server.persistence.entity.RecordingEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordingRepository extends BaseJpaRepository<RecordingEntity, Integer>
{
    List<RecordingEntity> selectBySessionId(final Integer sessionId);
}
