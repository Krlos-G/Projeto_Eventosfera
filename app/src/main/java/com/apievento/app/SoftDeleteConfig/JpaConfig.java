package com.apievento.app.SoftDeleteConfig;

import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class JpaConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource,
            JpaProperties jpaProperties) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.example.eventoscomunitarios.model");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        // Configuração do StatementInspector (substituto moderno do Interceptor)
        Map<String, Object> properties = new HashMap<>(jpaProperties.getProperties());
        properties.put("hibernate.session_factory.statement_inspector", softDeleteStatementInspector());
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    public SoftDeleteStatementInspector softDeleteStatementInspector() {
        return new SoftDeleteStatementInspector();
    }
}
