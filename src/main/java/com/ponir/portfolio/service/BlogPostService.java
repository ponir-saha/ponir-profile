package com.ponir.portfolio.service;

import com.ponir.portfolio.domain.BlogPost;
import com.ponir.portfolio.domain.PublishStatus;
import com.ponir.portfolio.repository.BlogPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlogPostService {
    private final BlogPostRepository posts;
    private final SlugService slugService;
    private final ImageStorageService imageStorage;

    public List<BlogPost> findLatestPublished(int limit) {
        return posts.findByStatusOrderByPublishedAtDesc(PublishStatus.PUBLISHED, PageRequest.of(0, limit));
    }

    public List<BlogPost> findPublished(String query) {
        String cleanQuery = query == null ? "" : query.trim();
        return cleanQuery.isBlank()
                ? posts.findByStatusOrderByPublishedAtDesc(PublishStatus.PUBLISHED)
                : posts.searchPublished(cleanQuery);
    }

    public Optional<BlogPost> findPublishedBySlug(String slug) {
        return posts.findBySlugAndStatus(slug, PublishStatus.PUBLISHED);
    }

    public List<BlogPost> findRelated(BlogPost post, int limit) {
        return posts.findByStatusOrderByPublishedAtDesc(PublishStatus.PUBLISHED).stream()
                .filter(candidate -> !candidate.getId().equals(post.getId()))
                .limit(limit)
                .toList();
    }

    public List<BlogPost> findAll() {
        return posts.findAllByOrderByUpdatedAtDesc();
    }

    public List<BlogPost> findRecent(int limit) {
        return findAll().stream().limit(limit).toList();
    }

    public Optional<BlogPost> findById(Long id) {
        return posts.findById(id);
    }

    public String resolveSlug(BlogPost post) {
        String candidate = post.getSlug() == null || post.getSlug().isBlank()
                ? post.getTitle() : post.getSlug();
        return slugService.slugify(candidate);
    }

    public boolean slugExists(String slug, Long currentId) {
        return posts.existsBySlugAndIdNot(slug, currentId == null ? -1L : currentId);
    }

    public long count() {
        return posts.count();
    }

    @Transactional
    public BlogPost save(BlogPost form, String slug, MultipartFile featuredImage) {
        BlogPost target = form.getId() == null ? new BlogPost() : posts.findById(form.getId())
                .orElseThrow(() -> new IllegalArgumentException("Blog post not found: " + form.getId()));
        target.setTitle(form.getTitle());
        target.setSlug(slug);
        target.setExcerpt(form.getExcerpt());
        target.setContent(form.getContent());
        target.setCategory(form.getCategory());
        target.setFeaturedImageUrl(form.getFeaturedImageUrl());
        target.setStatus(form.getStatus());
        target.setFeatured(form.isFeatured());
        if (target.getStatus() == PublishStatus.PUBLISHED && target.getPublishedAt() == null) {
            target.setPublishedAt(Instant.now());
        }
        String uploadedImage = imageStorage.store(featuredImage, "blog");
        if (uploadedImage != null) target.setFeaturedImageUrl(uploadedImage);
        return posts.save(target);
    }

    @Transactional
    public void delete(Long id) {
        posts.deleteById(id);
    }
}
