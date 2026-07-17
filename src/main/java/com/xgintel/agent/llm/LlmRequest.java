package com.xgintel.agent.llm;

/**
 * A minimal LLM request for the skeleton: a system prompt plus the user message.
 * Tool definitions and conversation history will be added as the agent loop is
 * built out.
 */
public record LlmRequest(String systemPrompt, String userMessage) {

    public LlmRequest {
        if (userMessage == null || userMessage.isBlank()) {
            throw new IllegalArgumentException("userMessage must not be blank");
        }
    }

    public static LlmRequest of(String userMessage) {
        return new LlmRequest(null, userMessage);
    }
}
