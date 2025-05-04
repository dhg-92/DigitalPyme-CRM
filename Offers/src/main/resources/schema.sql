INSERT INTO "category" (name, description, parent_id)
VALUES ('Electrónica','Dispositivos electrónicos como laptops, smartphones y tablets',NULL),
       ('Equipamiento','Piezas',NULL),
       ('Almacenamiento','Discos duros, usb...',2),
       ('Procesadores','Procesadores',2);

INSERT INTO "client" (name, surname, email, company, phone, address, city, zip_code, country)
VALUES
    ('Client 1', 'Client 1', 'diegohg8@email.com', 'Company 1', '+34111111111', 'Dirección 1', 'Madrid', 28080, 'España'),
    ('Client 2', 'Client 2', 'Client.2@email.com', 'Company 2', '+34222222222', 'Dirección 2', 'Madrid', 28080, 'España'),
    ('Client 3', 'Client 3', 'Client.3@email.com', 'Company 3', '+34333333333', 'Dirección 3', 'Madrid', 28080, 'España'),
    ('Client 4', 'Client 4', 'Client.4@email.com', 'Company 4', '+34444444444', 'Dirección 4', 'Madrid', 28080, 'España'),
    ('Client 5', 'Client 5', 'Client.5@email.com', 'Company 5', '+34555555555', 'Dirección 5', 'Madrid', 28080, 'España'),
    ('Client 6', 'Client 6', 'Client.6@email.com', 'Company 6', '+34666666666', 'Dirección 6', 'Madrid', 28080, 'España'),
    ('Client 7', 'Client 7', 'Client.7@email.com', 'Company 7', '+34777777777', 'Dirección 7', 'Madrid', 28080, 'España'),
    ('Client 8', 'Client 8', 'Client.8@email.com', 'Company 8', '+34888888888', 'Dirección 8', 'Madrid', 28080, 'España'),
    ('Client 9', 'Client 9', 'Client.9@email.com', 'Company 9', '+34999999999', 'Dirección 9', 'Madrid', 28080, 'España'),
    ('Client 10', 'Client 10', 'Client.10@email.com', 'Company 10', '+34999999991', 'Dirección 10', 'Madrid', 28080, 'España');

INSERT INTO "product" (name, description, default_price, default_tax, brand, model, category_id)
VALUES
    ('Producto1', 'ProductoDescripcion1', 100.00, 7.00, 'HP', 'G3', 1),
    ('Producto2', 'ProductoDescripcion2', 110.00, 7.50, 'HP', 'G3', 1),
    ('Producto3', 'ProductoDescripcion3', 120.00, 8.00, 'HP', 'G3', 1),
    ('Producto4', 'ProductoDescripcion4', 130.00, 8.50, 'HP', 'G3', 1),
    ('Producto5', 'ProductoDescripcion5', 140.00, 9.00, 'HP', 'G3', 1),
    ('Producto6', 'ProductoDescripcion6', 150.00, 9.50, 'HP', 'G3', 1),
    ('Producto7', 'ProductoDescripcion7', 160.00, 10.00, 'HP', 'G3', 1),
    ('Producto8', 'ProductoDescripcion8', 170.00, 10.50, 'HP', 'G3', 1),
    ('Producto9', 'ProductoDescripcion9', 180.00, 11.00, 'HP', 'G3', 1),
    ('Producto10', 'ProductoDescripcion10', 190.00, 11.50, 'HP', 'G3', 1);

INSERT INTO "offer" (name, date, client_id, status, total_price)
VALUES ('Test','2025-01-01 01:00:00',1,'Borrador',10.00),
       ('Test','2025-04-01 01:00:00',2,'Pendiente de enviar',10.00),
       ('Test','2025-04-18 01:00:00',3,'Pendiente de enviar',10.00),
       ('Test','2025-04-25 01:00:00',4,'Pendiente de enviar',10.00),
       ('Test','2025-04-28 01:00:00',5,'Enviada',10.00),
       ('Test','2025-01-01 01:00:00',3,'Enviada',10.00),
       ('Test','2025-01-01 01:00:00',3,'Enviada',10.00),
       ('Test','2025-01-01 01:00:00',3,'Enviada',10.00),
       ('Test','2025-01-01 01:00:00',4,'Aceptada',10.00),
       ('Test','2025-01-01 01:00:00',4,'Aceptada',10.00),
       ('Test','2025-01-01 01:00:00',5,'Aceptada',10.00),
       ('Test','2025-01-01 01:00:00',6,'Aceptada',10.00),
       ('Test','2025-01-01 01:00:00',2,'Aceptada',10.00),
       ('Test','2025-01-01 01:00:00',3,'Aceptada',10.00),
       ('Test','2025-01-01 01:00:00',2,'Aceptada',10.00),
       ('Test','2025-01-01 01:00:00',2,'Rechazada',10.00),
       ('Test','2025-04-01 01:00:00',7,'Rechazada',10.00),
       ('Test','2025-04-01 01:00:00',7,'Rechazada',10.00),
       ('Test','2025-04-01 01:00:00',7,'Rechazada',10.00),
       ('Test','2025-04-01 01:00:00',8,'Rechazada',10.00),
       ('Test','2025-04-18 01:00:00',9,'Rechazada',10.00),
       ('Test','2025-04-17 01:00:00',9,'Rechazada',10.00),
       ('Test','2025-04-23 01:00:00',8,'Rechazada',10.00),
       ('Test','2025-04-25 01:00:00',8,'Aceptada',10.00),
       ('Test','2025-04-26 01:00:00',1,'Aceptada',10.00);

INSERT INTO "offer_product" (offer_id, product_id, product_price, tax, quantity, margin, baseproduct_price)
VALUES
    (5, 1, 112.00, 7.00, 2, 12.00, 100.00),
    (12, 3, 144.00, 8.00, 1, 20.00, 120.00),
    (9, 7, 180.80, 10.00, 3, 13.00, 160.00),
    (20, 2, 123.20, 7.50, 2, 12.00, 110.00),
    (14, 5, 163.80, 9.00, 1, 17.00, 140.00),
    (6, 4, 152.10, 8.50, 4, 17.00, 130.00),
    (3, 9, 211.50, 11.00, 1, 17.50, 180.00),
    (8, 10, 213.90, 11.50, 2, 12.60, 190.00),
    (2, 6, 177.00, 9.50, 1, 18.00, 150.00),
    (18, 8, 197.20, 10.50, 2, 16.00, 170.00),
    (21, 1, 125.00, 7.00, 1, 25.00, 100.00),
    (11, 5, 154.00, 9.00, 3, 10.00, 140.00),
    (24, 3, 132.00, 8.00, 1, 10.00, 120.00),
    (7, 2, 115.50, 7.50, 2, 5.00, 110.00),
    (1, 6, 169.50, 9.50, 2, 13.00, 150.00),
    (13, 10, 217.60, 11.50, 2, 14.50, 190.00),
    (19, 7, 190.40, 10.00, 1, 19.00, 160.00),
    (23, 9, 202.50, 11.00, 3, 12.50, 180.00),
    (4, 4, 148.20, 8.50, 2, 14.00, 130.00),
    (16, 8, 189.10, 10.50, 2, 11.25, 170.00);