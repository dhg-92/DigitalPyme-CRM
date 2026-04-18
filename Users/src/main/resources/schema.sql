INSERT INTO "user"
(name, surname, email, password, phone, is_admin, mfa_enabled, mfa_secret)
VALUES
    ('Admin', 'Admin', 'admin@digipymcrm.es', '123456', '666666666', true, false, NULL),
    ('Comercial', 'Comercial', 'comercial@digipymcrm.es', '123456', '666666666', false, false, NULL);
