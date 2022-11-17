-- DELETE
-- FROM items
-- where id > 6;
--
-- DELETE
-- FROM users
-- where id > 3;

INSERT INTO users(NAME, EMAIL)
VALUES ('User1', 'User1@email.com'),
       ('User2', 'User2@email.com'),
       ('User3', 'User3@email.com'),
       ('User4', 'User4@email.com');

INSERT INTO items(NAME, DESCRIPTION, IS_AVAILABLE, OWNER_ID)
VALUES ('Item1', 'Description1', true, 1);
INSERT INTO items(NAME, DESCRIPTION, IS_AVAILABLE, OWNER_ID)
VALUES ('Item11', 'Description11', false, 1);
INSERT INTO items(NAME, DESCRIPTION, IS_AVAILABLE, OWNER_ID)
VALUES ('Item2', 'Description2', true, 2);
INSERT INTO items(NAME, DESCRIPTION, IS_AVAILABLE, OWNER_ID)
VALUES ('Item22', 'Description22', true, 2);
INSERT INTO items(NAME, DESCRIPTION, IS_AVAILABLE, OWNER_ID)
VALUES ('Item3', 'Description3', true, 3);
INSERT INTO items(NAME, DESCRIPTION, IS_AVAILABLE, OWNER_ID)
VALUES ('Item33', 'Description33', true, 3);
INSERT INTO items(NAME, DESCRIPTION, IS_AVAILABLE, OWNER_ID)
VALUES ('Item4', 'Description4', true, 4);
INSERT INTO items(NAME, DESCRIPTION, IS_AVAILABLE, OWNER_ID)
VALUES ('Item44', 'Description44', true, 4);
--
-- INSERT INTO bookings(start_date, end_date, item_id, booker_id, status)
-- VALUES ('2021-09-09 09:00:00', '2021-10-09 09:00:00', 3, 3, 'approved');
-- INSERT INTO bookings(start_date, end_date, item_id, booker_id, status)
-- VALUES ('2023-01-09 09:00:00', '2023-02-09 09:00:00', 3, 3, 'waiting');
