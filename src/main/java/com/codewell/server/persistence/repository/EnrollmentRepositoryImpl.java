package com.codewell.server.persistence.repository;

import com.codewell.server.persistence.entity.EnrollmentEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EnrollmentRepositoryImpl extends BaseJpaRepositoryImpl<EnrollmentEntity, Integer> implements EnrollmentRepository
{
    private static final String SELECT_BY_USER_ID = "SELECT e FROM EnrollmentEntity e WHERE e.userId = :userId";

    @Override
    public List<EnrollmentEntity> selectByUserId(final String userId)
    {
        return this.getEntityManager().createQuery(SELECT_BY_USER_ID, EnrollmentEntity.class)
            .setParameter("userId", userId)
            .getResultList();
    }
}
