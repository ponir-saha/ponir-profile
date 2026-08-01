package com.ponir.portfolio.config;

import com.ponir.portfolio.domain.BlogPost;
import com.ponir.portfolio.domain.Experience;
import com.ponir.portfolio.domain.Project;
import com.ponir.portfolio.domain.PublishStatus;
import com.ponir.portfolio.domain.SiteProfile;
import com.ponir.portfolio.domain.Skill;
import com.ponir.portfolio.repository.BlogPostRepository;
import com.ponir.portfolio.repository.ExperienceRepository;
import com.ponir.portfolio.repository.ProjectRepository;
import com.ponir.portfolio.repository.SiteProfileRepository;
import com.ponir.portfolio.repository.SkillRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Configuration
public class SeedDataConfiguration {

    @Bean
    ApplicationRunner seedPortfolioData(SiteProfileRepository profiles,
                                        SkillRepository skills,
                                        ExperienceRepository experiences,
                                        ProjectRepository projects,
                                        BlogPostRepository posts) {
        return args -> {
            seedProfile(profiles);
            seedSkills(skills);
            seedExperience(experiences);
            seedProjects(projects);
            seedPosts(posts);
        };
    }

    private void seedProfile(SiteProfileRepository repository) {
        if (repository.existsById(1L)) return;
        SiteProfile p = new SiteProfile();
        p.setId(1L);
        p.setFullName("Ponir Kumer Saha");
        p.setHeadline("Principal Engineer · System Architect · AI/RAG Builder");
        p.setHeroEyebrow("From architecture to production");
        p.setHeroTitle("I design resilient systems that turn ambitious ideas into dependable products.");
        p.setIntroduction("Principal Engineer and System Architect with 13+ years of experience delivering cloud-native, distributed, AI-powered, and high-availability platforms. I work where backend engineering, product strategy, and engineering leadership meet.");
        p.setAbout("I specialise in scalable backend systems, microservices, event-driven architecture, and applied AI. My work spans financial services, education technology, navigation, logistics, and enterprise platforms.\n\nI enjoy translating complex business problems into clear technical direction, mentoring engineering teams, and shipping systems that are observable, secure, and built to evolve. More recently, I have focused on Retrieval-Augmented Generation, document intelligence, vector search, and responsible LLM integration in enterprise workflows.");
        p.setEmail("ponir.saha@gmail.com");
        p.setPhone("+880 1713 177318");
        p.setLocation("Dhaka, Bangladesh · Working remotely");
        p.setLinkedinUrl("https://linkedin.com/in/ponirsaha");
        p.setGithubUrl("https://github.com/ponir-saha");
        p.setWhatsappUrl("https://wa.me/8801713177318");
        p.setPortraitUrl("/images/ponir-saha-portrait.png");
        p.setResumeUrl("/files/ponir-kumer-saha-resume.pdf");
        p.setAvailability("Open to remote architecture, principal engineering, and AI platform opportunities.");
        p.setYearsExperience(13);
        p.setProjectsDelivered(30);
        p.setEngineersMentored(10);
        repository.save(p);
    }

    private void seedSkills(SkillRepository repository) {
        if (repository.count() > 0) return;
        repository.saveAll(List.of(
                skill("Distributed Systems", "Architecture", 95, 1, true),
                skill("Event-Driven Architecture", "Architecture", 94, 2, true),
                skill("DDD · CQRS · Outbox", "Architecture", 90, 3, false),
                skill("Java", "Backend", 96, 1, true),
                skill("Spring Boot & Spring Cloud", "Backend", 96, 2, true),
                skill("Kafka · Redis · WebSocket", "Backend", 92, 3, true),
                skill("PostgreSQL · OpenSearch", "Data", 91, 1, true),
                skill("MongoDB · MySQL · Oracle", "Data", 86, 2, false),
                skill("Spring AI · LangChain4j", "AI & LLM", 91, 1, true),
                skill("RAG · Vector Search", "AI & LLM", 93, 2, true),
                skill("OpenAI · Gemini · AI Agents", "AI & LLM", 89, 3, true),
                skill("AWS · GCP · Kubernetes", "Cloud & DevOps", 88, 1, true),
                skill("Docker · CI/CD · Observability", "Cloud & DevOps", 91, 2, false),
                skill("Architecture Leadership", "Leadership", 94, 1, true),
                skill("Mentoring & Delivery", "Leadership", 93, 2, true)
        ));
    }

    private Skill skill(String name, String category, int proficiency, int order, boolean featured) {
        Skill skill = new Skill();
        skill.setName(name);
        skill.setCategory(category);
        skill.setProficiency(proficiency);
        skill.setSortOrder(order);
        skill.setFeatured(featured);
        return skill;
    }

