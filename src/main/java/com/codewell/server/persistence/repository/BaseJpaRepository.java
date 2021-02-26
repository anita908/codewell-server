package com.codewell.server.persistence.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseJpaRepository<T, P extends Serializable>
{
    void insert(T var1);
    T selectMain(P var1);
    T select(P var1);
    T select(P var1, String... var2);
    List<T> selectAll();
    T update(T var1);
    void delete(T var1);
    void delete(P var1);
    Long countAll();
    Long countAllMain();
    Object getIdentifier(Object var1);
    T getReference(P var1);
    Map<String, Object> createEagerLoadHintMap(String... var1);
    void flush();
    SessionFactory getSessionFactory();
    SessionFactory getReadOnlySessionFactory();
}