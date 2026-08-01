package com.ponir.portfolio.repository;

import com.ponir.portfolio.domain.BlogPost;
import com.ponir.portfolio.domain.PublishStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    List<BlogPost> findByStatusOrderByPublishedAtDesc(PublishStatus status);
    List<BlogPost> findByStatusOrderByPublishedAtDesc(PublishStatus status, Pageable pageable);
    List<BlogPost> findByStatusAndFeaturedTrueOrderByPublishedAtDesc(PublishStatus status);
    Optional<BlogPost> findBySlugAndStatus(String slug, PublishStatus status);
    List<BlogPost> findAllByOrderByUpdatedAtDesc();
    boolean existsBySlugAndIdNot(String slug, Long id);

    @Query("""
        select p from BlogPost p
        where p.status = com.ponir.portfolio.domain.PublishStatus.PUBLISHED
          and (lower(p.title) like lower(concat('%', :query, '%'))
            or lower(p.excerpt) like lower(concat('%', :query, '%'))
            or lower(p.category) like lower(concat('%', :query, '%')))
        order by p.publishedAt desc
        """)
    List<BlogPost> searchPublished(@Param("query") String query);
}
