package com.xgintel.catalog.repo;

import com.xgintel.catalog.domain.Competition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetitionRepository extends JpaRepository<Competition, Long> {
}
