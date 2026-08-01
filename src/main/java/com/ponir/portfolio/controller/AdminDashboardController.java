package com.ponir.portfolio.controller;

import com.ponir.portfolio.service.BlogPostService;
import com.ponir.portfolio.service.ContactMessageService;
import com.ponir.portfolio.service.ExperienceService;
import com.ponir.portfolio.service.ProjectService;
import com.ponir.portfolio.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {
    private final BlogPostService blogPostService;
    private final ProjectService projectService;
    private final ExperienceService experienceService;
    private final SkillService skillService;
    private final ContactMessageService contactMessageService;

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("postCount", blogPostService.count());
        model.addAttribute("projectCount", projectService.count());
        model.addAttribute("experienceCount", experienceService.count());
        model.addAttribute("skillCount", skillService.count());
        model.addAttribute("messageCount", contactMessageService.count());
        model.addAttribute("recentPosts", blogPostService.findRecent(5));
        model.addAttribute("recentMessages", contactMessageService.findRecent(5));
        return "admin/dashboard";
    }
}
