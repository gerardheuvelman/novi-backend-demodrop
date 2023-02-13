INSERT INTO authorities (username, authority) VALUES ('admin', 'ROLE_ADMIN');
INSERT INTO authorities (username, authority) VALUES ('admin', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('gerard', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('Hendrik', 'ROLE_USER');

INSERT INTO users (username, password, enabled, apikey, email, created_date) VALUES ('admin', '$2y$10$Te.lNeGaDiRAUAXXpBbR7.3s9vi/K8tKGNe2EvfuEjMOquXAbR/Uy', true, 'fakeKey1', 'admin@demodrop.nl', '2023-01-01');
INSERT INTO users (username, password, enabled, apikey, email, created_date) VALUES ('gerard', '$2y$10$b7KTevAvbO8kxrV9b6rEH.6iz49k4TFpE1lB4H97BZmlsdByswmPK', true, 'fakeKey2', 'gerardheuvelman@gmail.com','2023-01-02');
INSERT INTO users (username, password, enabled, apikey, email, created_date) VALUES ('hendrik', '$2y$10$b7KTevAvbO8kxrV9b6rEH.6iz49k4TFpE1lB4H97BZmlsdByswmPK', true, 'fakeKey3', 'hendrikheuvelman@icloud.com','2023-01-03');

INSERT INTO demo (demo_id, created_date, title, length, bpm) VALUES (1001, '2023-01-01', 'Prime Audio', 260, 123.0);
INSERT INTO demo (demo_id, created_date, title, length, bpm) VALUES (1002, '2023-01-02', 'Dance Monkey', 180, 120.0);
INSERT INTO demo (demo_id, created_date, title, length, bpm) VALUES (1003, '2023-01-03', 'Dubstep Wonderland', 160, 130.0);

INSERT INTO conversation (conversation_id, subject, created_date, latest_reply_date, body, read_by_producer, read_by_interested_user) VALUES (2001, 'subject1', '2023-01-11', '2023-01-14','body1' , false, false);
INSERT INTO conversation (conversation_id, subject, created_date, latest_reply_date, body, read_by_producer, read_by_interested_user) VALUES (2002, 'subject2', '2023-01-12', '2023-01-15','body2' , false , false);
INSERT INTO conversation (conversation_id, subject, created_date, latest_reply_date, body, read_by_producer, read_by_interested_user) VALUES (2003, 'subject3', '2023-01-13', '2023-01-16','body3' , false, false);

INSERT INTO audio_file (file_id, original_file_name) VALUES (3001, 'PrimeAudio.mp3');
INSERT INTO audio_file (file_id, original_file_name) VALUES (3002, 'DanceMonkey.mp3');
INSERT INTO audio_file (file_id, original_file_name) VALUES (3003, 'DubstepWonderland.mp3');

INSERT INTO genre (name) VALUES ('Alternative dance');
INSERT INTO genre (name) VALUES ('Dance');
INSERT INTO genre (name) VALUES ('Dub-Step');

INSERT INTO demos_users_favorites_ (demo_id, user_id) VALUES (1002, 'gerard');

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
SET audio_file = 3001
WHERE demo_id = 1001;

UPDATE demo
SET audio_file = 3002
WHERE demo_id = 1002;

UPDATE demo
SET audio_file = 3003
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
SET demo_id = 1001, producer_name = 'gerard', interested_user_name = 'admin'
WHERE conversation_id = 2001;

UPDATE conversation
SET demo_id = 1002, producer_name = 'admin', interested_user_name = 'gerard'
WHERE conversation_id = 2002;

UPDATE conversation
SET demo_id = 1003, producer_name = 'gerard', interested_user_name = 'admin'
WHERE conversation_id = 2003;