FROM node:22.11.0-alpine

WORKDIR /app

COPY . .

RUN npm install

EXPOSE 5173
CMD [ "npm", "run", "dev", "--", "--host" ]