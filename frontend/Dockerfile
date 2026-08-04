# Stage 1: Build stage using Node 20 Alpine
FROM node:20-alpine AS builder

WORKDIR /app

# Copy package files first to cache node_modules layer
COPY package.json package-lock.json ./

# Install npm dependencies
RUN npm ci

# Copy full application source code
COPY . .

# Build Vite SPA output into dist directory
RUN npm run build

# Stage 2: Production Nginx stage
FROM nginx:alpine

# Remove default Nginx HTML website
RUN rm -rf /usr/share/nginx/html/*

# Copy built production assets from builder stage
COPY --from=builder /app/dist /usr/share/nginx/html

# Copy custom Nginx configuration with reverse proxy rules
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 80
EXPOSE 80

# Start Nginx in foreground mode
CMD ["nginx", "-g", "daemon off;"]
