package com.codewell.server.persistence.repository;

import com.codewell.server.persistence.entity.EnrollmentEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends BaseJpaRepository<EnrollmentEntity, Integer>
{
    List<EnrollmentEntity> selectByUserId(final String userId);
    EnrollmentEntity selectBySessionAndUser(final int sessionId, final String userId);
}
