    FROM node:20-alpine
    WORKDIR /app

    # Copy dependency files only
    COPY package*.json ./

    # Install dependencies (This is the heavy part)
    RUN npm install --quiet

    # This image now acts as a "warm" environment with node_modules ready