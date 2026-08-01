package com.ponir.portfolio.controller;

import com.ponir.portfolio.domain.SiteProfile;
import com.ponir.portfolio.repository.SiteProfileRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/profile")
public class AdminProfileController {
    private final SiteProfileRepository profiles;

    public AdminProfileController(SiteProfileRepository profiles) {
        this.profiles = profiles;
    }

    @GetMapping
    public String edit(Model model) {
        model.addAttribute("profileForm", profiles.findById(1L).orElseGet(SiteProfile::new));
        return "admin/profile";
    }

    @PostMapping
    public String update(@Valid @ModelAttribute("profileForm") SiteProfile profile,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) return "admin/profile";
        profile.setId(1L);
        profiles.save(profile);
        redirectAttributes.addFlashAttribute("success", "Profile content updated.");
        return "redirect:/admin/profile";
    }
}
