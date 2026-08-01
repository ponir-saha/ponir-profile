package com.ponir.portfolio.controller;

import com.ponir.portfolio.domain.Project;
import com.ponir.portfolio.repository.ProjectRepository;
import com.ponir.portfolio.service.SlugService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/projects")
public class AdminProjectController {
    private final ProjectRepository projects;
    private final SlugService slugService;

    public AdminProjectController(ProjectRepository projects, SlugService slugService) {
        this.projects = projects;
        this.slugService = slugService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("projects", projects.findAllByOrderBySortOrderAsc());
        return "admin/projects/list";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("project", new Project());
        return "admin/projects/form";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("project", projects.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        return "admin/projects/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute Project form, BindingResult result,
                       RedirectAttributes redirectAttributes) {
        String slug = slugService.slugify(form.getSlug() == null || form.getSlug().isBlank() ? form.getTitle() : form.getSlug());
        Long currentId = form.getId() == null ? -1L : form.getId();
        if (projects.existsBySlugAndIdNot(slug, currentId)) {
            result.rejectValue("slug", "duplicate", "This slug is already in use.");
        }
        if (result.hasErrors()) return "admin/projects/form";

        Project target = form.getId() == null ? new Project() : projects.findById(form.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        copy(form, target);
        target.setSlug(slug);
        projects.save(target);
        redirectAttributes.addFlashAttribute("success", "Project saved.");
        return "redirect:/admin/projects";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        projects.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Project deleted.");
        return "redirect:/admin/projects";
    }

    private void copy(Project from, Project to) {
        to.setTitle(from.getTitle());
        to.setEyebrow(from.getEyebrow());
        to.setSummary(from.getSummary());
        to.setDescription(from.getDescription());
        to.setTechStack(from.getTechStack());
        to.setGithubUrl(from.getGithubUrl());
        to.setLiveUrl(from.getLiveUrl());
        to.setFeatured(from.isFeatured());
        to.setPublished(from.isPublished());
        to.setSortOrder(from.getSortOrder());
    }
}
