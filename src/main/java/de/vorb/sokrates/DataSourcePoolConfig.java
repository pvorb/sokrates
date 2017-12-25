/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.vorb.sokrates;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.flywaydb.core.Flyway;

import javax.annotation.PostConstruct;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourcePoolConfig {

    private final ComboPooledDataSource dataSource;

    public DataSourcePoolConfig() throws PropertyVetoException {

        disableJooqBanner();
        disableC3p0Logging();

        dataSource = initializeDataSource();

        migrateDatabase();
    }

    private ComboPooledDataSource initializeDataSource() throws PropertyVetoException {
        final ComboPooledDataSource dataSource = new ComboPooledDataSource();

        dataSource.setJdbcUrl("jdbc:h2:./sokrates");
        dataSource.setUser("sa");
        dataSource.setDriverClass(org.h2.Driver.class.getCanonicalName());

        return dataSource;
    }

    private void disableJooqBanner() {
        System.setProperty("org.jooq.no-logo", "true");
    }

    private void disableC3p0Logging() {
        System.setProperty("com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");
        System.setProperty("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", "WARNING");
    }

    private void migrateDatabase() {
        final Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.migrate();
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @PostConstruct
    public void close() {
        dataSource.close();
    }
}
