# Strim Backend

Backend-tjenesten for **Strim**, en applikasjon for håndtering av møter og arrangementer.

Backenden har ansvar for:

- Opprette og administrere arrangementer
- Håndtere deltakere (bli med / melde seg av)
- Administrere kategorier
- Sende kalenderinvitasjoner (Microsoft Graph / Azure-integrasjon)
- Sikre endepunkter med OAuth2 / JWT
- Lagring av data i PostgreSQL

---

## 🛠 Teknologistack

### Språk og rammeverk
- Kotlin
- Spring Boot
- Spring Security (OAuth2 Resource Server + JWT)
- Spring Data JPA
- Maven

### Database
- PostgreSQL (Google Cloud SQL i produksjon)

### Autentisering
- Azure AD (OAuth2 / JWT-validering)

### Sky og deploy
- Docker
- Google Cloud
- GitHub Actions (CI/CD)

---

## 🔐 Sikkerhet

Produksjonsprofilen (`!local`) bruker:

- JWT-validering via Spring Security OAuth2 Resource Server
- Autentisering kreves for:
    - Opprette arrangementer
    - Bli med på arrangementer
    - Melde seg av arrangementer
- Åpne GET-endepunkter for:
    - Hente arrangementer
    - Hente kategorier

Sikkerhetskonfigurasjonen er profilbasert.

---

## 🗄 Database

Applikasjonen bruker PostgreSQL.

Sørg for at du har en kjørende PostgreSQL-instans lokalt, eller konfigurer miljøvariabler for ekstern database.

### Nødvendige miljøvariabler (eksempel)

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/strim
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=yourpassword
SPRING_PROFILES_ACTIVE=local
```

I produksjon må også SSL og sky-credentials konfigureres.

---

## ▶️ Starte applikasjonen lokalt

### 1. Klon repoet

```bash
git clone <repo-url>
cd strim-backend
```

### 2. Start PostgreSQL (eksempel med Docker)

```bash
docker run --name strim-postgres \
  -e POSTGRES_DB=strim \
  -e POSTGRES_USER=app \
  -e POSTGRES_PASSWORD=app \
  -p 5432:5432 \
  -d postgres:15
```

### 3. Bygg prosjektet

```bash
mvn clean install
```

### 4. Start applikasjonen

```bash
mvn spring-boot:run
```

Alternativt via IntelliJ:
- Åpne prosjektet
- Kjør hovedklassen for Spring Boot

Backenden starter på:

```
http://localhost:8080
```

---

## 🐳 Kjøre med Docker

### Bygg image

```bash
docker compose up -d
```

### Start container

```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/strim \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=postgres \
  strim-backend
```

---

## 🌍 Profiler

- `local` → deaktiverer produksjonssikkerhet
- `!local` (standard) → aktiverer JWT-sikkerhet og produksjonskonfigurasjon

Sett profil:

```bash
SPRING_PROFILES_ACTIVE=local
```

---

## 📅 Kalenderintegrasjon

Backenden integrerer med Microsoft Graph for å:

- Verifisere brukere
- Opprette kalenderhendelser
- Sende møteinvitasjoner

Krever:

- Azure App Registration
- Riktige API-rettigheter (f.eks. `User.Read.All`, `Calendars.ReadWrite`)
- Admin consent gitt i Azure

Miljøvariabler for Azure må konfigureres i produksjon.

---

## 📡 API-oversikt

### Åpne endepunkter

```
GET    /events
GET    /events/{id}
GET    /categories
```

### Beskyttede endepunkter

```
POST   /events/create
POST   /events/{id}/join
DELETE /events/{id}/join
```

Alle beskyttede endepunkter krever gyldig JWT access token.

---

## 🧪 Testing

Kjør tester med:

```bash
mvn test
```

---

## 🚀 Deploy

Deploy håndteres via:

- Docker image-build
- CI/CD pipeline (GitHub Actions)
- Google Cloud

Sørg for at alle nødvendige miljøvariabler og secrets er konfigurert i skyløsningen.

---

## 👨‍💻 Utviklernotater

- Bruker lagdelt arkitektur (Controller → Service → Repository)
- DTO-er brukes for å skille API-lag fra entiteter
- Sikkerhet er profilbasert
- Backend er tett integrert med Strim-frontend

---

**Vedlikeholdes av NAV IT**
