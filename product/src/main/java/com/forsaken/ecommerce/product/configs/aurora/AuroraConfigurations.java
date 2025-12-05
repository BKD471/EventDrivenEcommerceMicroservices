package com.forsaken.ecommerce.product.configs.aurora;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class AuroraConfigurations {

    private final AwsDbCredentials dbCredentials;

    @Bean
    public DataSource dataSource() {
        final String jdbcUrl= String.format("jdbc:postgresql://%s:%s/%s",
                dbCredentials.host(),dbCredentials.port(),dbCredentials.dbName());

        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(dbCredentials.userName());
        dataSource.setPassword(dbCredentials.password());

        final Flyway flyway = Flyway.configure()
                .baselineOnMigrate(true)
                .dataSource(dataSource)
                .load();

        flyway.repair();
        flyway.migrate();
        return dataSource;
    }
}