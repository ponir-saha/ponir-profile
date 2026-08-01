package com.ponir.portfolio.service;

import com.ponir.portfolio.domain.Skill;
import com.ponir.portfolio.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SkillService {
    private static final List<String> PRACTICE_ORDER = List.of(
            "Architecture", "Backend", "Data", "AI & LLM", "Cloud & DevOps", "Leadership");
    private final SkillRepository skills;

    public List<Skill> findAll() {
        return skills.findAllByOrderByCategoryAscSortOrderAscNameAsc().stream()
                .sorted(Comparator.comparingInt((Skill skill) -> categoryRank(skill.getCategory()))
                        .thenComparingInt(Skill::getSortOrder)
                        .thenComparing(Skill::getName))
                .toList();
    }

    public Map<String, List<Skill>> findGrouped() {
        return findAll().stream().collect(Collectors.groupingBy(
                Skill::getCategory, LinkedHashMap::new, Collectors.toList()));
    }

    public List<Skill> findFeatured() {
        return skills.findByFeaturedTrueOrderBySortOrderAscNameAsc();
    }

    public Optional<Skill> findById(Long id) {
        return skills.findById(id);
    }

    public long count() {
        return skills.count();
    }

    @Transactional
    public Skill save(Skill skill) {
        return skills.save(skill);
    }

    @Transactional
    public void delete(Long id) {
        skills.deleteById(id);
    }

    private int categoryRank(String category) {
        int rank = PRACTICE_ORDER.indexOf(category);
        return rank >= 0 ? rank : PRACTICE_ORDER.size();
    }
}
