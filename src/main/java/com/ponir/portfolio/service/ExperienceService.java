package com.ponir.portfolio.service;

import com.ponir.portfolio.domain.Experience;
import com.ponir.portfolio.repository.ExperienceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExperienceService {
    private final ExperienceRepository experiences;

    public List<Experience> findAll() {
        return experiences.findAllByOrderBySortOrderAsc();
    }

    public List<Experience> findPreview(int limit) {
        return findAll().stream().limit(limit).toList();
    }

    public Optional<Experience> findById(Long id) {
        return experiences.findById(id);
    }

    public long count() {
        return experiences.count();
    }

    @Transactional
    public Experience save(Experience experience) {
        return experiences.save(experience);
    }

    @Transactional
    public void delete(Long id) {
        experiences.deleteById(id);
    }
}
