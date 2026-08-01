package com.ponir.portfolio.repository;

import com.ponir.portfolio.domain.SiteProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteProfileRepository extends JpaRepository<SiteProfile, Long> {
}
