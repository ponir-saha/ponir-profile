package com.ponir.portfolio.controller;

import com.ponir.portfolio.domain.BlogPost;
import com.ponir.portfolio.domain.PublishStatus;
import com.ponir.portfolio.service.BlogPostService;
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
@RequestMapping("/admin/blogs")
@RequiredArgsConstructor
public class AdminBlogController {
    private final BlogPostService blogPostService;

    @ModelAttribute("statuses")
    public PublishStatus[] statuses() { return PublishStatus.values(); }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("posts", blogPostService.findAll());
        return "admin/blogs/list";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("post", new BlogPost());
        return "admin/blogs/form";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("post", blogPostService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        return "admin/blogs/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("post") BlogPost form, BindingResult result,
                       @RequestParam(required = false) MultipartFile featuredImage,
                       RedirectAttributes redirectAttributes) {
        String slug = blogPostService.resolveSlug(form);
        if (blogPostService.slugExists(slug, form.getId())) {
            result.rejectValue("slug", "duplicate", "This slug is already in use.");
        }
        if (result.hasErrors()) return "admin/blogs/form";

        try {
            blogPostService.save(form, slug, featuredImage);
        } catch (ImageStorageException exception) {
            result.reject("imageUpload", exception.getMessage());
            return "admin/blogs/form";
        }
        redirectAttributes.addFlashAttribute("success", "Blog post saved.");
        return "redirect:/admin/blogs";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        blogPostService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Blog post deleted.");
        return "redirect:/admin/blogs";
    }
}
