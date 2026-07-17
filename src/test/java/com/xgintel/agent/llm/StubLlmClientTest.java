package com.xgintel.agent.llm;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class StubLlmClientTest {

    private final StubLlmClient client = new StubLlmClient();

    @Test
    void echoesQuestionDeterministically() {
        LlmResponse response = client.complete(LlmRequest.of("How does Arsenal play away?"));
        assertNotNull(response.content());
        assertTrue(response.content().contains("How does Arsenal play away?"));
    }

    @Test
    void isDeterministic() {
        LlmRequest request = LlmRequest.of("same question");
        assertTrue(client.complete(request).content().equals(client.complete(request).content()));
    }
}
