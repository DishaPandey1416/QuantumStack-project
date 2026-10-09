# AI Nexus — Final Complete Project Blueprint

## 1. Product

AI Nexus is a Java/Spring Boot project platform that combines the university project-review workflow with future-computing utilities.

### Core areas

1. Authentication
2. Team formation
3. Project title/description
4. Review 0 / Review 1 / Review 2 workflow
5. Faculty/admin approval
6. Intelligent recommendation engine
7. Quantum simulation lab
8. Network simulation lab
9. History and activity
10. Responsive premium UI

---

## 2. Technology

```text
Java 17
Spring Boot 3.4.13
Spring Web
Spring Data JPA
Spring Validation
Spring Security Crypto / BCrypt
H2 development database
MySQL production option
HTML5 + CSS3 + JavaScript
Docker / Docker Compose
```

The application deliberately uses a server-side bearer session token instead of pretending that a client-side localStorage user object is sufficient authentication.

---

## 3. Final Architecture

```text
Browser
  │
  ▼
Responsive Web UI
  │
  ├── Authentication
  ├── Team Workspace
  ├── Project & Reviews
  ├── Recommendation Engine
  ├── Quantum Lab
  ├── Network Lab
  └── History
  │
  ▼
Spring Boot REST API
  │
  ├── AuthService
  ├── TeamService
  ├── ProjectService
  ├── RecommendationService
  └── SimulationService
  │
  ▼
JPA Repositories
  │
  ▼
H2 / MySQL
```

---

## 4. Database Model

```text
users
  │
  ├── auth_tokens
  ├── teams ← team_members → users
  ├── team_invitations
  ├── requirements → recommendations
  └── simulations

teams → projects → review_submissions
```

### Important rules

- Passwords are BCrypt-hashed.
- Public registration always creates `STUDENT`.
- Only an existing leader can invite members.
- A team cannot exceed four members.
- Review 0 cannot be submitted with fewer than three members.
- Review stages are server-controlled.
- Faculty/Admin approval is server-authorized.
- Review 1 deadline: 10 October 2026.
- Review 2 deadline: 15 November 2026.

---

## 5. Authentication Flow

```text
Register
  ↓
Validate
  ↓
Hash password
  ↓
Save STUDENT
  ↓
Create session token
  ↓
Return token
```

```text
Login
  ↓
Find user
  ↓
BCrypt match
  ↓
Create 12-hour session token
  ↓
Browser sends Bearer token
  ↓
AuthInterceptor
  ↓
Authenticated User
```

---

## 6. Team Flow

```text
Student registers
      ↓
Create team
      ↓
Leader receives team code
      ↓
Leader invites registered students
      ↓
Student accepts
      ↓
Team reaches 3–4 members
      ↓
Project submission enabled
```

---

## 7. Review State Machine

```text
REVIEW_0_PENDING
      │
      │ submit Review 0
      ▼
REVIEW_0_SUBMITTED
      │
      │ faculty/admin approve
      ▼
REVIEW_1_OPEN
      │
      │ submit before 10 Oct 2026
      ▼
REVIEW_1_SUBMITTED
      │
      │ faculty/admin approve
      ▼
REVIEW_2_OPEN
      │
      │ submit before 15 Nov 2026
      ▼
REVIEW_2_SUBMITTED
      │
      │ faculty/admin approve
      ▼
COMPLETED
```

---

## 8. Recommendation Engine

The baseline is intentionally deterministic and explainable.

```text
Requirement
    ↓
Normalize text
    ↓
Extract workload signals
    ↓
Score AI
Score Quantum
Score Network
    ↓
Select highest score
    ↓
Calculate confidence
    ↓
Generate explanation
    ↓
Save recommendation
```

Signals include:

- Machine learning
- Dataset/data processing
- Prediction/classification
- Optimization
- Quantum terminology
- Routing/networking
- Latency
- Bandwidth
- Packet/throughput
- Dataset scale
- Performance priority

This is a strong project baseline because it is reproducible and demonstrable. A real ML/LLM model can later augment it without replacing the core architecture.

---

## 9. Quantum Lab

### Supported baseline

- 1–8 qubits
- H gate
- X gate
- State-probability output
- Saved simulation history

### Flow

```text
Choose qubits
    ↓
Choose gate
    ↓
Initialize |0...0>
    ↓
Apply operation
    ↓
Calculate probabilities
    ↓
Visualize result
    ↓
Save history
```

---

## 10. Network Lab

### Inputs

- Topology
- Node count
- Bandwidth
- Latency

### Outputs

- Estimated throughput
- Packet loss
- Jitter
- Latency

### Flow

```text
Topology
  ↓
Nodes
  ↓
Bandwidth + latency
  ↓
Simulation model
  ↓
Performance metrics
  ↓
Visualization
```

---

## 11. UI/UX System

### Design direction

- Dark future-computing visual language
- Teal/cyan accent system
- Glass-like panels
- Responsive layouts
- Strong typography
- Micro-interactions
- Status pills
- Metric cards
- Result visualization
- Mobile breakpoints
- Empty/error/locked states

### Pages

```text
Landing
Login
Register
Workspace
 ├── Overview
 ├── Project & Reviews
 ├── Team
 ├── Recommendation
 ├── Quantum Lab
 ├── Network Lab
 ├── History
 └── Faculty Review (privileged users)
```

---

## 12. API Groups

```text
/api/health
/api/auth/*
/api/teams/*
/api/projects/*
/api/recommendations/*
/api/simulations/*
/api/admin/*
```

See `docs/API.md` for request examples.

---

## 13. Deployment

### Zero setup

```bash
mvn clean test
mvn spring-boot:run
```

The default database is a local H2 file database.

### MySQL

Use environment variables and the MySQL profile/configuration.

### Docker

```bash
docker compose up --build
```

---

## 14. Project Demonstration

```text
1. Open landing page
2. Register student
3. Login
4. Create team
5. Register two more students and accept invitations
6. Create project
7. Submit Review 0
8. Login as admin/faculty
9. Approve Review 0
10. Login as student
11. Submit Review 1
12. Approve Review 1
13. Run Recommendation Engine
14. Run Quantum Lab
15. Run Network Lab
16. Show History
17. Submit Review 2
18. Approve Review 2
19. Show COMPLETED state
```

---

## 15. Definition of Complete

The project is considered functionally complete when:

```text
✓ Build configuration exists
✓ Backend starts
✓ Database initializes
✓ Authentication works
✓ Protected APIs reject unauthenticated users
✓ Teams work
✓ Invitations work
✓ 3–4 member constraint works
✓ Project creation works
✓ Review gates work
✓ Faculty approval works
✓ Deadlines are server-enforced
✓ Recommendation works
✓ Quantum simulation works
✓ Network simulation works
✓ History works
✓ UI is responsive
✓ Docker configuration exists
✓ Documentation exists
```

For the final machine-level verification, run `mvn clean test` or `docker compose up --build` on a machine with dependency/network access. See `docs/VERIFICATION.md` for the checks performed in the packaging environment.
