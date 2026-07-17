package com.xgintel.api.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Free-form analytical question for the agent, e.g.
 * "How does Arsenal play away against top-6?".
 */
public record AskRequest(@NotBlank String question) {
}
