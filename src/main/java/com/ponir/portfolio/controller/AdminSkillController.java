package com.ponir.portfolio.controller;

import com.ponir.portfolio.domain.Skill;
import com.ponir.portfolio.repository.SkillRepository;
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
@RequestMapping("/admin/skills")
public class AdminSkillController {
    private final SkillRepository skills;

    public AdminSkillController(SkillRepository skills) { this.skills = skills; }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("skills", skills.findAllByOrderByCategoryAscSortOrderAscNameAsc());
        return "admin/skills/list";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("skill", new Skill());
        return "admin/skills/form";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("skill", skills.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        return "admin/skills/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute Skill skill, BindingResult result,
                       RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) return "admin/skills/form";
        skills.save(skill);
        redirectAttributes.addFlashAttribute("success", "Skill saved.");
        return "redirect:/admin/skills";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        skills.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Skill deleted.");
        return "redirect:/admin/skills";
    }
}
