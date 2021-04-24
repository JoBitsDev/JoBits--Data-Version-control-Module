/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jobits.db.pool;

/**
 *
 * @author Jorge
 */
public interface ConnectionProvider {

    public ConnectionPoolService from(String persistenceUnitName);

}
