# hmpps-people-on-probation-api

[![Ministry of Justice Repository Compliance Badge](https://github-community.service.justice.gov.uk/repository-standards/api/hmpps-people-on-probation-api/badge?style=flat)](https://github-community.service.justice.gov.uk/repository-standards/hmpps-people-on-probation-api)
[![Docker Repository on ghcr](https://img.shields.io/badge/ghcr.io-repository-2496ED.svg?logo=docker)](https://ghcr.io/ministryofjustice/hmpps-people-on-probation-api)

API for exposing people-on-probation data backed by NDelius.

## Current endpoints

All endpoints are versioned under `/v1/person/{crn}`.

- `GET /name`
- `GET /personal-details`
- `GET /sentences`
- `GET /past-appointments?page=0&size=10`
- `GET /future-appointments?page=0&size=10`

## Tech stack

- Kotlin
- Spring Boot
- Spring Security resource server
- Spring WebClient for outbound NDelius calls
- WireMock-backed integration tests

## Configuration

The application expects these environment variables:

- `HMPPS_AUTH_URL`
- `NDELIUS_API_URL`
- `NDELIUS_API_CLIENT_ID`
- `NDELIUS_API_CLIENT_SECRET`

`application.yml` defines those properties directly. Helm values provide them in deployed environments.

## Running locally

There are four local modes.

### 1. Real NDelius integration

Use this when you want actual responses from the dev NDelius environment.

Set:

```bash
export HMPPS_AUTH_URL="https://sign-in-dev.hmpps.service.justice.gov.uk/auth"
export NDELIUS_API_URL="https://manage-my-community-sentence-and-delius-dev.hmpps.service.justice.gov.uk/"
export NDELIUS_API_CLIENT_ID="<real-client-id>"
export NDELIUS_API_CLIENT_SECRET="<real-client-secret>"
```

Run with:

```bash
./gradlew bootRun --args='--spring.profiles.active=dev,local'
```

Or in IntelliJ:

- Main class: `uk.gov.justice.digital.hmpps.peopleonprobationapi.PeopleOnProbationApiKt`
- Active profiles: `dev,local`
- Environment variables: the four variables above

Why `local`:

- `dev` gives dev URLs and Swagger
- `local` enables the local inbound auth bypass for manual testing only

With those profiles, Swagger is available at:

- `http://localhost:8080/swagger-ui/index.html`
- `http://localhost:8080/v3/api-docs`

Then call the API without an inbound bearer token, for example:

```bash
curl -i 'http://localhost:8080/v1/person/X990176/name'
curl -i 'http://localhost:8080/v1/person/X990176/personal-details'
curl -i 'http://localhost:8080/v1/person/X990176/sentences'
curl -i 'http://localhost:8080/v1/person/X990176/past-appointments?page=0&size=10'
curl -i 'http://localhost:8080/v1/person/X990176/future-appointments?page=0&size=10'
```

Important:

- the `local` profile bypasses inbound auth only
- outbound NDelius auth still uses the real client credentials above
- do not use the `local` profile in deployed environments

### 2. Mocked/local-only mode

Use this when you only need local startup or test support.

The repo includes `docker-compose.yml` with local `hmpps-auth`:

```bash
docker compose pull && docker compose up --scale hmpps-people-on-probation-api=0
```

That starts only local auth on port `8090`.

This is useful for fully local or mocked development, but not for real NDelius integration. A token from local `hmpps-auth` will not be accepted by the dev NDelius environment.

### 3. Local build and development in Docker with real NDelius integration

Use this when you want to build the application locally, run it in Docker, and still call the real dev NDelius environment.

The `Dockerfile` relies on the application being built first.

1. Build the jar files

```bash
./gradlew clean assemble
```

2. Copy the jar files to the base directory so the Docker build can find them

```bash
cp build/libs/*.jar .
```

3. Build the Docker image with the required build arguments

```bash
docker build \
  -t hmpps-people-on-probation-api:local \
  --build-arg GIT_REF=21345 \
  --build-arg GIT_BRANCH=bob \
  --build-arg BUILD_NUMBER=$(date '+%Y-%m-%d') \
  .
```

4. Run the Docker image with real dev auth and NDelius configuration

```bash
docker run \
  -p 8080:8080 \
  -e HMPPS_AUTH_URL="https://sign-in-dev.hmpps.service.justice.gov.uk/auth" \
  -e NDELIUS_API_URL="https://manage-my-community-sentence-and-delius-dev.hmpps.service.justice.gov.uk/" \
  -e NDELIUS_API_CLIENT_ID="<real-client-id>" \
  -e NDELIUS_API_CLIENT_SECRET="<real-client-secret>" \
  hmpps-people-on-probation-api:local
```

This mode does not use the `local` profile, so inbound authentication is not bypassed. If you want to call the running container manually, provide a valid inbound bearer token or use mode 1 instead.

### 4. Run the latest published image from the container registry

Use this when you want to run the latest published `hmpps-people-on-probation-api` image directly, without building locally.

Pull the latest image:

```bash
docker pull ghcr.io/ministryofjustice/hmpps-people-on-probation-api:latest
```

Run it with real dev auth and NDelius configuration:

```bash
docker run \
  -p 8080:8080 \
  -e HMPPS_AUTH_URL="https://sign-in-dev.hmpps.service.justice.gov.uk/auth" \
  -e NDELIUS_API_URL="https://manage-my-community-sentence-and-delius-dev.hmpps.service.justice.gov.uk/" \
  -e NDELIUS_API_CLIENT_ID="<real-client-id>" \
  -e NDELIUS_API_CLIENT_SECRET="<real-client-secret>" \
  -e SPRING_PROFILES_ACTIVE=dev \
  ghcr.io/ministryofjustice/hmpps-people-on-probation-api:latest
```

If you prefer using the image reference from `docker-compose.yml`, pull and run that service image directly:

```bash
docker compose pull hmpps-people-on-probation-api
docker run \
  -p 8080:8080 \
  -e HMPPS_AUTH_URL="https://sign-in-dev.hmpps.service.justice.gov.uk/auth" \
  -e NDELIUS_API_URL="https://manage-my-community-sentence-and-delius-dev.hmpps.service.justice.gov.uk/" \
  -e NDELIUS_API_CLIENT_ID="<real-client-id>" \
  -e NDELIUS_API_CLIENT_SECRET="<real-client-secret>" \
  -e SPRING_PROFILES_ACTIVE=dev \
  ghcr.io/ministryofjustice/hmpps-people-on-probation-api:latest
```

Like mode 3, this mode does not use the `local` profile, so inbound authentication is not bypassed.


## Running tests

Run the full suite:

```bash
./gradlew test
```

Run focused endpoint tests:

```bash
./gradlew test \
  --tests 'uk.gov.justice.digital.hmpps.peopleonprobationapi.integration.person.PersonNameTest' \
  --tests 'uk.gov.justice.digital.hmpps.peopleonprobationapi.integration.person.PersonPersonalDetailsTest' \
  --tests 'uk.gov.justice.digital.hmpps.peopleonprobationapi.integration.person.PersonSentenceProgressTest' \
  --tests 'uk.gov.justice.digital.hmpps.peopleonprobationapi.integration.person.PersonPastAppointmentsTest' \
  --tests 'uk.gov.justice.digital.hmpps.peopleonprobationapi.integration.person.PersonFutureAppointmentsTest'
```

Integration tests use:

- WireMock HMPPS Auth on `18090`
- WireMock NDelius on `18091`

They do not call Docker `hmpps-auth` or the real NDelius environment.

## Project structure

The main structure is feature-first with shared outbound integration:

```text
src/main/kotlin/uk/gov/justice/digital/hmpps/peopleonprobationapi
├── config
├── ndelius
│   └── client
└── person
    ├── api
    ├── domain
    └── service
```

Rules used in the current implementation:

- controllers handle HTTP only
- services orchestrate use cases
- `ndelius/client` owns downstream transport and mapping
- domain models are separate from API response models

## Deployment config

Relevant Helm files:

- base values: `helm_deploy/hmpps-people-on-probation-api/values.yaml`
- dev overrides: `helm_deploy/values-dev.yaml`
- preprod overrides: `helm_deploy/values-preprod.yaml`
- prod overrides: `helm_deploy/values-prod.yaml`

Deployed environments should provide:

- `HMPPS_AUTH_URL`
- `NDELIUS_API_URL`
- `NDELIUS_API_CLIENT_ID`
- `NDELIUS_API_CLIENT_SECRET`

They should not use the `local` profile.
