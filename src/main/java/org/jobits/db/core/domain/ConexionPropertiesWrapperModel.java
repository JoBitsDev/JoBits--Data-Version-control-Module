/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jobits.db.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * JoBits
 *
 * @author Jorge
 *
 */
public interface ConexionPropertiesWrapperModel {

    public int getSelectedUbicacion();

    public void setSelectedUbicacion(int selectedUbicacion);

    ConexionPropertiesModel getUbicacionActiva();

    public void setUbicacionSeleccionada(ConexionPropertiesModel selectedUbicacion);

    public ConexionPropertiesModel[] getUbicaciones();

    public void setUbicaciones(ConexionPropertiesModel[] ubicaciones);

}
