package com.xgintel.agent.llm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Real {@link LlmClient} backed by the Anthropic Claude API.
 *
 * <p><strong>Skeleton:</strong> the wiring and configuration are in place, but the
 * API call is not implemented yet. When implemented, this should use the official
 * Anthropic Java SDK ({@code com.anthropic:anthropic-java}) with a tool-use loop.
 *
 * <p>Active when {@code xgintel.llm.provider=anthropic}.
 */
@Component
@ConditionalOnProperty(name = "xgintel.llm.provider", havingValue = "anthropic")
public class AnthropicLlmClient implements LlmClient {

    private final String apiKey;
    private final String model;

    public AnthropicLlmClient(
            @Value("${xgintel.llm.anthropic.api-key:}") String apiKey,
            @Value("${xgintel.llm.anthropic.model:claude-sonnet-5}") String model) {
        this.apiKey = apiKey;
        this.model = model;
    }

    @Override
    public LlmResponse complete(LlmRequest request) {
        throw new UnsupportedOperationException(
                "AnthropicLlmClient is not implemented yet (model=" + model + "). "
                        + "See docs/architecture.md § Agent layer. Use LLM_PROVIDER=stub for now.");
    }
}
