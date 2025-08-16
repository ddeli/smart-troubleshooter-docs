# Smart Troubleshooter Docs

An intelligent tool for automatically generating structured troubleshooting documentation from free-text input — powered by Angular, Spring Boot, and Ollama with the Mistral LLM.

## Project Overview

**Smart Troubleshooter Docs** is a modern AI-assisted system that transforms unstructured problem descriptions into structured troubleshooting documentation. It is designed to assist support teams by turning free-form input (e.g., ticket texts, error logs) into useful, standardized output.

**Tech Stack:**

- **Frontend**: Angular
- **Backend**: Spring Boot (Java)
- **LLM Integration**: [Ollama](https://ollama.com) with the Mistral model
- **Containerization**: Docker & Docker Compose

## Project Structure

```
smart-troubleshooter-docs/
├── backend/       # Spring Boot Backend
├── frontend/      # Angular Frontend
```

## Getting Started

### 1. Prerequisites

- **Node.js** (recommended: ≥ 18.x)
- **npm**
- **Java JDK** (recommended: 17+)
- **Maven**
- **[Ollama](https://ollama.com/download)** (installed and running)
- Mistral LLM (must be pulled once):

```bash
ollama pull mistral
```

### 2. Starting the Project

#### Option 1: Using Docker (Recommended)

```bash
# Start all services with Docker Compose
docker-compose up -d

# Pull Mistral model (if not already done)
docker exec ollama ollama pull mistral
```

The app should now be running at:  
`http://localhost:4200` (Frontend)  
`http://localhost:8080` (Backend API)

#### Option 2: Manual Setup

##### Backend (Spring Boot)

```bash
cd backend
./mvnw spring-boot:run
```

##### Frontend (Angular)

```bash
cd frontend
npm install
npm start
```

The app should now be running at:  
`http://localhost:4200`

## How It Works

1. User submits free-text description of a problem.
2. Backend sends the input to Mistral via local Ollama instance.
3. Structured output is returned and rendered in the frontend.

## Development

### Backend

- Java 17+
- Spring Boot REST API
- Integration with local Ollama

### Frontend

- Angular 16+
- HTTP communication with backend
- UI for input and output visualization

## Testing

- **Backend**: Run `mvn test`
- **Frontend**: Run `ng test`

## Roadmap

- [ ] Multilingual support (EN/DE)
- [ ] Save/export troubleshooting documents

## License

MIT License — free to use with attribution.

## Contributing

Pull requests, suggestions, and bug reports are welcome!
