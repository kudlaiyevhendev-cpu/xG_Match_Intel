package com.xgintel.catalog.repo;

import com.xgintel.catalog.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
