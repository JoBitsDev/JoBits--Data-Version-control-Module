/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jobits.db.core.domain.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jobits.db.core.domain.ConexionPropertiesModel;
import org.jobits.db.core.domain.ConexionPropertiesModel;
import org.jobits.db.core.domain.ConexionPropertiesWrapperModel;
import org.jobits.db.core.domain.ConexionPropertiesWrapperModel;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
public class ConexionPropertiesWrapperModelImpl implements ConexionPropertiesWrapperModel {

    private ConexionPropertiesModel[] ubicaciones = new ConexionPropertiesModel[4];
    private int selectedUbicacion;

    public ConexionPropertiesWrapperModelImpl() {
    }

    public ConexionPropertiesWrapperModelImpl(ConexionPropertiesModel[] ubicaciones, int selectedUbicacion) {
        this.ubicaciones = ubicaciones;
        this.selectedUbicacion = selectedUbicacion;
    }

    @Override
    public ConexionPropertiesModel[] getUbicaciones() {
        return ubicaciones;
    }

    @Override
    public void setUbicaciones(ConexionPropertiesModel[] ubicaciones) {
        this.ubicaciones = ubicaciones;
    }

    @Override
    public int getSelectedUbicacion() {
        return selectedUbicacion;
    }

    @Override
    public void setSelectedUbicacion(int selectedUbicacion) {
        this.selectedUbicacion = selectedUbicacion;
    }
    
    @JsonIgnore
    @Override
    public void setUbicacionSeleccionada(ConexionPropertiesModel selectedUbicacion) {
        for (int i = 0; i < ubicaciones.length; i++) {
            if (ubicaciones[i].equals(selectedUbicacion)) {
                this.selectedUbicacion = i;
            }
        }
    }

    @JsonIgnore
    @Override
    public ConexionPropertiesModel getUbicacionActiva() {
        return selectedUbicacion == -1 ? null : ubicaciones[selectedUbicacion];
    }

}
