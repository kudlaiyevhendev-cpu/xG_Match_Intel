package com.xgintel.agent.llm;

/**
 * The model's answer. Kept intentionally small for the skeleton; token usage and
 * the tool-call trace will be added alongside the agent loop.
 */
public record LlmResponse(String content) {
}
