/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jobits.db.core.domain;

/**
 *
 * JoBits
 *
 * @author Jorge
 *
 */
public interface ConexionPropertiesModel {

    String getContrasena();

    String getDriver();

    String getNombreUbicacion();

    TipoConexion getTipoUbicacion();

    String getUrl();

    String getUsuario();

}
