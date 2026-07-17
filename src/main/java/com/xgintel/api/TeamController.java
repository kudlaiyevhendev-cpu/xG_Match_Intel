package com.xgintel.api;

import com.xgintel.api.dto.TeamResponse;
import com.xgintel.catalog.service.CatalogService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Teams resource. Delegates to {@link CatalogService}; maps entities to DTOs.
 */
@RestController
@RequestMapping("/api/v1/teams")
public class TeamController {

    private final CatalogService catalogService;

    public TeamController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    public List<TeamResponse> list() {
        return catalogService.listTeams().stream().map(TeamResponse::from).toList();
    }

    @GetMapping("/{id}")
    public TeamResponse get(@PathVariable Long id) {
        return TeamResponse.from(catalogService.getTeam(id));
    }
}
