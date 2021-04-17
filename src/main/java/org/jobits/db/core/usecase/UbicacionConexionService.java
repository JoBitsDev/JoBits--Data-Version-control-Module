/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jobits.db.core.usecase;

import com.root101.clean.core.app.usecase.AbstractUseCase;
import org.jobits.db.core.domain.UbicacionConexionModel;
import org.jobits.db.core.domain.UbicacionWrapper;


/**
 *
 * @author ERIK QUESADA
 */
public interface UbicacionConexionService extends AbstractUseCase{

    public static final String PROP_LOCATION_CHANGED = "locationChanged";
    
    public void editUbicacion(UbicacionConexionModel uc, int selectedUbicacion);

    public void setSelectedUbicacion(UbicacionConexionModel ubicacionSeleccionada);

    public UbicacionWrapper getUbicaciones();
    
}
