package com.ponir.portfolio.repository;

import com.ponir.portfolio.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findAllByOrderByCategoryAscSortOrderAscNameAsc();
    List<Skill> findByFeaturedTrueOrderBySortOrderAscNameAsc();
}
