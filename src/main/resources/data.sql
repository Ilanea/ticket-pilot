-- Insert a admin user
INSERT INTO users (id, version, first_name, last_name, user_status, user_role, email, password)
VALUES (0, 0, 'Admin', 'Test', 0, 0, 'admin@test.com', '$2a$10$NO54IlIvFio4Gu5GRgEyMuKr1hHtvm8xyf/L/LtAUPKDwQ2KIWuae');

-- Insert a Manager user
INSERT INTO users (id, version, first_name, last_name, user_status, user_role, email, password)
VALUES (1, 0, 'Manager', 'Test', 0, 1, 'manager@test.com', '$2a$10$NO54IlIvFio4Gu5GRgEyMuKr1hHtvm8xyf/L/LtAUPKDwQ2KIWuae');

-- Insert a Standard user
INSERT INTO users (id, version, first_name, last_name, user_status, user_role, email, password)
VALUES (2, 0, 'User', 'Test', 0, 2, 'user@test.com', '$2a$10$NO54IlIvFio4Gu5GRgEyMuKr1hHtvm8xyf/L/LtAUPKDwQ2KIWuae');

-- Insert our Standard users
INSERT INTO users (id, version, first_name, last_name, user_status, user_role, email, password)
VALUES (3, 0, 'Mario', 'Test', 0, 2, 'bm8260@mci4me.at', '$2a$10$NO54IlIvFio4Gu5GRgEyMuKr1hHtvm8xyf/L/LtAUPKDwQ2KIWuae');

INSERT INTO users (id, version, first_name, last_name, user_status, user_role, email, password)
VALUES (4, 0, 'Heinz', 'Test', 0, 2, 'mh2126@mci4me.at', '$2a$10$NO54IlIvFio4Gu5GRgEyMuKr1hHtvm8xyf/L/LtAUPKDwQ2KIWuae');

INSERT INTO users (id, version, first_name, last_name, user_status, user_role, email, password)
VALUES (5, 0, 'Judith', 'Test', 0, 2, 'lj0950@mci4me.at', '$2a$10$NO54IlIvFio4Gu5GRgEyMuKr1hHtvm8xyf/L/LtAUPKDwQ2KIWuae');

INSERT INTO users (id, version, first_name, last_name, user_status, user_role, email, password)
VALUES (6, 0, 'Matthias', 'Test', 0, 2, 'mm3220@mci4me.at', '$2a$10$NO54IlIvFio4Gu5GRgEyMuKr1hHtvm8xyf/L/LtAUPKDwQ2KIWuae');

INSERT INTO users (id, version, first_name, last_name, user_status, user_role, email, password)
VALUES (7, 0, 'Florian', 'Test', 0, 2, 'lf9600@mci4me.at', '$2a$10$NO54IlIvFio4Gu5GRgEyMuKr1hHtvm8xyf/L/LtAUPKDwQ2KIWuae');

INSERT INTO users (id, version, first_name, last_name, user_status, user_role, email, password)
VALUES (8, 0, 'Christine', 'Test', 0, 2, 'che.lackinger@mci4me.at', '$2a$10$NO54IlIvFio4Gu5GRgEyMuKr1hHtvm8xyf/L/LtAUPKDwQ2KIWuae');
