alter table experiences add column logo_url varchar(500);

update experiences set logo_url = '/images/companies/allianz.svg' where company = 'Allianz Technology';
update experiences set logo_url = '/images/companies/bjit.svg' where company = 'BJIT Limited';
update experiences set logo_url = '/images/companies/dnet.svg' where company = 'Dnet - A Social Enterprise';
update experiences set logo_url = '/images/companies/datasoft.svg' where company = 'DataSoft Systems Bangladesh';
update experiences set logo_url = '/images/companies/krungsri.svg' where company = 'Ayudhya Capital Services';
update experiences set logo_url = '/images/companies/wallenius-wilhelmsen.svg' where company = 'Wallenius Wilhelmsen Logistics';

update blog_posts set content = '## The architecture goal

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

Evaluation belongs in the delivery pipeline. Maintain representative questions, expected evidence, and failure cases. The goal is a dependable capability that helps people make better decisions while making uncertainty visible.'
where slug = 'designing-rag-systems-that-earn-trust';

update blog_posts set content = '## Start with the decision

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

The best review leaves the implementation team with greater clarity and ownership. It reduces hidden risk while preserving momentum.'
where slug = 'architecture-review-every-team-needs';

update blog_posts set content = '## The reliability problem

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

A CDC connector such as Debezium can stream committed outbox records to Kafka. Patterns remove categories of failure; they do not remove operational responsibility.'
where slug = 'outbox-and-cdc-reliability-pair';
