package org.jobits.db.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import com.root101.clean.core.app.repo.CRUDRepository;
import com.root101.clean.core.app.repo.Converter;
import com.root101.clean.core.domain.services.ResourceHandler;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jobits.db.pool.ConnectionPoolService;

/**
 * FirstDream
 *
 * @author Jorge
 * @param <Domain> entity object
 * @param <Entity>
 *
 */
public abstract class JpaCRUDRepository<Domain, Entity> implements CRUDRepository<Domain> {

    protected static ObjectMapper mapper = JsonMapper.builder()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .findAndAddModules()
            .build();
    protected final Class<Domain> domainClass;
    protected final Class<Entity> entityClass;
    protected final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    protected ConnectionPoolService connPool;

    public JpaCRUDRepository(ConnectionPoolService connPool, Class<Domain> domainClass, Class<Entity> entityClass) {
        this.domainClass = domainClass;
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

    public void refresh(Domain a) {
        persist(PersistAction.REFRESH, a);
    }

    @Override
    public Domain create(Domain entity) {
        return persist(PersistAction.CREATE, entity);

    }

    @Override
    public Domain edit(Domain entity) {
        return persist(PersistAction.UPDATE, entity);

    }

    @Override
    public Domain destroy(Domain entity) {
        return persist(PersistAction.DELETE, entity);

    }

    @Deprecated
    @Override
    public Domain destroyById(Object o) throws RuntimeException {
        throw new IllegalCallerException("Bad Call: nosotros no trabajamos con este metodo");
    }

    @Override
    public Domain findBy(Object id) {
        Domain ret = null;
        try {
            ret = converter.from(getEntityManager().find(entityClass, id));
        } catch (Exception ex) {
            Logger.getLogger(JpaCRUDRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ret;
    }

    @Override
    public List<Domain> findAll() {
        try {
            javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
            cq.select(cq.from(entityClass));
            return converter.from(new ArrayList<>(getEntityManager().createQuery(cq).getResultList()));
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return new ArrayList<>(findAll());
        }
    }

    public List<Domain> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        try {
            return converter.from(q.getResultList());
        } catch (Exception ex) {
            Logger.getLogger(JpaCRUDRepository.class.getName()).log(Level.SEVERE, null, ex);
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
        Domain a = findBy(prefix + "" + cont);
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
        Domain a = findBy(cont);
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
    private Domain persist(PersistAction persistAction, Domain domain) {
        try {
            Entity entity = converter.to(domain);
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
            return converter.from(entity);
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
        UPDATE,
        REFRESH
    }

    protected Converter<Domain, Entity> converter = new Converter<Domain, Entity>() {
        @Override
        public Domain from(Entity entity) {
            try {
                return (Domain) mapper.readValue(mapper.writeValueAsString(entity), domainClass);
            } catch (JsonProcessingException ex) {
                Logger.getLogger(JpaCRUDRepository.class.getName()).log(Level.SEVERE, null, ex);
                throw new IllegalStateException(ResourceHandler.getString("msg.com.jobits.exception.parsing_exception"));
            }
        }

        @Override
        public Entity to(Domain domain) {
            try {
                return (Entity) mapper.readValue(mapper.writeValueAsString(domain), entityClass);
            } catch (JsonProcessingException ex) {
                Logger.getLogger(JpaCRUDRepository.class.getName()).log(Level.SEVERE, null, ex);
                throw new IllegalStateException(ResourceHandler.getString("msg.com.jobits.exception.parsing_exception"));
            }
        }

        @Override
        public List<Domain> from(List<Entity> list) {
            try {
                List<Domain> ret = Converter.super.from(list); //To change body of generated methods, choose Tools | Templates.
                if (!ret.isEmpty()) {
                    Domain d = ret.get(0);
                    if (d instanceof Comparable) {
                        Collections.sort((List<Comparable>) ret);

                    }
                }
                return ret;
            } catch (Exception ex) {
                Logger.getLogger(JpaCRUDRepository.class.getName()).log(Level.SEVERE, null, ex);
                throw new IllegalStateException(ResourceHandler.getString("msg.com.jobits.exception.parsing_exception"));
            }
        }

    };

}
