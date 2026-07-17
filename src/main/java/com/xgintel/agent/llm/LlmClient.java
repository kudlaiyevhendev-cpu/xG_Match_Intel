package com.xgintel.agent.llm;

/**
 * Abstraction over the LLM used by the agent layer. Two implementations exist:
 * {@link StubLlmClient} (deterministic, offline — default, and used in tests/demos)
 * and {@link AnthropicLlmClient} (the real Claude API).
 *
 * <p>Selected at runtime via the {@code xgintel.llm.provider} property.
 */
public interface LlmClient {

    /**
     * Completes a single request. The tool-use loop and prompt assembly live in the
     * agent {@code run} package and call into this method.
     */
    LlmResponse complete(LlmRequest request);
}
