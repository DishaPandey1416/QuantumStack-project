# AI Nexus — Intelligent Computing & Java Project Platform

AI Nexus is a full-stack Spring Boot web application for a Java project-based evaluation. It combines three educational computing labs with an academic project workflow:

- **AI Recommendation Engine** — explainable scoring across AI, Quantum and Network approaches.
- **Quantum Lab** — educational 1–8 qubit state-vector simulation with H/X gates.
- **Network Lab** — topology/performance simulation for Star, Ring, Mesh, Tree and Bus.
- **Team Workspace** — 3–4 member team formation, invitations and roles.
- **Review Workflow** — Review 0 approval gate, Review 1 (33 marks) and Review 2 (17 marks).
- **Faculty/Admin Dashboard** — protected review approvals and feedback.
- **History** — saved recommendation and simulation activity.

## Architecture

This is intentionally a **single Spring Boot application** rather than separate React and backend repositories:

```text
src/main/java/                 Backend: controllers, services, JPA models
src/main/resources/static/     Frontend: HTML, CSS, JavaScript
src/main/resources/            H2 default + MySQL profile configuration
docs/                           Architecture, API, flows, roadmap, checklist
```

The frontend is served by Spring Boot, so there is no separate Node/Vite build step.

## Requirements

- Java 17+ (Java 21 recommended)
- Maven 3.9+

## Run locally (zero-setup H2)

From the directory containing `pom.xml`:

```powershell
mvn clean test
mvn spring-boot:run
```

Open:

```text
http://localhost:8080/
```

The default database is a local H2 file under `./data/ainexus`, so MySQL is not required for the normal demo.

## Demo accounts

When `DEMO_DATA=true` (the default local setting):

| Role | Email | Password |
|---|---|---|
| Admin | admin@ainexus.local | Admin@123 |
| Faculty | faculty@ainexus.local | Faculty@123 |

For a real deployment, set `DEMO_DATA=false` and use strong credentials managed outside source control.

## MySQL / Docker

A MySQL profile and Docker Compose setup are included. MySQL URLs already include `allowPublicKeyRetrieval=true` to avoid the common MySQL 8 authentication error seen with local development setups.

```powershell
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

Or:

```powershell
docker compose up --build
```

## Important project workflow

```text
Register / Login
      ↓
Create team
      ↓
Invite students
      ↓
3–4 members required
      ↓
Create project
      ↓
Review 0 → Faculty approval
      ↓
Review 1 → Faculty approval (33)
      ↓
Review 2 → Faculty approval (17)
      ↓
Completed
```

## Verification note

The source package includes automated tests and has been statically reviewed for the final build. A Maven runtime cannot be executed in every packaging environment, so the authoritative final verification is `mvn clean test` and a local browser/API smoke test on the target machine.

See `docs/VERIFICATION.md` for the complete checklist.
