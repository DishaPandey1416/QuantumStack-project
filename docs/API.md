# AI Nexus API Reference

All `/api/**` routes except `/api/health`, `/api/auth/login`, `/api/auth/register` and `/api/auth/logout` require:

```http
Authorization: Bearer <session-token>
```

## Authentication

### Register
`POST /api/auth/register`

```json
{"name":"Aarif","email":"student@example.com","password":"secret123"}
```

### Login
`POST /api/auth/login`

Returns a 12-hour server-side session token.

### Current user
`GET /api/auth/me`

### Logout
`POST /api/auth/logout`

## Teams

`GET /api/teams/me`

`POST /api/teams`

```json
{"name":"Quantum Stack"}
```

`POST /api/teams/invite`

```json
{"email":"member@example.com"}
```

`GET /api/teams/invitations`

`POST /api/teams/invitations/{id}/respond?accept=true`

Teams are limited to 4 members. Review 0 requires at least 3 members.

## Project

`GET /api/projects/me`

`POST /api/projects/me`

```json
{"title":"AI Nexus","description":"Intelligent computing project..."}
```

`POST /api/projects/reviews`

```json
{"reviewNo":0,"content":"Project title, problem and proposed solution."}
```

Review state machine:

```text
REVIEW_0_PENDING
       ↓ submit R0
REVIEW_0_SUBMITTED
       ↓ faculty/admin approve
REVIEW_1_OPEN
       ↓ submit R1
REVIEW_1_SUBMITTED
       ↓ faculty/admin approve
REVIEW_2_OPEN
       ↓ submit R2
REVIEW_2_SUBMITTED
       ↓ faculty/admin approve
COMPLETED
```

## Recommendation

`POST /api/recommendations/analyze`

```json
{
  "title":"Large-scale anomaly detection",
  "description":"Analyze a very large dataset for patterns and anomalies.",
  "problemType":"DATA",
  "datasetSize":"VERY_LARGE",
  "priority":"HIGH"
}
```

`GET /api/recommendations/history`

## Quantum

`POST /api/simulations/quantum`

```json
{"qubits":2,"gates":["H"]}
```

Supports 1–8 qubits and basic H/X educational probability simulation.

## Network

`POST /api/simulations/network`

```json
{"topology":"STAR","nodes":12,"bandwidth":100,"latency":20}
```

Returns estimated throughput, packet loss, jitter and latency.

## Faculty/Admin

`GET /api/admin/projects`

`POST /api/admin/projects/{id}/reviews/{reviewNo}/approve?marks=33&feedback=Good`

Only `ADMIN` and `FACULTY` roles may access these endpoints.
