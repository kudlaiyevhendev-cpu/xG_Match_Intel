package com.xgintel;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Verifies the Spring context wires up end to end (JPA + repositories + services +
 * controllers + stub LLM) under the {@code test} profile — H2, in-memory cache, no
 * external Postgres/Redis required.
 */
@SpringBootTest
@ActiveProfiles("test")
class XgMatchIntelApplicationTests {

    @Test
    void contextLoads() {
    }
}
