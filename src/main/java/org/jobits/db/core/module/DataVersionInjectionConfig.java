/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jobits.db.core.module;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.jobits.db.core.usecase.UbicacionConexionService;
import org.jobits.db.core.usecase.impl.UbicacionConexionServiceImpl;

/**
 *
 * JoBits
 *
 * @author Jorge
 *
 */
public class DataVersionInjectionConfig extends AbstractModule {

    @Override
    protected void configure() {
        bind(UbicacionConexionService.class).to(UbicacionConexionServiceImpl.class).in(Scopes.SINGLETON);
    }

}
