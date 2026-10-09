# AI Nexus Verification Checklist

## Build verification

Run from the directory containing `pom.xml`:

```powershell
mvn clean test
```

The build should finish with `BUILD SUCCESS` and execute the JUnit tests under `src/test`.

## Runtime smoke test

```powershell
mvn spring-boot:run
```

Then open `http://localhost:8080/`.

### Authentication

- Register a new student account.
- Confirm automatic login.
- Sign out.
- Sign in again.
- Verify `/api/auth/me` works only with a valid bearer session.
- Verify an expired/invalid token returns 401.

### Team workflow

- Create a team as a student.
- Register two additional students.
- Invite them by email.
- Accept invitations from the invited accounts.
- Verify team size cannot exceed four.
- Verify a student cannot join two teams.
- Verify duplicate pending invitations are rejected.

### Project and reviews

- Leader creates project only after team reaches 3–4 members.
- Submit Review 0.
- Confirm Review 1 remains locked until faculty/admin approval.
- Log in as admin or faculty.
- Approve Review 0 with 0 marks and optional feedback.
- Submit Review 1.
- Approve Review 1 with a value from 0–33.
- Submit Review 2.
- Approve Review 2 with a value from 0–17.
- Confirm project reaches `COMPLETED`.
- Confirm students cannot approve reviews.
- Confirm project details are locked after Review 0 submission.

### Recommendation engine

- Submit a data/ML problem and verify AI scoring.
- Submit an optimization problem and verify Quantum scoring.
- Submit a networking problem and verify Network scoring.
- Confirm each run appears in History.

### Quantum lab

- Run `H` on 1 qubit and expect 50% / 50% probabilities.
- Run `H,H` and expect the state to return to |0⟩.
- Run `X` and expect |1⟩ with 100% probability.
- Verify invalid qubit counts and unsupported gates are rejected.

### Network lab

- Test Star, Ring, Mesh, Tree and Bus.
- Verify node limits 2–100.
- Verify bandwidth and latency validation.
- Confirm throughput, packet loss and jitter are returned.
- Confirm runs appear in History.

### Security / access control

- Student cannot access faculty review APIs.
- Faculty/admin can access review queue.
- Unauthenticated API calls return 401.
- Passwords are stored using BCrypt hashes, not plaintext.
- Tokens are random, time-limited bearer sessions.

## Packaging status

This package is designed to be the final source distribution. Before university submission, execute the build and smoke-test checklist above on the same Windows/JDK/Maven environment that will be used for the demonstration.
