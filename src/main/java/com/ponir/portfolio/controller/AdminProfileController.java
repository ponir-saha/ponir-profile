package com.ponir.portfolio.controller;

import com.ponir.portfolio.domain.SiteProfile;
import com.ponir.portfolio.service.ProfileService;
import com.ponir.portfolio.service.ImageStorageException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/profile")
@RequiredArgsConstructor
public class AdminProfileController {
    private final ProfileService profileService;

    @GetMapping
    public String edit(Model model) {
        model.addAttribute("profileForm", profileService.getProfile());
        return "admin/profile";
    }

    @PostMapping
    public String update(@Valid @ModelAttribute("profileForm") SiteProfile profile,
                         BindingResult bindingResult,
                         @RequestParam(required = false) MultipartFile portraitImage,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) return "admin/profile";
        try {
            profileService.save(profile, portraitImage);
        } catch (ImageStorageException exception) {
            bindingResult.reject("imageUpload", exception.getMessage());
            return "admin/profile";
        }
        redirectAttributes.addFlashAttribute("success", "Profile content updated.");
        return "redirect:/admin/profile";
    }
}
