package com.apievento.app.SoftDeleteConfig;

import org.hibernate.resource.jdbc.spi.StatementInspector;

public class SoftDeleteStatementInspector implements StatementInspector {

    @Override
    public String inspect(String sql) {
        if (isSelectQuery(sql) && !containsSoftDeleteCondition(sql)) {
            return addSoftDeleteCondition(sql);
        }
        return sql;
    }

    private boolean isSelectQuery(String sql) {
        return sql.trim().toLowerCase().startsWith("select");
    }

    private boolean containsSoftDeleteCondition(String sql) {
        String lowerSql = sql.toLowerCase();
        return lowerSql.contains("deleted_at") || lowerSql.contains("deletedat");
    }

    private String addSoftDeleteCondition(String sql) {
        if (sql.toLowerCase().contains(" where ")) {
            return sql.replace(" where ", " where deleted_at is null and ");
        } else if (sql.toLowerCase().contains(" group by ")) {
            return sql.replace(" group by ", " where deleted_at is null group by ");
        } else if (sql.toLowerCase().contains(" order by ")) {
            return sql.replace(" order by ", " where deleted_at is null order by ");
        } else {
            return sql + " where deleted_at is null";
        }
    }

}
