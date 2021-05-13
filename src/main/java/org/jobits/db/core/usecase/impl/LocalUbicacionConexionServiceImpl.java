/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jobits.db.core.usecase.impl;

import org.jobits.db.core.usecase.UbicacionConexionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jobits.db.core.domain.ConexionPropertiesModel;
import org.jobits.db.core.domain.ConexionPropertiesWrapperModel;
import org.jobits.db.core.domain.impl.ConexionPropertiesModelImpl;
import org.jobits.db.core.domain.impl.ConexionPropertiesWrapperModelImpl;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
public class LocalUbicacionConexionServiceImpl extends com.root101.clean.core.app.usecase.AbstractUseCaseImpl
        implements UbicacionConexionService {

    private ConexionPropertiesWrapperModelImpl ubicaciones;
    private ObjectMapper om = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private String FILE_NAME = "ubicaciones.json";

    public LocalUbicacionConexionServiceImpl() {
        try {
            SimpleModule deserialize = new SimpleModule();
            deserialize.addAbstractTypeMapping(ConexionPropertiesModel.class, ConexionPropertiesModelImpl.class);
            deserialize.addAbstractTypeMapping(ConexionPropertiesWrapperModel.class, ConexionPropertiesWrapperModelImpl.class);
            om.registerModule(deserialize);
            ubicaciones = getUbicacionesAlmacenadas();
        } catch (IOException ex) {
            try {
                File f = new File(FILE_NAME);
                f.createNewFile();
                ubicaciones = new ConexionPropertiesWrapperModelImpl(ConexionPropertiesModelImpl.getDefaultUbicaciones(), 0);
                guardarUbicacionesAlmacenadas();
            } catch (IOException ex1) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error de IO. {0}", ex1.getMessage());
            }
        }
    }

    private ConexionPropertiesWrapperModelImpl getUbicacionesAlmacenadas() throws IOException {
        return om.readValue(new File(FILE_NAME), ConexionPropertiesWrapperModelImpl.class);
    }

    private void guardarUbicacionesAlmacenadas() {
        try {
            om.writeValue(new File(FILE_NAME), ubicaciones);
        } catch (IOException ex) {
            Logger.getLogger(LocalUbicacionConexionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("No es posible acceder a las ubicaciones");
        }
    }

    public ConexionPropertiesWrapperModelImpl getUbicaciones() {
        return ubicaciones;
    }

    public void setUbicaciones(ConexionPropertiesWrapperModelImpl ubicaciones) {
        this.ubicaciones = ubicaciones;
    }

    public void setSelectedUbicacion(int pos) throws IOException {
        ubicaciones.setSelectedUbicacion(pos);
        guardarUbicacionesAlmacenadas();
    }

    @Override
    public void setSelectedConexion(ConexionPropertiesModel ubicacionSeleccionada) {
        ConexionPropertiesModel oldUbicacion = ubicaciones.getUbicacionActiva();
        ubicaciones.setUbicacionSeleccionada(ubicacionSeleccionada);
        guardarUbicacionesAlmacenadas();
        firePropertyChange(PROP_LOCATION_CHANGED, oldUbicacion, ubicaciones.getUbicacionActiva());
    }

    @Override
    public void editConexion(ConexionPropertiesModel ubicacionEditada, int pos) {
        ubicaciones.getUbicaciones()[pos] = ubicacionEditada;
        guardarUbicacionesAlmacenadas();
    }

}
