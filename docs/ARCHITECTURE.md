# AI Nexus — Final Architecture

```mermaid
flowchart TB
 U[Student / Faculty] --> UI[Responsive Web UI]
 UI --> AUTH[Auth API]
 UI --> TEAM[Team API]
 UI --> PROJECT[Project & Review API]
 UI --> REC[Recommendation API]
 UI --> LAB[Simulation API]

 AUTH --> AS[Auth Service]
 TEAM --> TS[Team Service]
 PROJECT --> PS[Project Service]
 REC --> RS[Recommendation Service]
 LAB --> SS[Simulation Service]

 AS --> TOK[Server Session Tokens]
 TS --> TEAMDB[(Team Tables)]
 PS --> REVIEW[(Project / Review Tables)]
 RS --> RECS[(Requirement / Recommendation)]
 SS --> SIM[(Simulation History)]
 TOK --> DB[(H2 / MySQL)]
 TEAMDB --> DB
 REVIEW --> DB
 RECS --> DB
 SIM --> DB

 REC --> AI[AI Scoring]
 REC --> Q[Quantum Fit Scoring]
 REC --> N[Network Fit Scoring]
 LAB --> QE[Quantum Engine]
 LAB --> NE[Network Engine]
```

## Security boundary

```text
Browser
  ↓ Bearer session token
AuthInterceptor
  ↓ authenticated User
Controller
  ↓
Service authorization
  ↓
Repository
```

Passwords are BCrypt-hashed. Privileged roles are never accepted from public registration.
