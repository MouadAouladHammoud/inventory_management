package com.mouad.stockmanagement.interceptor;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

public class Interceptor implements StatementInspector {

    @Override
    public String inspect(String sql) { // Ici, on peut interpréter les requêtes SQL provenant de la méthode "hibernateCustomizer()". (voir le fichier: InterceptorConfig.java)
        if (StringUtils.hasLength(sql) && sql.toLowerCase().startsWith("select")) {

            final String entityName = sql.substring(7, sql.indexOf(".")); // La référence de la table concernée
            System.out.println("entityName => " + entityName);

            if (StringUtils.hasLength(entityName)
                // && !entityName.toLowerCase().contains("companies")
                // && !entityName.toLowerCase().contains("roles")
                && sql.toLowerCase().contains(entityName + ".company_id")
            ) {

                if (sql.contains("where")) {
                    sql = sql + " and " + entityName + ".company_id = " + 1; // prendre id directemet depui user courant ...
                } else {
                    sql = sql + " where " + entityName + ".company_id = " + 1; // prendre id directemet depui user courant ...
                }
            }
        }
        System.out.println("sql => " + sql);
        return sql;
    }

}
