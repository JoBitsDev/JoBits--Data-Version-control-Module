package org.jobits.db.utils;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import com.root101.clean.core.app.repo.CRUDRepository;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jobits.db.pool.ConnectionPoolService;

/**
 * FirstDream
 *
 * @author Jorge
 * @param <Entity> entity object
 * @param <Entity>
 *
 */
public abstract class EntityJpaCRUDRepository<Entity> implements CRUDRepository<Entity> {

    protected final Class<Entity> entityClass;
    protected final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    protected ConnectionPoolService connPool;

    public EntityJpaCRUDRepository(ConnectionPoolService connPool, Class<Entity> entityClass) {
        this.entityClass = entityClass;
        this.connPool = connPool;
    }

    public EntityManager getEntityManager() {
        return connPool.getCurrentConnection();
    }

    @Override
    public void startTransaction() {
        if (!getEntityManager().getTransaction().isActive()) {
            getEntityManager().getTransaction().begin();
        }
    }

    @Override
    public void commitTransaction() {
        if (getEntityManager().getTransaction().isActive()) {
            try {
                getEntityManager().flush();
                getEntityManager().getTransaction().commit();
            } catch (PersistenceException e) {
                dbException(e);
            }
        }
    }

    @Override
    public void rollBackTransaction() {
        if (getEntityManager().getTransaction().isActive()) {
            getEntityManager().getTransaction().rollback();
        }
    }

    public void refresh(Entity a) {
        persist(PersistAction.REFRESH, a);
    }

    @Override
    public Entity create(Entity entity) {
        return persist(PersistAction.CREATE, entity);

    }

    @Override
    public Entity edit(Entity entity) {
        return persist(PersistAction.UPDATE, entity);

    }

    @Override
    public Entity destroy(Entity entity) {
        return persist(PersistAction.DELETE, entity);

    }

    @Override
    public Entity destroyById(Object o) throws RuntimeException {
        Entity e = findBy(o);
        return destroy(e);
    }

    @Override
    public Entity findBy(Object id) {
        Entity ret = null;
        try {
            ret = getEntityManager().find(entityClass, id);
        } catch (Exception ex) {
            Logger.getLogger(EntityJpaCRUDRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ret;
    }

    @Override
    public List<Entity> findAll() {
        try {
            javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
            cq.select(cq.from(entityClass));
            return new ArrayList<>(getEntityManager().createQuery(cq).getResultList());
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return new ArrayList<>(findAll());
        }
    }

    public List<Entity> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        try {
            return q.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(EntityJpaCRUDRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    @Override
    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<Entity> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    /**
     * Tu generate IDs for the relational model of the application
     *
     * @param prefix includign the '-' char ej: "P-"
     * @return
     */
    public String generateStringCode(String prefix) {
        int cont = 1;
        Entity a = findBy(prefix + "" + cont);
        while (a != null) {
            cont++;
            a = findBy(prefix + "" + cont);
        }

        return prefix + "" + cont;
    }

    /**
     * Tu generate IDs for the relational model of the application
     *
     * @return
     */
    public int generateIDCode() {
        int cont = 0;
        Entity a = findBy(cont);
        while (a != null) {
            cont++;
        }
        return cont;
    }

    public int generate(String idName) {
        throw new IllegalStateException("Bad Call");
        //return new ConfigDAO().generateNewId(idName);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    public Class<Entity> getEntityClass() {
        return entityClass;
    }

    //
    // Private Methods
    //
    private Entity persist(PersistAction persistAction, Entity entity) {
        try {
            switch (persistAction) {
                case CREATE:
                    getEntityManager().persist(entity);
                    firePropertyChange("CREATE", null, entity);
                    break;
                case DELETE:
                    getEntityManager().remove(getEntityManager().merge(entity));
                    firePropertyChange("DELETE", entity, null);
                    break;
                case UPDATE:
                    getEntityManager().merge(entity);
                    firePropertyChange("UPDATE", null, entity);
                    break;
                case REFRESH:
                    getEntityManager().refresh(entity);
                    firePropertyChange("REFRESH", null, entity);
                    break;
            }
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            dbException(e);
        }
        return null;
    }

    private void dbException(Exception e) {
        if (getEntityManager().getTransaction().isActive()) {
            getEntityManager().getTransaction().rollback();
            getEntityManager().getEntityManagerFactory().getCache().evictAll();
            getEntityManager().clear();
        }
        throw new PersistenceException(
                "Error en base de datos. Reconectandose... \n " + e.getLocalizedMessage());
    }

    //
    // Enum for persist action
    //
    public static enum PersistAction {
        CREATE,
        DELETE,
        DELETE_BY_ID,
        UPDATE,
        REFRESH
    }

}
