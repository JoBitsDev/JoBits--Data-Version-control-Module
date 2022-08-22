/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jobits.db.versioncontrol;

import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.flywaydb.core.Flyway;
import org.jobits.db.core.domain.ConexionPropertiesModel;
import org.jobits.db.pool.ConnectionPoolHandler;
import org.jobits.db.pool.ConnectionPoolService;

/**
 *
 * JoBits
 *
 * @author Jorge
 *
 */
public class DataVersionControlHandler {

    private static final Map<String, DataVersionControlService> registeredServices = new HashMap<>();
    private static final Map<String, Boolean> updatedDataBases = new HashMap<>();
    public static final PropertyChangeListener ubicacionChangeListener = (evt) -> {
        updateAllDataVersionControl();
    };

    public static void registerDataVersionControlService(DataVersionControlService service) {
        if (!registeredServices.containsKey(service.getModuleName())) {
            registeredServices.put(service.getModuleName(), service);
            updateDataVersionControl(service.getModuleName());
        }
    }

    public static void updateAllDataVersionControl() {
        registeredServices.values().forEach(service -> {
            updateDB(service);
        });
    }

    public static void updateDataVersionControl(String moduleName) {
        if (registeredServices.containsKey(moduleName)) {
            updateDB(registeredServices.get(moduleName));
        }
    }

    private static void updateDB(DataVersionControlService service) {
        if (updatedDataBases.getOrDefault(service.getModuleName(),false)) {
                return;
        }
        ConnectionPoolService pool = ConnectionPoolHandler.getConnectionPoolService(service.getModuleName());
        if (pool == null || pool.getCurrentUbicacion() == null) {
            return;
        }
        String url = pool.getCurrentUbicacion().getUrl();
        String user = pool.getCurrentUbicacion().getUsuario();
        String pass = pool.getCurrentUbicacion().getContrasena();
        Flyway flyWay = Flyway.configure()
                .dataSource(url, user, pass)
                .locations(service.getVersionControlPath())
                .createSchemas(true)
                .schemas(service.getDataSchema())
                .load();
        try {
            if (flyWay.info().pending().length > 0) {
                flyWay.repair();
                flyWay.migrate();
            }
            updatedDataBases.put(service.getModuleName(), true);
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.WARNING, ex.getMessage());
        }

    }

}
