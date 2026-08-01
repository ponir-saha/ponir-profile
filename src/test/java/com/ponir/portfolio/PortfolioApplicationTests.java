package com.ponir.portfolio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PortfolioApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void homePageRendersSeededPortfolio() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(content().string(containsString("Ponir Kumer Saha")))
                .andExpect(content().string(containsString("aria-label=\"WhatsApp\"")))
                .andExpect(content().string(containsString("https://wa.me/8801713177318")))
                .andExpect(content().string(containsString("Production architecture")))
                .andExpect(content().string(containsString("Spring Services")))
                .andExpect(content().string(containsString("Kafka Events")))
                .andExpect(content().string(containsString("Learning Orbit")))
                .andExpect(content().string(containsString("Designing RAG Systems That Earn Trust")));
    }

    @Test
    void publicBlogAndProjectDetailsRender() throws Exception {
        mockMvc.perform(get("/blog/designing-rag-systems-that-earn-trust"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("retrieval product")))
                .andExpect(content().string(containsString("<h2>The architecture goal</h2>")))
                .andExpect(content().string(containsString("<pre><code class=\"language-java\">")));

        mockMvc.perform(get("/projects/itinerary-builder"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Travel operations SaaS")));
    }

    @Test
    void capabilitiesAndCompanyBrandingRender() throws Exception {
        mockMvc.perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Architecture")))
                .andExpect(content().string(containsString("AI &amp; LLM")))
                .andExpect(content().string(containsString("Define the system")))
                .andExpect(content().string(containsString("Build the product")))
                .andExpect(content().string(containsString("Operate and lead")));

        mockMvc.perform(get("/experience"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Allianz Technology")))
                .andExpect(content().string(containsString("Projects")))
                .andExpect(content().string(containsString("Skills")))
                .andExpect(content().string(containsString("Achievements")))
                .andExpect(content().string(containsString("AI-assisted insurance case-management platform")))
                .andExpect(content().string(not(containsString("company-logo"))));
    }
}
