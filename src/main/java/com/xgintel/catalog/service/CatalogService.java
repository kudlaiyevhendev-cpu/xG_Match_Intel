package com.xgintel.catalog.service;

import com.xgintel.catalog.domain.Team;
import com.xgintel.catalog.repo.TeamRepository;
import com.xgintel.common.error.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Read access to catalog resources (teams, competitions, matches). Controllers and
 * agent tools call services like this — never repositories directly.
 */
@Service
@Transactional(readOnly = true)
public class CatalogService {

    private final TeamRepository teamRepository;

    public CatalogService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Team> listTeams() {
        return teamRepository.findAll();
    }

    public Team getTeam(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Team", id));
    }
}
