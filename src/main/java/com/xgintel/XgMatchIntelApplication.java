package com.xgintel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Entry point for the xG Match Intel service.
 *
 * <p>A modular monolith: ingestion, catalog, metrics, agent, and api packages,
 * layered internally. See {@code docs/architecture.md} for the full design.
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling
public class XgMatchIntelApplication {

    public static void main(String[] args) {
        SpringApplication.run(XgMatchIntelApplication.class, args);
    }
}
