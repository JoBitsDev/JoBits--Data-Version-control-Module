/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jobits.db.pool;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.jobits.db.core.domain.ConexionPropertiesModel;

/**
 * Es el servicio encargado de gestionar los EnityManager de los modulos
 * 
 * @author Jorge
 */
public interface ConnectionPoolService {

    public EntityManagerFactory getEMF();

    public EntityManagerFactory getEmfFrom(ConexionPropertiesModel connectionsProperties);

    public EntityManager getCurrentConnection();

    public ConexionPropertiesModel getCurrentUbicacion();

    public void resetConnection();

    public boolean isConnected();
}
