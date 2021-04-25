/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

module org.joits.db {
    requires com.google.guice;
    requires org.flywaydb.core;
    requires org.postgresql.jdbc;
    requires org.eclipse.persistence.jpa;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.root101clean.core;
    requires javax.inject;
    requires java.persistence;
    requires org.hibernate.validator;
    requires org.hibernate.validator.annotationprocessor;
    requires javax.el.api;
    requires java.logging;
    requires java.desktop;

    exports org.jobits.db.core.domain;
    exports org.jobits.db.core.module;
    exports org.jobits.db.core.usecase;
    exports org.jobits.db.pool;
    exports org.jobits.db.utils;
    exports org.jobits.db.versioncontrol;
}
