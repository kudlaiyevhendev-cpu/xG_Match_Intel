package com.xgintel.api;

import com.xgintel.agent.llm.LlmClient;
import com.xgintel.agent.llm.LlmRequest;
import com.xgintel.agent.llm.LlmResponse;
import com.xgintel.api.dto.AskRequest;
import com.xgintel.api.dto.AskResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Agent endpoint. For the skeleton this passes the question straight to the
 * {@link LlmClient}; the tool-use loop (question → tool calls → grounded answer)
 * will sit behind this controller in the agent {@code run} package.
 */
@RestController
@RequestMapping("/api/v1/agent")
public class AgentController {

    private final LlmClient llmClient;

    public AgentController(LlmClient llmClient) {
        this.llmClient = llmClient;
    }

    @PostMapping("/ask")
    public AskResponse ask(@Valid @RequestBody AskRequest request) {
        LlmResponse response = llmClient.complete(LlmRequest.of(request.question()));
        return new AskResponse(response.content());
    }
}
