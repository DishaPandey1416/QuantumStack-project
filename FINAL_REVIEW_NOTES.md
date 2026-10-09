# AI Nexus — Final Review Notes

## Changes in this package
- Fixed the frontend state/method name collision: `App.team()` and `App.project()` remain callable while data is stored in `currentTeam` and `currentProject`.
- Made `GET /api/projects/me` return a stable empty-state JSON object when the authenticated user has not joined a team yet. Creating/saving a project still enforces team membership and 3–4 member rules.
- Added explicit generic types to list-stream mappings in service classes.
- Added ICO and SVG favicons and referenced them from static HTML pages.
- Kept local databases, build output, and local credential files out of the archive.

## Verification status
- ZIP integrity is checked after packaging.
- JavaScript syntax is checked with Node.js when available.
- Java/Maven tests must be run on a machine with Maven and dependency access using `mvn clean test`. This build environment did not include Maven, so this package must not be described as fully integration-tested or guaranteed bug-free until that command and browser workflows pass.

## Manual acceptance checks
1. Register a new test user; verify duplicate email and invalid inputs show helpful errors.
2. Login, reload `/app.html`, call `/api/auth/me`, then logout and confirm protected APIs reject the old token.
3. Open Project & Reviews without a team; confirm no HTTP 400 and an explanatory empty state.
4. Create a team; invite two registered users; accept invitations; verify member count and leader permissions.
5. Create project, submit Review 0, approve as faculty, submit Review 1 and Review 2 according to their configured states/deadlines.
6. Run recommendation, quantum and network simulations; verify results are saved to history.
7. Inspect browser console/network for unexpected 4xx/5xx responses.
