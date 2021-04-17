/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jobits.db.versioncontrol;

/**
 *
 * JoBits
 *
 * @author Jorge
 *
 */
public interface DataVersionControlService {

    public static DataVersionControlService from(String moduleName, String path, String schema) {
        return new DataVersionControlService() {
            @Override
            public String getModuleName() {
                return moduleName;
            }

            @Override
            public String getVersionControlPath() {
                return path;
            }

            @Override
            public String getDataSchema() {
                return schema;
            }

        };
    }

    public String getModuleName();

    /**
     *
     * @return Path to app package where the migration data is located
     */
    public String getVersionControlPath();

    public String getDataSchema();

}
