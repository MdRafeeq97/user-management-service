package com.ums.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;
import java.net.UnknownHostException;

//@EnableCaching
@SpringBootApplication(
        exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class}

)
@ComponentScan(basePackages = {"com.ums.*"})
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@EnableAsync
@EnableScheduling
@Configuration
@EnableConfigurationProperties
@EntityScan("com.ums.*")
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@EnableJpaRepositories(basePackages = {"com.ums.*"},
        entityManagerFactoryRef = "entityManagerFactory",
        enableDefaultTransactions = false)
//@EnableTransactionManagement(order = 100)
@PropertySource(value = {"file:./application-${spring.profiles.active}.properties", "classpath:application-${spring.profiles.active}.properties"}, ignoreResourceNotFound = true)
public class UserManagementApp {
    public static void main(String[] args) throws UnknownHostException {
        System.setProperty("hostName", InetAddress.getLocalHost().getHostName());
        System.setProperty("applicationName", "InvoicingWeb");
        SpringApplication.run(UserManagementApp.class, args);
    }
}
