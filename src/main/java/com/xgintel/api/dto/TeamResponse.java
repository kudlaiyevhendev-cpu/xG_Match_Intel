package com.xgintel.api.dto;

import com.xgintel.catalog.domain.Team;

/**
 * API representation of a team. Entities never leak past the controller boundary.
 */
public record TeamResponse(Long id, String name, String shortName) {

    public static TeamResponse from(Team team) {
        return new TeamResponse(team.getId(), team.getName(), team.getShortName());
    }
}