    private void seedExperience(ExperienceRepository repository) {
        if (repository.count() > 0) return;
        repository.saveAll(List.of(
                experience("Allianz Technology", "Senior Full Stack Developer", "/images/companies/allianz.svg", "Remote", "Apr 2026", "Present", true,
                        "Designed an AI-assisted insurance case-management modernization on Spring Boot microservices. Integrated RAG, OpenSearch, vector embeddings, OpenAI/Gemini, and document intelligence to improve contextual search and knowledge access.",
                        "AI-assisted insurance case-management platform\nEnterprise knowledge retrieval and document intelligence",
                        "Java, Spring Boot, Spring AI, RAG, OpenSearch, OpenAI, Gemini",
                        "Modernized contextual search across insurance workflows\nDesigned traceable AI-assisted knowledge access", 1),
                experience("BJIT Limited", "Principal Software Engineer / Architect", "/images/companies/bjit.svg", "Dhaka, Bangladesh", "Feb 2022", "Mar 2026", false,
                        "Architected microservices platforms with Spring Cloud, reducing API latency by 40%. Designed high-volume event-driven systems with Kafka, Redis, and WebSocket, and led a cross-functional team of 10+ engineers across architecture, governance, delivery, and mentoring.",
                        "Cloud-native microservices platform\nHigh-volume real-time event platform",
                        "Java, Spring Boot, Spring Cloud, Kafka, Redis, WebSocket, Kubernetes",
                        "Reduced API latency by 40%\nLed and mentored a cross-functional team of 10+ engineers", 2),
                experience("Dnet - A Social Enterprise", "Lead Java Developer", "/images/companies/dnet.svg", "Dhaka, Bangladesh", "Nov 2020", "Feb 2022", false,
                        "Led Kafka-based event-driven architecture, cloud migration, and technical roadmaps. Improved reliability to 99.9% uptime while aligning engineering delivery with organisational goals.",
                        "Kafka-based event platform\nCloud migration and technical roadmap",
                        "Java, Spring Boot, Kafka, Cloud Architecture, Observability",
                        "Improved platform reliability to 99.9% uptime\nAligned architecture decisions with delivery goals", 3),
                experience("DataSoft Systems Bangladesh", "Software Engineer", "/images/companies/datasoft.svg", "Dhaka, Bangladesh", "Nov 2019", "Oct 2020", false,
                        "Developed cloud-native backend APIs with Java, Spring Boot, and Node.js. Standardised testing practices and raised code coverage to 80%.",
                        "Cloud-native backend API platform\nAutomated engineering quality initiative",
                        "Java, Spring Boot, Node.js, REST APIs, Automated Testing",
                        "Raised automated test coverage to 80%\nStandardized testing practices across delivery", 4),
                experience("Ayudhya Capital Services", "Senior Software Developer", "/images/companies/krungsri.svg", "Bangkok, Thailand", "Aug 2017", "May 2019", false,
                        "Designed secure financial systems, improved transaction reliability, reduced downtime by 20%, and improved database-query performance by 35%.",
                        "Secure financial transaction services\nDatabase performance modernization",
                        "Java, Spring, Oracle, Secure Transactions, Performance Tuning",
                        "Reduced production downtime by 20%\nImproved database-query performance by 35%", 5),
                experience("Wallenius Wilhelmsen Logistics", "Transform Developer", "/images/companies/wallenius-wilhelmsen.svg", "Bangkok, Thailand", "Feb 2012", "Jul 2017", false,
                        "Automated logistics data transformations with 99.9% accuracy and built custom PDF-generation tools that significantly reduced manual financial-document processing.",
                        "Global logistics data transformations\nFinancial-document PDF automation",
                        "Java, Data Transformation, XML, PDF Generation, Logistics Systems",
                        "Delivered logistics transformations with 99.9% accuracy\nReduced manual financial-document processing", 6)
        ));
    }

    private Experience experience(String company, String title, String logoUrl, String location, String start, String end,
                                  boolean current, String summary, String projectHighlights, String skills,
                                  String achievements, int order) {
        Experience item = new Experience();
        item.setCompany(company);
        item.setTitle(title);
        item.setLogoUrl(logoUrl);
        item.setLocation(location);
        item.setStartLabel(start);
        item.setEndLabel(end);
        item.setCurrentRole(current);
        item.setSummary(summary);
        item.setProjectHighlights(projectHighlights);
        item.setSkills(skills);
        item.setAchievements(achievements);
        item.setSortOrder(order);
        return item;
    }

