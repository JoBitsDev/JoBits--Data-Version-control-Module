/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jobits.db.core.module;

import com.root101.clean.core.domain.services.ResourceService;
import java.util.HashMap;
import java.util.Map;
import org.jobits.db.core.usecase.UbicacionConexionService;
import org.jobits.db.core.domain.ConexionPropertiesModel;

/**
 *
 * JoBits
 *
 * @author Jorge
 *
 */
public class UbicacionResourceServiceImpl implements ResourceService<String> {

    private UbicacionConexionService ubicacionRepo;

    private final String UBICACION_SELECCIONADA_NOMBRE = "com.jobits.pos.db.current_conn_name";
    private final String UBICACION_SELECCIONADA_URL = "com.jobits.pos.db.current_conn_url";
    private final String UBICACION_SELECCIONADA_USER = "com.jobits.pos.db.current_conn_user";
    private final String UBICACION_SELECCIONADA_PASS = "com.jobits.pos.db.current_conn_pass";
    private final String UBICACION_SELECCIONADA_DRIVER = "com.jobits.pos.db.current_conn_driver";
    private final String UBICACION_SELECCIONADA_TIPO = "com.jobits.pos.db.current_conn_tipo";

    private Map<String, String> propertiesMap = new HashMap<>();

    public UbicacionResourceServiceImpl(UbicacionConexionService ubicacionRepo) {
        this.ubicacionRepo = ubicacionRepo;
        propertiesMap.put(UBICACION_SELECCIONADA_URL, UBICACION_SELECCIONADA_URL);
        propertiesMap.put(UBICACION_SELECCIONADA_DRIVER, UBICACION_SELECCIONADA_DRIVER);
        propertiesMap.put(UBICACION_SELECCIONADA_NOMBRE, UBICACION_SELECCIONADA_NOMBRE);
        propertiesMap.put(UBICACION_SELECCIONADA_PASS, UBICACION_SELECCIONADA_PASS);
        propertiesMap.put(UBICACION_SELECCIONADA_TIPO, UBICACION_SELECCIONADA_TIPO);
        propertiesMap.put(UBICACION_SELECCIONADA_USER, UBICACION_SELECCIONADA_USER);

    }

    @Override
    public boolean contain(String string) {
        return propertiesMap.containsKey(string);
    }

    @Override
    public String getObject(String string) {
        return getString(string);
    }

    @Override
    public String getString(String string) {
        ConexionPropertiesModel model = ubicacionRepo.getUbicaciones().getUbicacionActiva();
        switch (string) {
            case UBICACION_SELECCIONADA_DRIVER:
                return model.getDriver();
            case UBICACION_SELECCIONADA_NOMBRE:
                return model.getNombreUbicacion();
            case UBICACION_SELECCIONADA_PASS:
                return model.getContrasena();
            case UBICACION_SELECCIONADA_TIPO:
                return model.getTipoUbicacion().toString();
            case UBICACION_SELECCIONADA_URL:
                return model.getUrl();
            case UBICACION_SELECCIONADA_USER:
                return model.getUsuario();
            default:
                return string;

        }
    }

}
