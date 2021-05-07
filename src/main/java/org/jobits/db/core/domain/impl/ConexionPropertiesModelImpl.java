/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jobits.db.core.domain.impl;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.Objects;
import org.jobits.db.core.domain.ConexionPropertiesModel;
import org.jobits.db.core.domain.TipoConexion;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
public class ConexionPropertiesModelImpl implements ConexionPropertiesModel{

    public static ConexionPropertiesModel from(String nombre, String url, String usuario,
            String contrasena, String driver, TipoConexion tipoUbicacion) {
        return new ConexionPropertiesModelImpl(nombre, url, usuario, contrasena, driver, tipoUbicacion);
    }

    public static ConexionPropertiesModelImpl[] getDefaultUbicaciones() {
        ConexionPropertiesModelImpl[] ret = new ConexionPropertiesModelImpl[4];
        for (int i = 0; i < ret.length; i++) {
            ConexionPropertiesModelImpl ubicacionVacia
                    = new ConexionPropertiesModelImpl("<Nueva Ubicacion>", "0.0.0.0", "Nueva", "Nueva", "Nueva", TipoConexion.DESACTIVADA);
            ret[i] = ubicacionVacia;
        }
        return ret;
    }

    @JsonAlias({"nombre"})
    private String nombreUbicacion;
    private String url;
    private String usuario;
    private String contrasena;
    private String driver;
    private TipoConexion tipoUbicacion;

    public ConexionPropertiesModelImpl(String nombre, String url, String usuario,
            String contrasena, String driver, TipoConexion tipoUbicacion) {
        this.tipoUbicacion = tipoUbicacion;
        this.nombreUbicacion = nombre;
        this.url = url;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.driver = driver;
    }

    public ConexionPropertiesModelImpl() {
    }

    //
    // Getters y Setters
    //
    public String getNombreUbicacion() {
        return nombreUbicacion;
    }

    public void setNombreUbicacion(String nombreUbicacion) {
        this.nombreUbicacion = nombreUbicacion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public TipoConexion getTipoUbicacion() {
        return tipoUbicacion;
    }

    public void setTipoUbicacion(TipoConexion tipoUbicacion) {
        this.tipoUbicacion = tipoUbicacion;
    }

    @Override
    public String toString() {
        return getNombreUbicacion() + "<" + getTipoUbicacion() + ">";
    }

    public enum TipoUbicacion {
        DESACTIVADA, MASTER, RESPALDO, SINCRONIZACION;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.nombreUbicacion);
        hash = 41 * hash + Objects.hashCode(this.url);
        hash = 41 * hash + Objects.hashCode(this.usuario);
        hash = 41 * hash + Objects.hashCode(this.contrasena);
        hash = 41 * hash + Objects.hashCode(this.driver);
        hash = 41 * hash + Objects.hashCode(this.tipoUbicacion);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ConexionPropertiesModelImpl other = (ConexionPropertiesModelImpl) obj;
        if (!Objects.equals(this.nombreUbicacion, other.nombreUbicacion)) {
            return false;
        }
        if (!Objects.equals(this.url, other.url)) {
            return false;
        }
        if (!Objects.equals(this.usuario, other.usuario)) {
            return false;
        }
        if (!Objects.equals(this.contrasena, other.contrasena)) {
            return false;
        }
        if (!Objects.equals(this.driver, other.driver)) {
            return false;
        }
        if (this.tipoUbicacion != other.tipoUbicacion) {
            return false;
        }
        return true;
    }
    
    

}
