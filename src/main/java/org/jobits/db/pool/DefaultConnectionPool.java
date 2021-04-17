/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jobits.db.pool;

import org.jobits.db.core.domain.UbicacionConexionModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import com.root101.clean.core.domain.services.ResourceHandler;
import javax.persistence.EntityManager;
import org.jboss.logging.Logger;

/**
 * El ConnectionPool tiene como responsabilidad crer los entityManagerFactory y
 * EntityManager. Mantiene las transacciones a nivel de aplicacion
 *
 * @author Jorge
 */
public class DefaultConnectionPool implements ConnectionPoolService {

    public static ConnectionPoolService createPoolService(String persistenceUnitName) {
        return new DefaultConnectionPool(persistenceUnitName);
    }

    private final String DRIVER = "javax.persistence.jdbc.driver";

    private EntityManagerFactory EMF;
    private final String PASSWORD = "javax.persistence.jdbc.password";
    private final String URL = "javax.persistence.jdbc.url";
    private final String USER = "javax.persistence.jdbc.user";
    
    private final String START_UP_CHECK = "hibernate.query.startup_check";//FALSE
    private final String VALIDATION_MODE = "javax.persistence.validation.mode";//NONE
    private final String AUTOREGISTER_LISTENERS = "hibernate.validator.autoregister_listeners";//FALSE
    private final String APPLY_TO_DDL = "hibernate.validator.apply_to_ddl";//FALSE
    
    private List<EntityManagerFactoryCache> cachedEmf = new ArrayList<>();
    private boolean connected = false;

    private EntityManager currentConnection;
    private UbicacionConexionModel currentUbicacion;

    private String persistenceUnitName;

    private DefaultConnectionPool(UbicacionConexionModel connectionProperties) {
        init();
    }

    private DefaultConnectionPool(String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
    }

    @Override
    public EntityManager getCurrentConnection() {
        init();
        return currentConnection;
    }

    @Override
    public UbicacionConexionModel getCurrentUbicacion() {
        return currentUbicacion;
    }

    @Override
    public EntityManagerFactory getEMF() {
        init();
        return EMF;
    }

    @Override
    public EntityManagerFactory getEmfFrom(UbicacionConexionModel connectionsProperties) {
        for (EntityManagerFactoryCache cache : cachedEmf) {
            if (cache.getUbicaicon().equals(connectionsProperties)) {
                return cache.getFactory();
            }
        }
        EntityManagerFactory newFactory = Persistence.createEntityManagerFactory(persistenceUnitName, getConnectionsPropeties(connectionsProperties));
        EntityManagerFactoryCache cacheItem = new EntityManagerFactoryCache(newFactory, connectionsProperties);
        cachedEmf.add(cacheItem);
        return newFactory;

    }

    @Override
    public boolean isConnected() {
        init();
        return connected;
    }

    @Override
    public void resetConnection() {
        if (currentConnection != null) {
            currentConnection.close();
            currentConnection = EMF.createEntityManager();
        }
    }

    private HashMap<String, String> getConnectionsPropeties(UbicacionConexionModel connectionProperties) {
        HashMap<String, String> prop = new HashMap<>();
        prop.put(URL, connectionProperties.getUrl());
        prop.put(USER, connectionProperties.getUsuario());
        prop.put(PASSWORD, connectionProperties.getContrasena());
        prop.put(DRIVER, connectionProperties.getDriver());
        prop.put(VALIDATION_MODE, "none");
        return prop;
    }

    private void init() {
        UbicacionConexionModel oldModel = currentUbicacion;
        setCurrentUbicacion();
        if (oldModel != null && currentConnection != null) {
            if (oldModel.equals(currentUbicacion)) {
                return;
            }
        }
        EMF = getEmfFrom(currentUbicacion);
        // currentUbicacion = connectionProperties;
        if (EMF != null) {
            initConnections();

        } else {
            throw new NullPointerException(
                    ResourceHandler.getString("msg.com.jobits.pos.null_pointer_EMF_not_Found"));
        }
    }

    private boolean initConnections() {
        try {
            //EMF.getCache().evictAll();
            currentConnection = EMF.createEntityManager();
            connected = true;
        } catch (Exception e) {
            Logger.getLogger(DefaultConnectionPool.class).log(Logger.Level.ERROR, e.getMessage());
            connected = false;
        }
        return connected;
    }

    private void setCurrentUbicacion() {
        String nombreUbicacion = ResourceHandler.getString("com.jobits.pos.db.current_conn_name");
        String url = ResourceHandler.getString("com.jobits.pos.db.current_conn_url");
        String user = ResourceHandler.getString("com.jobits.pos.db.current_conn_user");
        String pass = ResourceHandler.getString("com.jobits.pos.db.current_conn_pass");
        String driver = ResourceHandler.getString("com.jobits.pos.db.current_conn_driver");
        String tipoUbicacion = ResourceHandler.getString("com.jobits.pos.db.current_conn_tipo");
        UbicacionConexionModel model = UbicacionConexionModel.from(nombreUbicacion, url,
                user, pass, driver,
                UbicacionConexionModel.TipoUbicacion.valueOf(tipoUbicacion));
        currentUbicacion = model;
    }

    private class EntityManagerFactoryCache {

        private final EntityManagerFactory factory;
        private final UbicacionConexionModel ubicaicon;

        public EntityManagerFactoryCache(EntityManagerFactory factory, UbicacionConexionModel ubicaicon) {
            this.factory = factory;
            this.ubicaicon = ubicaicon;
        }

        public EntityManagerFactory getFactory() {
            return factory;
        }

        public UbicacionConexionModel getUbicaicon() {
            return ubicaicon;
        }

    }

}
