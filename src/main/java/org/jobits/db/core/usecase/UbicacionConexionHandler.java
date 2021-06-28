/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jobits.db.core.usecase;

import org.jobits.db.pool.*;
import org.jobits.db.pool.impl.LocalConnectionPool;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jobits.db.core.usecase.impl.LocalUbicacionConexionServiceImpl;
import org.jobits.db.versioncontrol.DataVersionControlHandler;

/**
 *
 * JoBits
 *
 * @author Jorge
 *
 */
public class UbicacionConexionHandler {

    private static UbicacionConexionService connectionServices ;

    public static void registerUbicacionConexionService(UbicacionConexionService service) {
        if (connectionServices != null) {
            connectionServices.removePropertyChangeListener(DataVersionControlHandler.ubicacionChangeListener);
        }
        connectionServices = service;
        connectionServices.addPropertyChangeListener(DataVersionControlHandler.ubicacionChangeListener);
        DataVersionControlHandler.ubicacionChangeListener.propertyChange(null);
    }

    public static UbicacionConexionService getConnectionPoolService() {
        return connectionServices;
    }

}
