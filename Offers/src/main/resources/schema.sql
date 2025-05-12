INSERT INTO "category" (name, description, parent_id)
VALUES ('Hardware','Elementos Hardware',NULL);

INSERT INTO "client" (name, surname, email, company, phone, address, city, zip_code, country)
VALUES
    ('Diego', 'Higuera', 'dh92@uoc.edu', 'UOC Student', '+34111111111', 'Avenida del Tibidabo, 39-43', 'Barcelona', 08018, 'España');

INSERT INTO "product" (name, description, default_price, default_tax, brand, model, category_id)
VALUES
    ('Portatil', 'Intel Core i5-13420H/16GB/1TB SSD/15.6', 400.00, 21.00, 'Lenovo', 'IdeaPad Slim 3 Gen 8 15IRH8', 1);

INSERT INTO "offer" (name, date, client_id, status, total_price)
VALUES ('Venta de portatil','2025-05-10 14:30:00',1,'Borrador',580.80);

INSERT INTO "offer_product" (offer_id, product_id, product_price, tax, quantity, margin, baseproduct_price)
VALUES
    (1, 1, 480.00, 21.00, 1, 20.00, 400.00);
