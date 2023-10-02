package com.ums.config;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

//@EnableTransactionManagement
@Configuration
@PropertySource(value = "classpath:datasource-${spring.profiles.active}.properties")
public class DataSourceConfig {

    private static final String FALSE = "false";

    @Bean("hcMaster")
    @Primary
    @ConfigurationProperties(prefix = "master.datasource.hikari")
    public HikariConfig masterHikariConfig() {
        return new HikariConfig();
    }

    @Bean("hcSlave")
    @ConfigurationProperties(prefix = "slave.datasource.hikari")
    public HikariConfig slaveHikariConfig() {
        return new HikariConfig();
    }

    @Bean
    @Primary
    public DataSource masterDataSource() {
        return new HikariDataSource(masterHikariConfig());
    }

    @Bean
    public DataSource slaveDataSource() {
        return new HikariDataSource(slaveHikariConfig());
    }

    @Bean
    public RoutingDataSource routingDataSource() {
        RoutingDataSource routingDataSource = new RoutingDataSource();
        routingDataSource.setDefaultTargetDataSource(DataSourceType.MASTER);

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(DataSourceType.MASTER, masterDataSource());
        dataSourceMap.put(DataSourceType.SLAVE, slaveDataSource());

        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(masterDataSource());
        return routingDataSource;
    }


    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(routingDataSource());
        em.setPersistenceUnitName("persistence.routing");
        em.setPackagesToScan("com.ums");
        em.setJpaPropertyMap(getHibernatePropertiesMap());

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        em.setJpaProperties(getHibernatePropertiesMaster());
        return em;
    }

    private Properties getHibernatePropertiesMaster() {
        Properties properties = new Properties();
        properties.put("org.hibernate.envers.auditTableSuffix", "_aud");
        properties.put("org.hibernate.envers.auditTablePrefix", "");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.use_sql_comments", "true");
        properties.put("hibernate.generate_statistics", FALSE);
        properties.put("hibernate.hbm2ddl.auto", "none");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
//        properties.put("hibernate.physical_naming_strategy",
//                "com.paytm.invoicing.dao.config.CustomPhysicalNamingStrategy");
        return properties;
    }

    // pranav
    private Map<String, String> getHibernatePropertiesMap() {
        Map<String, String> properties = new HashMap<>();
        properties.put("org.hibernate.envers.auditTableSuffix", "_AUD");
        properties.put("org.hibernate.envers.auditTablePrefix", "");
        properties.put("org.hibernate.envers.storeDataAtDelete", "true");
        properties.put("org.hibernate.envers.do_not_audit_optimistic_locking_field", "true");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.use_sql_comments", "true");
        properties.put("hibernate.generate_statistics", FALSE);
        properties.put("hibernate.hbm2ddl.auto", "none");
        properties.put("bytecode.use_reflection_optimizer", FALSE);
        properties.put("javax.persistence.validation.mode", "none");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
//        properties.put("hibernate.physical_naming_strategy",
//                "com.paytm.invoicing.dao.config.CustomPhysicalNamingStrategy");
        return properties;
    }

}
