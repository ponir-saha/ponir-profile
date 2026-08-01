package com.ponir.portfolio.controller;

import com.ponir.portfolio.domain.BlogPost;
import com.ponir.portfolio.domain.ContactMessage;
import com.ponir.portfolio.domain.PublishStatus;
import com.ponir.portfolio.repository.BlogPostRepository;
import com.ponir.portfolio.repository.ContactMessageRepository;
import com.ponir.portfolio.repository.ExperienceRepository;
import com.ponir.portfolio.repository.ProjectRepository;
import com.ponir.portfolio.repository.SkillRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PublicController {
    private final SkillRepository skills;
    private final ExperienceRepository experiences;
    private final ProjectRepository projects;
    private final BlogPostRepository posts;
    private final ContactMessageRepository messages;

    public PublicController(SkillRepository skills,
                            ExperienceRepository experiences,
                            ProjectRepository projects,
                            BlogPostRepository posts,
                            ContactMessageRepository messages) {
        this.skills = skills;
        this.experiences = experiences;
        this.projects = projects;
        this.posts = posts;
        this.messages = messages;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("featuredSkills", skills.findByFeaturedTrueOrderBySortOrderAscNameAsc());
        model.addAttribute("featuredProjects", projects.findByPublishedTrueAndFeaturedTrueOrderBySortOrderAsc());
        model.addAttribute("latestPosts", posts.findByStatusOrderByPublishedAtDesc(PublishStatus.PUBLISHED, PageRequest.of(0, 4)));
        model.addAttribute("experiencePreview", experiences.findAllByOrderBySortOrderAsc().stream().limit(3).toList());
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("skills", skills.findAllByOrderByCategoryAscSortOrderAscNameAsc());
        return "about";
    }

    @GetMapping("/experience")
    public String experience(Model model) {
        model.addAttribute("experiences", experiences.findAllByOrderBySortOrderAsc());
        return "experience";
    }

    @GetMapping("/projects")
    public String projects(Model model) {
        model.addAttribute("projects", projects.findByPublishedTrueOrderBySortOrderAsc());
        return "projects";
    }

    @GetMapping("/projects/{slug}")
    public String project(@PathVariable String slug, Model model) {
        model.addAttribute("project", projects.findBySlugAndPublishedTrue(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        return "project-detail";
    }

    @GetMapping("/blog")
    public String blog(@RequestParam(required = false, defaultValue = "") String query, Model model) {
        String cleanQuery = query.trim();
        model.addAttribute("query", cleanQuery);
        model.addAttribute("posts", cleanQuery.isBlank()
                ? posts.findByStatusOrderByPublishedAtDesc(PublishStatus.PUBLISHED)
                : posts.searchPublished(cleanQuery));
        return "blog";
    }

    @GetMapping("/blog/{slug}")
    public String post(@PathVariable String slug, Model model) {
        BlogPost post = posts.findBySlugAndStatus(slug, PublishStatus.PUBLISHED)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("post", post);
        model.addAttribute("relatedPosts", posts.findByStatusOrderByPublishedAtDesc(PublishStatus.PUBLISHED)
                .stream().filter(candidate -> !candidate.getId().equals(post.getId())).limit(2).toList());
        return "blog-detail";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        if (!model.containsAttribute("contactMessage")) {
            model.addAttribute("contactMessage", new ContactMessage());
        }
        return "contact";
    }

    @PostMapping("/contact")
    public String sendMessage(@Valid @ModelAttribute ContactMessage contactMessage,
                              BindingResult bindingResult,
                              @RequestParam(required = false, defaultValue = "") String website,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "contact";
        }
        if (!website.isBlank()) {
            redirectAttributes.addFlashAttribute("success", "Thanks for reaching out. Your message is now in my inbox.");
            return "redirect:/contact";
        }
        messages.save(contactMessage);
        redirectAttributes.addFlashAttribute("success", "Thanks for reaching out. Your message is now in my inbox.");
        return "redirect:/contact";
    }
}
