alter table experiences add column project_highlights text;
alter table experiences add column skills text;
alter table experiences add column achievements text;

update experiences
set project_highlights = 'AI-assisted insurance case-management platform
Enterprise knowledge retrieval and document intelligence',
    skills = 'Java, Spring Boot, Spring AI, RAG, OpenSearch, OpenAI, Gemini',
    achievements = 'Modernized contextual search across insurance workflows
Designed traceable AI-assisted knowledge access'
where company = 'Allianz Technology';

update experiences
set project_highlights = 'Cloud-native microservices platform
High-volume real-time event platform',
    skills = 'Java, Spring Boot, Spring Cloud, Kafka, Redis, WebSocket, Kubernetes',
    achievements = 'Reduced API latency by 40%
Led and mentored a cross-functional team of 10+ engineers'
where company = 'BJIT Limited';

update experiences
set project_highlights = 'Kafka-based event platform
Cloud migration and technical roadmap',
    skills = 'Java, Spring Boot, Kafka, Cloud Architecture, Observability',
    achievements = 'Improved platform reliability to 99.9% uptime
Aligned architecture decisions with delivery goals'
where company = 'Dnet - A Social Enterprise';

update experiences
set project_highlights = 'Cloud-native backend API platform
Automated engineering quality initiative',
    skills = 'Java, Spring Boot, Node.js, REST APIs, Automated Testing',
    achievements = 'Raised automated test coverage to 80%
Standardized testing practices across delivery'
where company = 'DataSoft Systems Bangladesh';

update experiences
set project_highlights = 'Secure financial transaction services
Database performance modernization',
    skills = 'Java, Spring, Oracle, Secure Transactions, Performance Tuning',
    achievements = 'Reduced production downtime by 20%
Improved database-query performance by 35%'
where company = 'Ayudhya Capital Services';

update experiences
set project_highlights = 'Global logistics data transformations
Financial-document PDF automation',
    skills = 'Java, Data Transformation, XML, PDF Generation, Logistics Systems',
    achievements = 'Delivered logistics transformations with 99.9% accuracy
Reduced manual financial-document processing'
where company = 'Wallenius Wilhelmsen Logistics';