    private void seedProjects(ProjectRepository repository) {
        if (repository.count() > 0) return;
        repository.saveAll(List.of(
                project("Learning Orbit", "learning-orbit", "AI learning platform",
                        "A chapter-aware learning platform that turns approved textbooks into guided study, AI tutoring, practice exams, and teacher-reviewed assessments.",
                        "Architected a multi-role education SaaS with contextual tutoring, OCR, chapter extraction, assessment generation, instant feedback, and real-time progress insights for students, teachers, parents, and administrators.",
                        "Spring Boot, Angular, PostgreSQL, OpenSearch, Spring AI, LangChain4j, Gemini, OpenAI, OCR, WebSocket",
                        "https://github.com/ponir-saha/learning-orbit", true, 1),
                project("Itinerary Builder", "itinerary-builder", "Travel operations SaaS",
                        "A full-stack platform for complex multi-city itineraries, booking operations, pricing, payments, and document generation.",
                        "Built role-based workflows for super admins, admins, and agents, including catalogue approvals, itinerary versioning, payment tracking, real-time collaboration, and generated itinerary, voucher, and invoice PDFs.",
                        "Spring Boot, Angular, PostgreSQL, Spring Security, Flyway, Docker Compose, OpenSearch, PDF generation",
                        "https://github.com/ponir-saha/itinerary-builder", true, 2)
        ));
    }

    private Project project(String title, String slug, String eyebrow, String summary, String description,
                            String techStack, String github, boolean featured, int order) {
        Project item = new Project();
        item.setTitle(title);
        item.setSlug(slug);
        item.setEyebrow(eyebrow);
        item.setSummary(summary);
        item.setDescription(description);
        item.setTechStack(techStack);
        item.setGithubUrl(github);
        item.setFeatured(featured);
        item.setPublished(true);
        item.setSortOrder(order);
        return item;
    }

    private void seedPosts(BlogPostRepository repository) {
        if (repository.count() > 0) return;
        Instant now = Instant.now();
        repository.saveAll(List.of(
                post("Designing RAG Systems That Earn Trust", "designing-rag-systems-that-earn-trust", "AI Engineering",
                        "A practical architecture checklist for retrieval quality, citations, evaluation, observability, and safe enterprise adoption.",
                        """
                        ## The architecture goal

                        A useful RAG system is more than an embedding model attached to a chat box. It is a retrieval product whose answers must be traceable, measurable, and safe.

                        ## Delivery plan

                        - Govern the knowledge lifecycle and access rules.
                        - Model chunks and metadata around the source structure.
                        - Measure retrieval quality before judging generation.
                        - Trace every answer back to evidence.

                        ## A boundary that keeps retrieval testable

                        ```java
                        public interface KnowledgeRetriever {
                            List<Evidence> retrieve(String question, AccessScope scope);
                        }
                        ```

                        Evaluation belongs in the delivery pipeline. The goal is a dependable capability that helps people make better decisions while making uncertainty visible.
                        """.strip(),
                        true, now.minus(2, ChronoUnit.DAYS)),
                post("The Architecture Review I Wish Every Team Had", "architecture-review-every-team-needs", "Engineering Leadership",
                        "A lightweight review method that improves important decisions without slowing a delivery team to a halt.",
                        """
                        ## Start with the decision

                        Architecture review works best as decision support, not a gatekeeping ceremony. A useful review makes context, constraints, trade-offs, and reversibility visible.

                        ## Review plan

                        - State the business outcome and material failure modes.
                        - Make data ownership, interfaces, security, cost, and operations explicit.
                        - Record the decision, its assumptions, and the date to revisit it.

                        ## A compact decision record

                        ```text
                        Decision: Publish domain events through a transactional outbox
                        Because: Business state and event intent must commit atomically
                        Revisit: When throughput or ordering requirements materially change
                        ```

                        The best review leaves the implementation team with greater clarity and ownership. It reduces hidden risk while preserving momentum.
                        """.strip(),
                        true, now.minus(9, ChronoUnit.DAYS)),
                post("Outbox and CDC: A Practical Reliability Pair", "outbox-and-cdc-reliability-pair", "Distributed Systems",
                        "How transactional outbox and change-data capture work together to make event publication reliable and observable.",
                        """
                        ## The reliability problem

                        Distributed transactions between a database and a broker are a common source of subtle failure. The transactional outbox pattern keeps the domain change and event record in one local transaction.

                        ## Implementation plan

                        - Write business state and the outbox record in one transaction.
                        - Stream committed records through CDC.
                        - Make consumers idempotent and monitor connector lag.
                        - Define schema evolution and retention policies.

                        ## Transaction boundary

                        ```java
                        @Transactional
                        public Order place(OrderCommand command) {
                            Order order = orders.save(Order.from(command));
                            outbox.save(OrderPlaced.from(order));
                            return order;
                        }
                        ```

                        A CDC connector such as Debezium can stream committed outbox records to Kafka. Patterns remove categories of failure; they do not remove operational responsibility.
                        """.strip(),
                        false, now.minus(18, ChronoUnit.DAYS))
        ));
    }

    private BlogPost post(String title, String slug, String category, String excerpt, String content,
                          boolean featured, Instant publishedAt) {
        BlogPost item = new BlogPost();
        item.setTitle(title);
        item.setSlug(slug);
        item.setCategory(category);
        item.setExcerpt(excerpt);
        item.setContent(content);
        item.setStatus(PublishStatus.PUBLISHED);
        item.setFeatured(featured);
        item.setPublishedAt(publishedAt);
        return item;
    }
}
