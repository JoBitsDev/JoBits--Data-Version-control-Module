/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jobits.db.versioncontrol;

import com.root101.clean.core.domain.services.ResourceHandler;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.flywaydb.core.Flyway;

/**
 *
 * JoBits
 *
 * @author Jorge
 *
 */
public class DataVersionControlHandler {

    private static final Map<String, DataVersionControlService> registeredServices = new HashMap<>();
    public static final PropertyChangeListener ubicacionChangeListener = (evt) -> {
        updateAllDataVersionControl();
    };

    public static void registerDataVersionControlService(DataVersionControlService service) {
        if (!registeredServices.containsKey(service.getModuleName())) {
            registeredServices.put(service.getModuleName(), service);
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
        String url = ResourceHandler.getString("com.jobits.pos.db.current_conn_url");
        String user = ResourceHandler.getString("com.jobits.pos.db.current_conn_user");
        String pass = ResourceHandler.getString("com.jobits.pos.db.current_conn_pass");
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
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.WARNING, ex.getMessage());
        }

    }

}
