# AI Nexus — Development Guidelines

## 1. Project Architecture
- Follow the existing Spring Boot and static frontend structure.
- Keep controllers, services, repositories, DTOs, models, and configuration responsibilities separate.
- Reuse existing components and APIs before adding alternatives.
- Avoid unnecessary dependencies and duplicate implementations.

## 2. Before Changing Code
- Inspect the existing implementation and its callers.
- Identify affected APIs, frontend views, data models, and tests.
- Make focused changes; do not rewrite unrelated working features.
- Never claim a feature works without verifying it.

## 3. Security
- Never commit passwords, tokens, private API keys, or local credentials.
- Keep local secrets in environment variables or ignored configuration files.
- Do not expose stack traces or internal exception details in API responses.
- Validate incoming data and enforce authorization on protected operations.

## 4. Backend Testing
- Run `mvn clean test` after backend changes.
- Add or update tests for new business logic and bug fixes.
- Test API success, validation failures, unauthorized access, and unexpected errors.
- Treat compiler warnings separately from build failures.

## 5. Frontend Verification
- Check the browser console and network requests.
- Verify loading, success, empty, and error states.
- Test navigation, forms, buttons, filters, and API integration.
- Do not consider a page complete just because it renders.

## 6. Database and Configuration
- Preserve existing database data unless a migration is required and reviewed.
- Document schema changes and configuration requirements.
- Never commit local database files, logs, or test credentials.
- Verify the default H2 setup and any supported MySQL setup separately.

## 7. Git Workflow
- Check `git status` before staging files.
- Stage only files related to the intended change.
- Run `git diff --check` before committing.
- Use clear, focused commit messages.
- Fetch and inspect remote changes before resolving divergence.
- Never force-push or discard remote work without an explicit recovery plan.

## 8. Definition of Done
A change is complete only when:
- The code compiles.
- Relevant automated tests pass.
- The affected feature is manually verified where needed.
- No unrelated files or secrets are included in the commit.
- Known limitations are documented honestly.