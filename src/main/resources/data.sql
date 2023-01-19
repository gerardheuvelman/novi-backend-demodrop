
INSERT INTO conversation (id, subject, created_date, latest_reply_date, body) VALUES (2001, 'subject1', '2023-01-11', '2023-01-14','body1');
INSERT INTO conversation (id, subject, created_date, latest_reply_date, body) VALUES (2002, 'subject2', '2023-01-12', '2023-01-15','body2');
INSERT INTO conversation (id, subject, created_date, latest_reply_date, body) VALUES (2003, 'subject3', '2023-01-13', '2023-01-16','body3');

INSERT INTO demo (id, title, created_date, length, audiofile_url) VALUES (1001, 'title1', '2023-01-01', 101.0, 'url1');
INSERT INTO demo (id, title, created_date, length, audiofile_url) VALUES (1002, 'title2', '2023-01-02', 102.0, 'url2');
INSERT INTO demo (id, title, created_date, length, audiofile_url) VALUES (1003, 'title3', '2023-01-03', 103.0, 'url3');

INSERT INTO users (username, password, enabled, apikey, email) VALUES ('henk', '$2y$10$Te.lNeGaDiRAUAXXpBbR7.3s9vi/K8tKGNe2EvfuEjMOquXAbR/Uy', true, '7847493', 'test@testy.tst');
INSERT INTO users (username, password, enabled, apikey, email) VALUES ('gerard', '$2y$10$b7KTevAvbO8kxrV9b6rEH.6iz49k4TFpE1lB4H97BZmlsdByswmPK', true, '7847493', 'gerardheuvelman@gmail.com');

INSERT INTO authorities (username, authority) VALUES ('henk', 'ROLE_ADMIN');
INSERT INTO authorities (username, authority) VALUES ('gerard', 'ROLE_USER');

UPDATE demo
SET username = 'gerard'
WHERE demo.id = 1001;

UPDATE demo
SET username = 'henk'
WHERE demo.id = 1002;

UPDATE demo
SET username = 'gerard'
WHERE demo.id = 1003;

UPDATE conversation
SET demo_id = 1001, username = 'henk'
WHERE conversation.id = 2001;

UPDATE conversation
SET demo_id = 1002, username = 'gerard'
WHERE conversation.id = 2002;

UPDATE conversation
SET demo_id = 1003, username = 'henk'
WHERE conversation.id = 2003;