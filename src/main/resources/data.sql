INSERT INTO authorities (username, authority) VALUES ('admin', 'ROLE_ADMIN');
INSERT INTO authorities (username, authority) VALUES ('gerard', 'ROLE_USER');

INSERT INTO users (username, password, enabled, apikey, email) VALUES ('admin', '$2y$10$Te.lNeGaDiRAUAXXpBbR7.3s9vi/K8tKGNe2EvfuEjMOquXAbR/Uy', true, '7847493', 'admin@demodrop.nl');
INSERT INTO users (username, password, enabled, apikey, email) VALUES ('gerard', '$2y$10$b7KTevAvbO8kxrV9b6rEH.6iz49k4TFpE1lB4H97BZmlsdByswmPK', true, '7847493', 'gerardheuvelman@gmail.com');
INSERT INTO users (username, password, enabled, apikey, email) VALUES ('hendrik', '$2y$10$b7KTevAvbO8kxrV9b6rEH.6iz49k4TFpE1lB4H97BZmlsdByswmPK', true, '7847493', 'hendrikheuvelman@icloud.com');

INSERT INTO demo (demo_id, created_date, title, length, bpm) VALUES (1001, '2023-01-01', 'Prime Audio', 260, 123.0);
INSERT INTO demo (demo_id, created_date, title, length, bpm) VALUES (1002, '2023-01-02', 'Dance Monkey', 180, 120.0);
INSERT INTO demo (demo_id, created_date, title, length, bpm) VALUES (1003, '2023-01-03', 'Dubstep Wonderland', 160, 130.0);

INSERT INTO conversation (conversation_id, subject, created_date, latest_reply_date, body) VALUES (2001, 'subject1', '2023-01-11', '2023-01-14','body1');
INSERT INTO conversation (conversation_id, subject, created_date, latest_reply_date, body) VALUES (2002, 'subject2', '2023-01-12', '2023-01-15','body2');
INSERT INTO conversation (conversation_id, subject, created_date, latest_reply_date, body) VALUES (2003, 'subject3', '2023-01-13', '2023-01-16','body3');

INSERT INTO file (file_name, content_type, url) VALUES ('PrimeAudio.mp3', 'audio', '/uploads');
INSERT INTO file (file_name, content_type, url) VALUES ('dance-monkey.mp3', 'audio', '/uploads');
INSERT INTO file (file_name, content_type, url) VALUES ('dubstep-wonderland.mp3', 'audio', '/uploads');

INSERT INTO genre (name) VALUES ('Alternative dance');
INSERT INTO genre (name) VALUES ('Dance');
INSERT INTO genre (name) VALUES ('Dub-Step');

UPDATE demo
SET username = 'gerard'
WHERE demo_id = 1001;

UPDATE demo
SET username = 'admin'
WHERE demo_id = 1002;

UPDATE demo
SET username = 'gerard'
WHERE demo_id = 1003;

UPDATE demo
SET file = 'PrimeAudio.mp3'
WHERE demo_id = 1001;

UPDATE demo
SET file = 'dance-monkey.mp3'
WHERE demo_id = 1002;

UPDATE demo
SET file = 'dubstep-wonderland.mp3'
WHERE demo_id = 1003;

UPDATE demo
SET genre = 'Alternative dance'
WHERE demo_id = 1001;

UPDATE demo
SET genre = 'Dance'
WHERE demo_id = 1002;

UPDATE demo
SET genre = 'Dub-Step'
WHERE demo_id = 1003;

UPDATE conversation
SET demo_id = 1001, username = 'admin'
WHERE conversation_id = 2001;

UPDATE conversation
SET demo_id = 1002, username = 'gerard'
WHERE conversation_id = 2002;

UPDATE conversation
SET demo_id = 1003, username = 'admin'
WHERE conversation_id = 2003;