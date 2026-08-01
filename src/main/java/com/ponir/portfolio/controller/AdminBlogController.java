package com.ponir.portfolio.controller;

import com.ponir.portfolio.domain.BlogPost;
import com.ponir.portfolio.domain.PublishStatus;
import com.ponir.portfolio.repository.BlogPostRepository;
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

import java.time.Instant;

@Controller
@RequestMapping("/admin/blogs")
public class AdminBlogController {
    private final BlogPostRepository posts;
    private final SlugService slugService;

    public AdminBlogController(BlogPostRepository posts, SlugService slugService) {
        this.posts = posts;
        this.slugService = slugService;
    }

    @ModelAttribute("statuses")
    public PublishStatus[] statuses() { return PublishStatus.values(); }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("posts", posts.findAllByOrderByUpdatedAtDesc());
        return "admin/blogs/list";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("post", new BlogPost());
        return "admin/blogs/form";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("post", posts.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        return "admin/blogs/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("post") BlogPost form, BindingResult result,
                       RedirectAttributes redirectAttributes) {
        String slug = slugService.slugify(form.getSlug() == null || form.getSlug().isBlank() ? form.getTitle() : form.getSlug());
        Long currentId = form.getId() == null ? -1L : form.getId();
        if (posts.existsBySlugAndIdNot(slug, currentId)) {
            result.rejectValue("slug", "duplicate", "This slug is already in use.");
        }
        if (result.hasErrors()) return "admin/blogs/form";

        BlogPost target = form.getId() == null ? new BlogPost() : posts.findById(form.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        copy(form, target);
        target.setSlug(slug);
        if (target.getStatus() == PublishStatus.PUBLISHED && target.getPublishedAt() == null) {
            target.setPublishedAt(Instant.now());
        }
        posts.save(target);
        redirectAttributes.addFlashAttribute("success", "Blog post saved.");
        return "redirect:/admin/blogs";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        posts.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Blog post deleted.");
        return "redirect:/admin/blogs";
    }

    private void copy(BlogPost from, BlogPost to) {
        to.setTitle(from.getTitle());
        to.setExcerpt(from.getExcerpt());
        to.setContent(from.getContent());
        to.setCategory(from.getCategory());
        to.setFeaturedImageUrl(from.getFeaturedImageUrl());
        to.setStatus(from.getStatus());
        to.setFeatured(from.isFeatured());
    }
}
