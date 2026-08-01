package com.ponir.portfolio.service;

import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.regex.Pattern;

@Service
public class ArticleMarkupService {
    private static final Pattern SAFE_LANGUAGE = Pattern.compile("[a-zA-Z0-9#+.-]{1,30}");

    public String render(String source) {
        if (source == null || source.isBlank()) return "";

        StringBuilder html = new StringBuilder();
        StringBuilder paragraph = new StringBuilder();
        boolean inCode = false;
        boolean inList = false;

        for (String line : source.replace("\r\n", "\n").split("\n", -1)) {
            String trimmed = line.trim();

            if (trimmed.startsWith("```")) {
                flushParagraph(html, paragraph);
                if (inList) {
                    html.append("</ul>");
                    inList = false;
                }
                if (inCode) {
                    html.append("</code></pre>");
                    inCode = false;
                } else {
                    String language = trimmed.substring(3).trim();
                    html.append("<pre><code");
                    if (SAFE_LANGUAGE.matcher(language).matches()) {
                        html.append(" class=\"language-").append(language).append("\"");
                    }
                    html.append(">");
                    inCode = true;
                }
                continue;
            }

            if (inCode) {
                html.append(HtmlUtils.htmlEscape(line)).append('\n');
                continue;
            }

            if (trimmed.isBlank()) {
                flushParagraph(html, paragraph);
                if (inList) {
                    html.append("</ul>");
                    inList = false;
                }
                continue;
            }

            if (trimmed.startsWith("## ") || trimmed.startsWith("### ")) {
                flushParagraph(html, paragraph);
                if (inList) {
                    html.append("</ul>");
                    inList = false;
                }
                int level = trimmed.startsWith("### ") ? 3 : 2;
                html.append("<h").append(level).append(">")
                        .append(renderInline(trimmed.substring(level + 1)))
                        .append("</h").append(level).append(">");
                continue;
            }

            if (trimmed.startsWith("- ")) {
                flushParagraph(html, paragraph);
                if (!inList) {
                    html.append("<ul>");
                    inList = true;
                }
                html.append("<li>").append(renderInline(trimmed.substring(2))).append("</li>");
                continue;
            }

            if (inList) {
                html.append("</ul>");
                inList = false;
            }
            if (!paragraph.isEmpty()) paragraph.append(' ');
            paragraph.append(trimmed);
        }

        flushParagraph(html, paragraph);
        if (inList) html.append("</ul>");
        if (inCode) html.append("</code></pre>");
        return html.toString();
    }

    private void flushParagraph(StringBuilder html, StringBuilder paragraph) {
        if (paragraph.isEmpty()) return;
        html.append("<p>").append(renderInline(paragraph.toString())).append("</p>");
        paragraph.setLength(0);
    }

    private String renderInline(String value) {
        StringBuilder rendered = new StringBuilder();
        String[] segments = value.split("`", -1);
        for (int index = 0; index < segments.length; index++) {
            String escaped = HtmlUtils.htmlEscape(segments[index]);
            rendered.append(index % 2 == 1 ? "<code>" + escaped + "</code>" : escaped);
        }
        return rendered.toString();
    }
}
