# MAMS Backend

Spring Boot 3 REST API, built with Java 17 and Maven.

## Local Run

Run MySQL locally, set `SPRING_DATASOURCE_PASSWORD`, then run:

```powershell
mvn spring-boot:run
```

The API listens on port 8081 by default. Set `SERVER_PORT` to override it.

## Render + Railway Deployment

Create a Render Web Service from this repository and select **Docker**. The Dockerfile is at the repository root. Render supplies `PORT` automatically.

Create a MySQL service on Railway and enable its public TCP proxy. Render cannot use Railway's private hostname, so configure these Render environment variables using Railway's public host and port:

- `SPRING_DATASOURCE_URL`: `jdbc:mysql://PUBLIC_HOST:PUBLIC_PORT/DATABASE?sslMode=REQUIRED&serverTimezone=UTC`
- `SPRING_DATASOURCE_USERNAME`: Railway MySQL username
- `SPRING_DATASOURCE_PASSWORD`: Railway MySQL password
- `APP_JWT_SECRET`: a long, randomly generated secret
- `CORS_ALLOWED_ORIGINS`: the exact deployed Vercel origin, such as `https://your-project.vercel.app`

Do not commit credentials or use the local development JWT secret in production. Hibernate updates the schema automatically on startup.