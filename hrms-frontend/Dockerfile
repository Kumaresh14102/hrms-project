# # Stage 1: Build (Uses Node.js)
# FROM node:20-alpine AS build
# WORKDIR /app

# # Install dependencies
# COPY package*.json ./
# RUN npm install --quiet

# # Copy source and build
# COPY . .
# RUN npm run build --configuration=production

# # Stage 2: Serve with Nginx
# FROM nginx:alpine

# # Based on your angular.json, the files are in dist/hrms-frontend/browser
# COPY --from=build /app/dist/hrms-frontend/browser /usr/share/nginx/html

# # Copy your nginx config to handle Angular routing
# COPY nginx.conf /etc/nginx/conf.d/default.conf

# EXPOSE 80
# CMD ["nginx", "-g", "daemon off;"]





# Stage 1: Build (Starts from your base image)
FROM hrms-frontend-base:latest AS build
WORKDIR /app

# Your node_modules are already inside hrms-frontend-base!
# Just copy your code and build.
COPY . .
RUN npm run build --configuration=production

# Stage 2: Serve with Nginx
FROM nginx:alpine
COPY --from=build /app/dist/hrms-frontend/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]