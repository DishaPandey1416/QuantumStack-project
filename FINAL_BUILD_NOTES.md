# AI Nexus Final Build Notes

This package is the reviewed final source build.

## Corrections made during final review

- Fixed missing JPA model imports in repository interfaces.
- Fixed invalid XML in `pom.xml` (`&` in project description).
- Protected `GET /api/auth/me` instead of bypassing it with all auth routes.
- Removed the student-accessible project approval endpoint; review approval is faculty/admin-only.
- Added explicit role protection to faculty/admin APIs.
- Added review mark limits: Review 0 = 0, Review 1 = 0–33, Review 2 = 0–17.
- Added review state/duplicate approval checks.
- Locked project editing after Review 0 submission.
- Enforced 3–4 members before project creation/review submission.
- Prevented students from joining multiple teams and duplicate pending invitations.
- Added self-invitation protection.
- Improved request validation and input length limits.
- Reworked Quantum Lab to use state amplitudes, making repeated H/X operations mathematically consistent.
- Added Quantum and Network input validation.
- Added Faculty demo account for local evaluation.
- Fixed MySQL connection URLs to include `allowPublicKeyRetrieval=true`.
- Kept H2 as the zero-setup default for local development.
- Rebuilt the dashboard JavaScript with safer HTML escaping and functional loading/error paths.
- Added multi-gate quantum input (`H, X, H`).
- Added faculty feedback during review approval.
- Added meaningful unit tests for the Quantum Lab and request model.
- Updated API and verification documentation.

## Verification performed in the packaging environment

- JavaScript syntax check: PASS.
- HTML parsing sanity check: PASS.
- Maven POM XML parsing: PASS.
- Docker Compose YAML parsing: PASS.
- Repository model-import scan: PASS.

The packaging environment does not contain Maven and cannot reach Maven Central, so the Spring Boot dependency-resolved build cannot be executed here. Run `mvn clean test` on the target Windows/JDK/Maven environment before the university demonstration.
