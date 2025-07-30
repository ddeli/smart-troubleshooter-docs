# frontend/Dockerfile

# 1. Build Stage
FROM node:18-alpine AS builder
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build -- --configuration production

# 2. Nginx Stage
FROM nginx:alpine
COPY --from=builder /app/dist/smart-troubleshooter-docs-frontend/browser /usr/share/nginx/html
EXPOSE 80
