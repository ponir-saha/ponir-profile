package com.ponir.portfolio;

import com.ponir.portfolio.repository.BlogPostRepository;
import com.ponir.portfolio.repository.ContactMessageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityAndAdminTests {

    @Autowired MockMvc mockMvc;
    @Autowired ContactMessageRepository messages;
    @Autowired BlogPostRepository posts;

    @Test
    void adminRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminDashboardRendersForAdmin() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void experienceFormExposesStructuredDeliveryFields() throws Exception {
        mockMvc.perform(get("/admin/experience/new"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Project highlights")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Skills used")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Achievements")));
    }

    @Test
    void contactFormPersistsMessage() throws Exception {
        long before = messages.count();
        mockMvc.perform(post("/contact").with(csrf())
                        .param("name", "Test Visitor")
                        .param("email", "visitor@example.com")
                        .param("subject", "Architecture consultation")
                        .param("message", "I would like to discuss a platform."))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contact"));
        assertThat(messages.count()).isEqualTo(before + 1);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanCreateDraftWithGeneratedSlug() throws Exception {
        mockMvc.perform(post("/admin/blogs").with(csrf())
                        .param("title", "A New Architecture Note")
                        .param("slug", "")
                        .param("excerpt", "A concise excerpt for the article.")
                        .param("content", "The complete article content goes here.")
                        .param("category", "Architecture")
                        .param("status", "DRAFT"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/blogs"));
        assertThat(posts.findAllByOrderByUpdatedAtDesc())
                .anyMatch(post -> post.getSlug().equals("a-new-architecture-note"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanUploadAndServeABlogImage() throws Exception {
        byte[] png = new byte[]{(byte) 0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a};
        MockMultipartFile image = new MockMultipartFile(
                "featuredImage", "architecture.png", "image/png", png);

        mockMvc.perform(multipart("/admin/blogs").file(image).with(csrf())
                        .param("title", "An Illustrated Architecture Note")
                        .param("slug", "")
                        .param("excerpt", "A concise excerpt with an uploaded image.")
                        .param("content", "## Design\n\nThe complete illustrated article content.")
                        .param("category", "Architecture")
                        .param("status", "DRAFT"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/blogs"));

        String uploadedPath = posts.findAllByOrderByUpdatedAtDesc().stream()
                .filter(post -> post.getSlug().equals("an-illustrated-architecture-note"))
                .findFirst()
                .orElseThrow()
                .getFeaturedImageUrl();
        assertThat(uploadedPath).startsWith("/uploads/blog/").endsWith(".png");

        mockMvc.perform(get(uploadedPath))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/png"))
                .andExpect(content().bytes(png));
    }
}
