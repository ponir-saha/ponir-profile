package com.ponir.portfolio;

import com.ponir.portfolio.service.ArticleMarkupService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleMarkupServiceTests {
    private final ArticleMarkupService renderer = new ArticleMarkupService();

    @Test
    void rendersStructuredTechnicalContentAndEscapesHtml() {
        String html = renderer.render("""
                ## Plan

                - Keep controllers thin

                ```java
                return "<unsafe>";
                ```
                """);

        assertThat(html)
                .contains("<h2>Plan</h2>")
                .contains("<ul><li>Keep controllers thin</li></ul>")
                .contains("<pre><code class=\"language-java\">")
                .contains("&lt;unsafe&gt;")
                .doesNotContain("<unsafe>");
    }
}
