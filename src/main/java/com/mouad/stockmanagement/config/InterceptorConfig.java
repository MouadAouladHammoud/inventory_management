package com.mouad.stockmanagement.config;

import com.mouad.stockmanagement.interceptor.Interceptor;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InterceptorConfig {
    @Bean
    public HibernatePropertiesCustomizer hibernateCustomizer() { // "HibernatePropertiesCustomizer" est utilisé pour interpréter les requêtes SQL
        // return (properties) -> properties.put(AvailableSettings.STATEMENT_INSPECTOR, new Interceptor());

        return (properties) -> {
            properties.put(AvailableSettings.DIALECT, "org.hibernate.dialect.MySQLDialect"); // On peut changer le dialecte SQL utilisé par Hibernate si un dialecte est déjà prédéfini dans la configuration du projet (voir le fichier application.yml).

            properties.put(AvailableSettings.SHOW_SQL, true); // Afficher les requêtes SQL générées par Hibernate
            properties.put(AvailableSettings.FORMAT_SQL, true); // Formate les requêtes SQL générées pour une meilleure lisibilité

            // properties.put(AvailableSettings.USE_SECOND_LEVEL_CACHE, true); // Active/désactive le cache de second niveau
            // properties.put(AvailableSettings.CACHE_REGION_FACTORY, "org.hibernate.cache.ehcache.EhCacheRegionFactory"); // Spécifie la classe de la fabrique de cache de second niveau

            // Cette propriété permet d'inspecter et de modifier les requêtes SQL avant qu'elles ne soient exécutées par Hibernate.
            //   Les modifications des requêtes seront effectuées dans la classe "Interceptor.java", qui implémente l'interface "StatementInspector" pour pouvoir interpréter les requêtes provenant de la méthode "hibernateCustomizer()".
            properties.put(AvailableSettings.STATEMENT_INSPECTOR, new Interceptor());
        };
    }
}
