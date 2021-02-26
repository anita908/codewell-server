package com.codewell.server.persistence.repository;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.Lob;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(transactionManager = "readOnlyTransactionManager")
public abstract class BaseJpaRepositoryImpl<T, P extends Serializable> implements BaseJpaRepository<T, P>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseJpaRepositoryImpl.class);

    protected final Class<T> entityClass;
    private final Map<Field, Integer> fieldLengths = new HashMap();

    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    public BaseJpaRepositoryImpl()
    {
        ParameterizedType genericSuperclass;
        try
        {
            genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        } catch (ClassCastException var8)
        {
            genericSuperclass = (ParameterizedType) this.getClass().getSuperclass().getGenericSuperclass();
        }
        this.entityClass = (Class) genericSuperclass.getActualTypeArguments()[0];
        Field[] var2 = this.entityClass.getDeclaredFields();
        int var3 = var2.length;
        for (int var4 = 0; var4 < var3; ++var4) {
            Field field = var2[var4];
            Column annotation = (Column)field.getAnnotation(Column.class);
            Lob lob = (Lob)field.getAnnotation(Lob.class);
            if (lob == null && annotation != null && annotation.length() != 255 && annotation.length() > 0 && field.getType().equals(String.class)) {
                field.setAccessible(true);
                this.fieldLengths.put(field, annotation.length());
            }
        }
    }

    @Transactional(transactionManager = "transactionManager")
    public void insert(T entity) {
        this.prepareForUpdate(entity);
        this.getEntityManager().persist(entity);
    }

    @Transactional(transactionManager = "transactionManager")
    public T selectMain(P id)
    {
        return this.getEntityManager().find(this.entityClass, id);
    }

    public T select(P id)
    {
        return this.getEntityManager().find(this.entityClass, id);
    }

    public T select(P id, String... eagerLoadColumns)
    {
        return this.getEntityManager().find(this.entityClass, id, this.createEagerLoadHintMap(eagerLoadColumns));
    }

    @Transactional(transactionManager = "transactionManager")
    public T update(T entity)
    {
        this.prepareForUpdate(entity);
        return this.getEntityManager().merge(entity);
    }

    @Transactional(transactionManager = "transactionManager")
    public void delete(T entity)
    {
        T entityToRemove = this.getEntityManager().merge(entity);
        this.getEntityManager().remove(entityToRemove);
    }

    @Transactional(transactionManager = "transactionManager")
    public void delete(P id)
    {
        T entityToRemove = this.getReference(id);
        this.getEntityManager().remove(entityToRemove);
    }

    public List<T> selectAll()
    {
        CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(this.entityClass);
        Root<T> rootEntry = cq.from(this.entityClass);
        CriteriaQuery<T> all = cq.select(rootEntry);
        TypedQuery<T> allQuery = this.getEntityManager().createQuery(all);
        return allQuery.getResultList();
    }
    public Long countAll()
    {
        CriteriaBuilder qb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        cq.select(qb.count(cq.from(this.entityClass)));
        return (Long)this.getEntityManager().createQuery(cq).getSingleResult();
    }

    @Transactional(transactionManager = "transactionManager")
    public Long countAllMain()
    {
        CriteriaBuilder qb = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        cq.select(qb.count(cq.from(this.entityClass)));
        return (Long)this.getEntityManager().createQuery(cq).getSingleResult();
    }

    public Object getIdentifier(Object object)
    {
        return this.getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(object);
    }

    public Map<String, Object> createEagerLoadHintMap(String... entityNames)
    {
        EntityGraph<T> graph = this.getEntityManager().createEntityGraph(this.entityClass);
        String[] var3 = entityNames;
        int var4 = entityNames.length;
        for (int var5 = 0; var5 < var4; ++var5)
        {
            String string = var3[var5];
            graph.addSubgraph(string);
        }
        Map<String, Object> hints = new HashMap();
        hints.put("javax.persistence.loadgraph", graph);
        return hints;
    }

    private void prepareForUpdate(T entity)
    {
        this.fieldLengths.forEach((field, length) ->
        {
            try
            {
                String value = (String)field.get(entity);
                if (value != null && value.length() > length)
                {
                    field.set(entity, value.substring(0, length));
                }
            }
            catch (IllegalAccessException var4)
            {
                LOGGER.warn("Could not set value of field {} on {}.", field.getName(), entity.getClass().getSimpleName(), var4);
            }
        });
    }
    protected EntityManager getEntityManager()
    {
        return this.entityManager;
    }

    public SessionFactory getSessionFactory()
    {
        return (SessionFactory) this.entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
    }

    public SessionFactory getReadOnlySessionFactory()
    {
        return (SessionFactory) this.entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
    }

    public T getReference(P id)
    {
        return this.getEntityManager().getReference(this.entityClass, id);
    }

    public void flush()
    {
        this.getEntityManager().flush();
    }
}