/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jobits.db.core.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.Objects;

/**
 * FirstDream
 *
 * @author Jorge
 *
 */
public class UbicacionConexionModel {

    public static UbicacionConexionModel from(String nombre, String url, String usuario,
            String contrasena, String driver, TipoUbicacion tipoUbicacion) {
        return new UbicacionConexionModel(nombre, url, usuario, contrasena, driver, tipoUbicacion);
    }

    public static UbicacionConexionModel[] getDefaultUbicaciones() {
        UbicacionConexionModel[] ret = new UbicacionConexionModel[4];
        for (int i = 0; i < ret.length; i++) {
            UbicacionConexionModel ubicacionVacia
                    = new UbicacionConexionModel("<Nueva Ubicacion>", "0.0.0.0", "Nueva", "Nueva", "Nueva", TipoUbicacion.DESACTIVADA);
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
    private TipoUbicacion tipoUbicacion;

    public UbicacionConexionModel(String nombre, String url, String usuario,
            String contrasena, String driver, TipoUbicacion tipoUbicacion) {
        this.tipoUbicacion = tipoUbicacion;
        this.nombreUbicacion = nombre;
        this.url = url;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.driver = driver;
    }

    public UbicacionConexionModel() {
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

    public TipoUbicacion getTipoUbicacion() {
        return tipoUbicacion;
    }

    public void setTipoUbicacion(TipoUbicacion tipoUbicacion) {
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
        final UbicacionConexionModel other = (UbicacionConexionModel) obj;
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
