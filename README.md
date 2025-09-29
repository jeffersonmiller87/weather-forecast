# ☁️ Weather Forecast API Service

This Application provides a RESTful API service for retrieving current weather forecasts based on a geographical 
ZIP/Postal Code.
It efficiently utilizes a layered architecture (Controller, Service, Feign Clients) and implements a 15-minute 
Time-To-Live (TTL) in memory caching mechanism using Caffeine for rapid, repeated data access.

## ✨ Features
API Endpoint: Accepts a single ZIP or Postal Code

Core Data: Provides current temperature.

Extended Data: Includes daily high/low temperatures and an extended 7-day forecast.

Geocoding: Uses the Nominatim API to convert the ZIP code to Latitude/Longitude.

Weather Data: Uses the Open-Meteo API to fetch weather data.

Caching (Caffeine): Implements a 15-minute Time-To-Live (TTL) in memory cache for forecast results based on the ZIP code.

Cache Indicator: The response payload includes a fromCache boolean flag to notify the user if the result was retrieved 
from the cache or a live API call.

## 🛠️ Technology Stack

| Component        | Technology                                  | Version |
|------------------|---------------------------------------------|---------|
| Language         | Java                                        | 21      |
| Build Tool       | Gradle                                      | Latest  |
| Framework        | Spring Boot                                 | 3.x     |
| Caching Provider | Caffeine                                    | Latest  |
| HTTP Client      | Spring Cloud OpenFeign                      | Latest  |
| External APIs    | Nominatim (Geocoding), Open-Meteo (Weather) | Latest  |

## 🚀 Getting Started

### Prerequisites

Java 21 or newer (LTS recommended)

Maven 3.6+

### Build and Run

Build the project using Maven:

`./gradlew clean build`

Run the application:

`./gradlew bootRun`

The service will start on http://localhost:8080 (unless configured otherwise).

### 🎯 API Endpoint
The primary endpoint is a simple GET request using the Zip Code as a path variable.

#### Request
| Method | URL                     | Example                                     |
|--------|-------------------------|---------------------------------------------|
| GET    | /api/forecast/{zipCode} | http://localhost:8080/api/forecast/88220000 |

#### Example call
`curl --request GET --url http://localhost:8080/api/forecast/88220000`

#### Response Example
A successful response returns a JSON object containing the forecast and the cache status.

`{"current":{"temperature_2m":8.5},"daily":{"time":
["2025-09-29","2025-09-30","2025-10-01","2025-10-02","2025-10-03","2025-10-04","2025-10-05"],
"temperature_2m_min": [7.6,10.0,8.5,10.6,14.6,11.1,9.3],
"temperature_2m_max": [17.2,17.7,16.7,17.6,19.0,17.5,16.5]},
"fromCache": true}`

#### Postal Code Validation
The controller includes input validation using regular expression to accept various international formats, including:

- US ZIP: DDDDD or DDDDD-DDDD
- Brazil CEP: DDDDD-DDD or DDDDDDDD
- UK Postcode: Complex formats like SW1A 0AA
