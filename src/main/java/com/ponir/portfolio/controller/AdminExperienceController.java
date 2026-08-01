package com.ponir.portfolio.controller;

import com.ponir.portfolio.domain.Experience;
import com.ponir.portfolio.repository.ExperienceRepository;
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
@RequestMapping("/admin/experience")
public class AdminExperienceController {
    private final ExperienceRepository experiences;

    public AdminExperienceController(ExperienceRepository experiences) { this.experiences = experiences; }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("experiences", experiences.findAllByOrderBySortOrderAsc());
        return "admin/experience/list";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("experience", new Experience());
        return "admin/experience/form";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("experience", experiences.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        return "admin/experience/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute Experience experience, BindingResult result,
                       RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) return "admin/experience/form";
        experiences.save(experience);
        redirectAttributes.addFlashAttribute("success", "Experience saved.");
        return "redirect:/admin/experience";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        experiences.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Experience deleted.");
        return "redirect:/admin/experience";
    }
}
