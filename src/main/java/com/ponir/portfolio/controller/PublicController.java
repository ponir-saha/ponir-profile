package com.ponir.portfolio.controller;

import com.ponir.portfolio.domain.BlogPost;
import com.ponir.portfolio.domain.ContactMessage;
import com.ponir.portfolio.service.ArticleMarkupService;
import com.ponir.portfolio.service.BlogPostService;
import com.ponir.portfolio.service.ContactMessageService;
import com.ponir.portfolio.service.ExperienceService;
import com.ponir.portfolio.service.ProjectService;
import com.ponir.portfolio.service.SkillService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class PublicController {
    private final SkillService skillService;
    private final ExperienceService experienceService;
    private final ProjectService projectService;
    private final BlogPostService blogPostService;
    private final ContactMessageService contactMessageService;
    private final ArticleMarkupService articleMarkup;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("featuredSkills", skillService.findFeatured());
        model.addAttribute("featuredProjects", projectService.findFeatured());
        model.addAttribute("latestPosts", blogPostService.findLatestPublished(4));
        model.addAttribute("experiencePreview", experienceService.findPreview(3));
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("skillsByCategory", skillService.findGrouped());
        return "about";
    }

    @GetMapping("/experience")
    public String experience(Model model) {
        model.addAttribute("experiences", experienceService.findAll());
        return "experience";
    }

    @GetMapping("/projects")
    public String projects(Model model) {
        model.addAttribute("projects", projectService.findPublished());
        return "projects";
    }

    @GetMapping("/projects/{slug}")
    public String project(@PathVariable String slug, Model model) {
        model.addAttribute("project", projectService.findPublishedBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        return "project-detail";
    }

    @GetMapping("/blog")
    public String blog(@RequestParam(required = false, defaultValue = "") String query, Model model) {
        String cleanQuery = query.trim();
        model.addAttribute("query", cleanQuery);
        model.addAttribute("posts", blogPostService.findPublished(cleanQuery));
        return "blog";
    }

    @GetMapping("/blog/{slug}")
    public String post(@PathVariable String slug, Model model) {
        BlogPost post = blogPostService.findPublishedBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("post", post);
        model.addAttribute("articleHtml", articleMarkup.render(post.getContent()));
        model.addAttribute("relatedPosts", blogPostService.findRelated(post, 2));
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
        contactMessageService.save(contactMessage);
        redirectAttributes.addFlashAttribute("success", "Thanks for reaching out. Your message is now in my inbox.");
        return "redirect:/contact";
    }
}
