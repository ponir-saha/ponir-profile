package com.ponir.portfolio.service;

import com.ponir.portfolio.domain.Project;
import com.ponir.portfolio.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projects;
    private final SlugService slugService;
    private final ImageStorageService imageStorage;

    public List<Project> findPublished() {
        return projects.findByPublishedTrueOrderBySortOrderAsc();
    }

    public List<Project> findFeatured() {
        return projects.findByPublishedTrueAndFeaturedTrueOrderBySortOrderAsc();
    }

    public List<Project> findAll() {
        return projects.findAllByOrderBySortOrderAsc();
    }

    public Optional<Project> findPublishedBySlug(String slug) {
        return projects.findBySlugAndPublishedTrue(slug);
    }

    public Optional<Project> findById(Long id) {
        return projects.findById(id);
    }

    public String resolveSlug(Project project) {
        String candidate = project.getSlug() == null || project.getSlug().isBlank()
                ? project.getTitle() : project.getSlug();
        return slugService.slugify(candidate);
    }

    public boolean slugExists(String slug, Long currentId) {
        return projects.existsBySlugAndIdNot(slug, currentId == null ? -1L : currentId);
    }

    public long count() {
        return projects.count();
    }

    @Transactional
    public Project save(Project form, String slug, MultipartFile projectImage) {
        Project target = form.getId() == null ? new Project() : projects.findById(form.getId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + form.getId()));
        target.setTitle(form.getTitle());
        target.setSlug(slug);
        target.setEyebrow(form.getEyebrow());
        target.setSummary(form.getSummary());
        target.setDescription(form.getDescription());
        target.setTechStack(form.getTechStack());
        target.setImageUrl(form.getImageUrl());
        target.setGithubUrl(form.getGithubUrl());
        target.setLiveUrl(form.getLiveUrl());
        target.setFeatured(form.isFeatured());
        target.setPublished(form.isPublished());
        target.setSortOrder(form.getSortOrder());
        String uploadedImage = imageStorage.store(projectImage, "projects");
        if (uploadedImage != null) target.setImageUrl(uploadedImage);
        return projects.save(target);
    }

    @Transactional
    public void delete(Long id) {
        projects.deleteById(id);
    }
}
