/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jobits.db.core.usecase;

import com.root101.clean.core.app.usecase.AbstractUseCase;
import org.jobits.db.core.domain.ConexionPropertiesModel;
import org.jobits.db.core.domain.ConexionPropertiesWrapperModel;

/**
 *
 * @author ERIK QUESADA
 */
public interface UbicacionConexionService extends AbstractUseCase {

    public static final String PROP_LOCATION_CHANGED = "conexionChanged";

    public void editConexion(ConexionPropertiesModel uc, int selectedUbicacion);

    public void setSelectedConexion(ConexionPropertiesModel ubicacionSeleccionada);

    public ConexionPropertiesWrapperModel getUbicaciones();

}
