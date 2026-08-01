package com.ponir.portfolio.controller;

import com.ponir.portfolio.repository.BlogPostRepository;
import com.ponir.portfolio.repository.ContactMessageRepository;
import com.ponir.portfolio.repository.ExperienceRepository;
import com.ponir.portfolio.repository.ProjectRepository;
import com.ponir.portfolio.repository.SkillRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {
    private final BlogPostRepository posts;
    private final ProjectRepository projects;
    private final ExperienceRepository experiences;
    private final SkillRepository skills;
    private final ContactMessageRepository messages;

    public AdminDashboardController(BlogPostRepository posts, ProjectRepository projects,
                                    ExperienceRepository experiences, SkillRepository skills,
                                    ContactMessageRepository messages) {
        this.posts = posts;
        this.projects = projects;
        this.experiences = experiences;
        this.skills = skills;
        this.messages = messages;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("postCount", posts.count());
        model.addAttribute("projectCount", projects.count());
        model.addAttribute("experienceCount", experiences.count());
        model.addAttribute("skillCount", skills.count());
        model.addAttribute("messageCount", messages.count());
        model.addAttribute("recentPosts", posts.findAllByOrderByUpdatedAtDesc().stream().limit(5).toList());
        model.addAttribute("recentMessages", messages.findAllByOrderByCreatedAtDesc().stream().limit(5).toList());
        return "admin/dashboard";
    }
}
