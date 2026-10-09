# AI Nexus — Java Project

Spring Boot application with a static HTML/CSS/JavaScript frontend. Backend packages are under `src/main/java/com/ainexus`; frontend assets are served from `src/main/resources/static`.

## Requirements
- JDK 21 (project targets Java 17 bytecode)
- Maven 3.9+

## Run in VS Code / Windows
1. Extract the ZIP and open the extracted folder containing `pom.xml` in VS Code.
2. In the VS Code terminal, run `mvn clean test`.
3. If the build reports `BUILD SUCCESS`, run `mvn spring-boot:run`.
4. Visit `http://localhost:8080` and verify `http://localhost:8080/api/health`.

VS Code tasks are included in `.vscode/tasks.json` (`AI Nexus: Maven Tests` and `AI Nexus: Run Spring Boot`). Additional Windows notes are in `OPEN-IN-VSCODE-WINDOWS.txt`.

## Default database
The default configuration uses a local H2 file database. Do not delete the `data` folder if you need to keep its data. MySQL configuration is available separately in `src/main/resources/application-mysql.properties`.

## Before submission
- Run `mvn clean test` and keep the terminal output.
- Manually test registration, login, `/api/auth/me`, logout, team invitations, project/review workflows, recommendation, simulation, and history screens.
- Never include real secrets, tokens, or personal credentials in Git or the submission ZIP.

## Verification status
The packaging pass checked the frontend JavaScript syntax and includes fixes for two previously observed authentication errors (transaction handling and lazy user loading). A fresh Maven build and full browser/API end-to-end pass could not be run in the packaging environment because Maven is unavailable there. Verify locally before claiming all features are fully working.
