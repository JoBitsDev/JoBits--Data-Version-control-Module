/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jobits.db.core.module;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.root101.clean.core.app.modules.AbstractModule;
import com.root101.clean.core.app.modules.DefaultAbstractModule;
import com.root101.clean.core.domain.services.ResourceHandler;
import com.root101.clean.core.exceptions.AlreadyInitModule;
import com.root101.clean.core.exceptions.NotInitModule;
import org.jobits.db.core.usecase.UbicacionConexionService;
import org.jobits.db.core.usecase.impl.UbicacionConexionServiceImpl;
import org.jobits.db.pool.UbicacionResourceServiceImpl;
import org.jobits.db.versioncontrol.DataVersionControlHandler;

/**
 *
 * JoBits
 *
 * @author Jorge
 *
 */
public class DataVersionControlModule extends DefaultAbstractModule {

    public static final String MODULE_NAME = "Data Version Control Module";

    private final Injector inj = Guice.createInjector(new DataVersionInjectionConfig());

    private static DataVersionControlModule INSTANCE;

    public static DataVersionControlModule getInstance() {
        if (INSTANCE == null) {
            throw new NotInitModule(ResourceHandler.getString("org.jobits.db.name"));
        }
        return INSTANCE;
    }

    /**
     * Usar init() sin repo por parametro para usar el repo por defecto
     *
     * @param repoModule
     * @return
     * @Deprecated
     */
    public static DataVersionControlModule init(AbstractModule... repoModule) {
        if (INSTANCE != null) {
            throw new AlreadyInitModule(ResourceHandler.getString("org.jobits.db.name"));
        }
        INSTANCE = new DataVersionControlModule();
        for (AbstractModule x : repoModule) {
            INSTANCE.registerModule(x);

        }
        return getInstance();
    }

    private DataVersionControlModule() {
        ResourceHandler.registerInternal("data-version-control-module-props");
        ResourceHandler.registerResourceService(new UbicacionResourceServiceImpl(new UbicacionConexionServiceImpl()));//TODO: inyectar
        getImplementation(UbicacionConexionService.class).addPropertyChangeListener(DataVersionControlHandler.ubicacionChangeListener);
    }

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

    @Override
    protected <T> T getOwnImplementation(Class<T> type) {
        return inj.getInstance(type);
    }

}
