# Abysalto Backend Task – Product Middleware API

Middleware REST API koji dohvaća proizvode iz vanjskog izvora (za sada [DummyJSON](https://dummyjson.com/products))
i izlaže ih kroz vlastiti API s filtriranjem, pretragom, keširanjem i JWT prijavom.

Cilj mi je bio napraviti arhitekturu tako da se kasnije mogu dodati i drugi izvori podataka
(baza, file system, RSS) bez diranja kontrolera i servisa – dovoljno je napisati novu implementaciju
sučelja `ProductSource`.

## Tehnologije

- Java 21
- Spring Boot 3.5.3
- Maven (wrapper `./mvnw`)
- Spring `RestClient` (dohvat s DummyJSON-a)
- Spring Security + JWT (jjwt)
- Caffeine (cache)
- springdoc-openapi (Swagger)
- JUnit 5, Mockito, MockMvc (testovi)

## Arhitektura

```
HTTP  →  Controller  →  Service  →  ProductSource (sučelje)  →  DummyJsonProductSource  →  DummyJSON
```

- **Controller** – prima HTTP zahtjeve i vraća JSON.
- **Service** – poslovna logika: filtriranje, pretraga, skraćivanje opisa, cache.
- **ProductSource** – sučelje koje predstavlja izvor podataka. Servis ovisi o sučelju, ne o konkretnom izvoru.
- **DummyJsonProductSource** – implementacija koja podatke dohvaća s DummyJSON API-ja.

Zbog tog sloja, dodavanje novog izvora znači samo novu klasu koja implementira `ProductSource`.

## Pokretanje

Potrebna je Java 21. Maven nije potreban jer je u projektu wrapper.

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

Aplikacija se pokreće na http://localhost:8080.

### Konfiguracija

Postavke su u `src/main/resources/application.properties`:

| Postavka | Zadano | Opis |
|---|---|---|
| `dummyjson.base-url` | `https://dummyjson.com` | bazni URL izvora podataka |
| `spring.cache.caffeine.spec` | `maximumSize=500,expireAfterWrite=60s` | veličina i trajanje cachea |
| `jwt.secret` | (postavljen) | ključ za potpisivanje JWT tokena |
| `jwt.expiration-ms` | `3600000` | trajanje tokena (1h) |

JWT ključ sam za potrebe zadatka ostavio u repozitoriju da se rješenje može odmah pokrenuti.
U pravom projektu bi išao kroz environment varijablu i ne bi se commitao.

## Prijava

Endpointi za proizvode su zaštićeni, pa je potreban token. Token se dobije prijavom, a
korisnik se provjerava kod DummyJSON-a.

Testni korisnik:

| username | password |
|---|---|
| `emilys` | `emilyspass` |

Prijava:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"emilys","password":"emilyspass"}'
```

Odgovor:

```json
{ "token": "eyJhbGciOiJIUzUxMiJ9..." }
```

Token se onda šalje u headeru svakog zahtjeva:

```
Authorization: Bearer <token>
```

## Endpointi

| Metoda | Put | Opis |
|---|---|---|
| `GET` | `/api/products` | lista proizvoda (naziv, slika, cijena, skraćen opis do 100 znakova) |
| `GET` | `/api/products/{id}` | detalji jednog proizvoda |
| `GET` | `/api/products?category=beauty` | filtriranje po kategoriji |
| `GET` | `/api/products?minPrice=10&maxPrice=50` | filtriranje po cijeni |
| `GET` | `/api/products?search=mascara` | pretraga po nazivu |
| `POST` | `/api/auth/login` | prijava (javno) |

Filteri i pretraga mogu se kombinirati:

```
/api/products?category=beauty&maxPrice=15&search=lip
```

Primjer s tokenom:

```bash
TOKEN="<token>"
curl http://localhost:8080/api/products?search=mascara \
  -H "Authorization: Bearer $TOKEN"
```

## Swagger

Nakon pokretanja:

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

Swagger, health i login su javni, ostalo traži token.

## Testovi

```bash
./mvnw test
```

- Unit testovi (`ProductServiceTest`) – filtriranje, pretraga, skraćivanje opisa (izvor je mockan).
- Integracijski testovi (`ProductControllerTest`) – HTTP sloj i sigurnost preko MockMvc-a.

## Napomena o korištenju AI alata

Pri izradi sam koristio AI (Claude) kao pomoć pri odlukama, ne za generiranje gotovog rješenja.
Kod sam pisao i testirao sam, uz razumijevanje svake odluke u rješenju. AI sam koristio za:

- konzultaciju oko arhitekture – kako postaviti middleware da bude lako proširiv na nove izvore
  podataka (baza, RSS...) bez diranja kontrolera i servisa
- prijedlog scenarija koje bi bilo dobro pokriti testovima
- pomoć pri debugiranju konkretnih grešaka tijekom razvoja

Par primjera pitanja koja sam postavljao:

- "Kako bih trebao strukturirati izvore podataka da mogu kasnije lako dodati bazu ili RSS kao
  novi izvor proizvoda, a da ne moram dirati controller i service?"
- "Trebam li izdavati vlastiti JWT ili proslijediti token koji vrati DummyJSON?"
- "Koje scenarije bih trebao pokriti testovima za filtriranje i pretragu proizvoda?"
- "Kako riješiti da ponovljeni pozivi s istim parametrima pretrage/filtera ne idu svaki put na
  vanjski izvor?"
