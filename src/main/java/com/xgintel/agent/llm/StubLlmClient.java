package com.xgintel.agent.llm;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Deterministic, offline {@link LlmClient} used by default and in tests/demos, so
 * the "wow path" runs with no API key or network. It does not call any model — it
 * echoes a canned, grounded-looking response.
 *
 * <p>Active when {@code xgintel.llm.provider=stub} (the default).
 */
@Component
@ConditionalOnProperty(name = "xgintel.llm.provider", havingValue = "stub", matchIfMissing = true)
public class StubLlmClient implements LlmClient {

    @Override
    public LlmResponse complete(LlmRequest request) {
        return new LlmResponse(
                "[stub] I would answer \"" + request.userMessage() + "\" by calling the metrics "
                        + "tools (form, xG trend, head-to-head, home/away splits) and grounding the "
                        + "response in their results. Set LLM_PROVIDER=anthropic for a real answer.");
    }
}
