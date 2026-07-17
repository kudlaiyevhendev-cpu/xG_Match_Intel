package com.xgintel.catalog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.xgintel.catalog.domain.Team;
import com.xgintel.catalog.repo.TeamRepository;
import com.xgintel.common.error.ResourceNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CatalogServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private CatalogService catalogService;

    @Test
    void getTeamReturnsTeamWhenPresent() {
        Team team = new Team("Arsenal", "ARS");
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        assertEquals("Arsenal", catalogService.getTeam(1L).getName());
    }

    @Test
    void getTeamThrowsWhenMissing() {
        when(teamRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> catalogService.getTeam(99L));
    }
}
