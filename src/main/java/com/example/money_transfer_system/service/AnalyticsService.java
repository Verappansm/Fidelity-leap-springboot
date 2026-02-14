package com.example.money_transfer_system.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AnalyticsService {

    private final JdbcTemplate snowflakeJdbcTemplate;

    public AnalyticsService(
            @Qualifier("snowflakeJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.snowflakeJdbcTemplate = jdbcTemplate;
    }

    public Map<String, Object> getKpis() {

        Integer totalTransactions = snowflakeJdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM FACT_TRANSACTIONS",
                Integer.class
        );

        Double totalAmount = snowflakeJdbcTemplate.queryForObject(
                "SELECT SUM(amount) FROM FACT_TRANSACTIONS",
                Double.class
        );

        Double successRate = snowflakeJdbcTemplate.queryForObject("""
                SELECT ROUND(100.0 *
                SUM(CASE WHEN status='SUCCESS' THEN 1 ELSE 0 END)
                / NULLIF(COUNT(*),0),2)
                FROM FACT_TRANSACTIONS
                """,
                Double.class
        );

        return Map.of(
                "totalTransactions", totalTransactions,
                "totalAmount", totalAmount,
                "successRate", successRate
        );
    }
}
