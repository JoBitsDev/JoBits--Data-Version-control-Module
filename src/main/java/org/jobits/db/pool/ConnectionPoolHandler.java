/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jobits.db.pool;

import org.jobits.db.pool.impl.DefaultConnectionPool;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * JoBits
 *
 * @author Jorge
 *
 */
public class ConnectionPoolHandler {

    private static Map<String, ConnectionPoolService> connectionServices = new HashMap<>();

    private static ConnectionProvider connProvider = (persistenceUnitName) -> {
        return new DefaultConnectionPool(persistenceUnitName);
    };

    public static void registerConnectionPoolService(String moduleName, ConnectionPoolService service) {
        connectionServices.put(moduleName, service);
    }

    public static void registerConnectionPoolService(String moduleName, String persistenceUnitName) {
        if (connProvider != null) {
            connectionServices.put(moduleName, connProvider.from(persistenceUnitName));
        }
    }

    public static void registerProvider(ConnectionProvider provider) {
        if (provider != null) {
            connProvider = provider;
        }
    }

    public static ConnectionPoolService getConnectionPoolService(String moduleName) {
        return connectionServices.get(moduleName);
    }

}
