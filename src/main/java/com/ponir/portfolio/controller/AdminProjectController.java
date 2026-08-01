package com.ponir.portfolio.controller;

import com.ponir.portfolio.domain.Project;
import com.ponir.portfolio.service.ProjectService;
import com.ponir.portfolio.service.ImageStorageException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/projects")
@RequiredArgsConstructor
public class AdminProjectController {
    private final ProjectService projectService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("projects", projectService.findAll());
        return "admin/projects/list";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("project", new Project());
        return "admin/projects/form";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("project", projectService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        return "admin/projects/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute Project form, BindingResult result,
                       @RequestParam(required = false) MultipartFile projectImage,
                       RedirectAttributes redirectAttributes) {
        String slug = projectService.resolveSlug(form);
        if (projectService.slugExists(slug, form.getId())) {
            result.rejectValue("slug", "duplicate", "This slug is already in use.");
        }
        if (result.hasErrors()) return "admin/projects/form";

        try {
            projectService.save(form, slug, projectImage);
        } catch (ImageStorageException exception) {
            result.reject("imageUpload", exception.getMessage());
            return "admin/projects/form";
        }
        redirectAttributes.addFlashAttribute("success", "Project saved.");
        return "redirect:/admin/projects";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        projectService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Project deleted.");
        return "redirect:/admin/projects";
    }
}
