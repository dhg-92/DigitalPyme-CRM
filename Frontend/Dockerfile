FROM node:22.11.0-alpine AS builder

WORKDIR /app

COPY . .

RUN npm install
RUN npm run build

FROM node:22.11.0-alpine AS runner

WORKDIR /app

RUN npm install -g serve

COPY --from=builder /app/dist ./dist

EXPOSE 5173

CMD ["serve", "-s", "dist", "-l", "5173"]
