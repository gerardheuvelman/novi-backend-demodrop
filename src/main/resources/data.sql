INSERT INTO authorities (username, authority) VALUES ('admin', 'ROLE_ADMIN');
INSERT INTO authorities (username, authority) VALUES ('admin', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('gerard', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('aalke', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('admin2', 'ROLE_ADMIN');
INSERT INTO authorities (username, authority) VALUES ('someotheruser3', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('someotheruser4', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('someotheruser5', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('someotheruser6', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('someotheruser7', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('someotheruser8', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('someotheruser9', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('someotheruser10', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('someotheruser11', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('someotheruser12', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('someotheruser13', 'ROLE_USER');

INSERT INTO users (username, password, enabled, email, created_date) VALUES ('admin', '$2y$10$Te.lNeGaDiRAUAXXpBbR7.3s9vi/K8tKGNe2EvfuEjMOquXAbR/Uy', true, 'admin@demodrop.nl', '2023-01-01');
INSERT INTO users (username, password, enabled, email, created_date) VALUES ('gerard', '$2y$10$b7KTevAvbO8kxrV9b6rEH.6iz49k4TFpE1lB4H97BZmlsdByswmPK', true, 'gerardheuvelman@gmail.com','2023-01-02');
INSERT INTO users (username, password, enabled, email, created_date) VALUES ('hendrik', '$2y$10$b7KTevAvbO8kxrV9b6rEH.6iz49k4TFpE1lB4H97BZmlsdByswmPK', true, 'hendrikheuvelman@icloud.com','2023-01-03');
INSERT INTO users (username, password, enabled, email, created_date) VALUES ('aalke', '$2y$10$b7KTevAvbO8kxrV9b6rEH.6iz49k4TFpE1lB4H97BZmlsdByswmPK', true, 'hendrikheuvelman@icloud.com1','2023-01-03');
INSERT INTO users (username, password, enabled, email, created_date) VALUES ('disableduser', '$2y$10$b7KTevAvbO8kxrV9b6rEH.6iz49k4TFpE1lB4H97BZmlsdByswmPK', false, 'hendrikheuvelman@icloud.com1','2023-01-03');
INSERT INTO users (username, password, enabled, email, created_date) VALUES ('admin2', '$2y$10$Te.lNeGaDiRAUAXXpBbR7.3s9vi/K8tKGNe2EvfuEjMOquXAbR/Uy', true, 'hendrikheuvelman@icloud.com2','2023-01-03');
INSERT INTO users (username, password, enabled, email, created_date) VALUES ('someotheruser3', '$2y$10$b7KTevAvbO8kxrV9b6rEH.6iz49k4TFpE1lB4H97BZmlsdByswmPK', true, 'hendrikheuvelman@icloud.com3','2023-01-03');
INSERT INTO users (username, password, enabled, email, created_date) VALUES ('someotheruser4', '$2y$10$b7KTevAvbO8kxrV9b6rEH.6iz49k4TFpE1lB4H97BZmlsdByswmPK', true, 'hendrikheuvelman@icloud.com4','2023-01-03');
INSERT INTO users (username, password, enabled, email, created_date) VALUES ('someotheruser5', '$2y$10$b7KTevAvbO8kxrV9b6rEH.6iz49k4TFpE1lB4H97BZmlsdByswmPK', true, 'hendrikheuvelman@icloud.com5','2023-01-03');
INSERT INTO users (username, password, enabled, email, created_date) VALUES ('someotheruser6', '$2y$10$b7KTevAvbO8kxrV9b6rEH.6iz49k4TFpE1lB4H97BZmlsdByswmPK', true, 'hendrikheuvelman@icloud.com6','2023-01-03');
INSERT INTO users (username, password, enabled, email, created_date) VALUES ('someotheruser7', '$2y$10$b7KTevAvbO8kxrV9b6rEH.6iz49k4TFpE1lB4H97BZmlsdByswmPK', true, 'hendrikheuvelman@icloud.com7','2023-01-03');
INSERT INTO users (username, password, enabled, email, created_date) VALUES ('someotheruser8', '$2y$10$b7KTevAvbO8kxrV9b6rEH.6iz49k4TFpE1lB4H97BZmlsdByswmPK', true, 'hendrikheuvelman@icloud.com8','2023-01-03');
INSERT INTO users (username, password, enabled, email, created_date) VALUES ('someotheruser9', '$2y$10$b7KTevAvbO8kxrV9b6rEH.6iz49k4TFpE1lB4H97BZmlsdByswmPK', true, 'hendrikheuvelman@icloud.com9','2023-01-03');
INSERT INTO users (username, password, enabled, email, created_date) VALUES ('someotheruser10', '$2y$10$b7KTevAvbO8kxrV9b6rEH.6iz49k4TFpE1lB4H97BZmlsdByswmPK', true, 'hendrikheuvelman@icloud.com10','2023-01-03');
INSERT INTO users (username, password, enabled, email, created_date) VALUES ('someotheruser11', '$2y$10$b7KTevAvbO8kxrV9b6rEH.6iz49k4TFpE1lB4H97BZmlsdByswmPK', true, 'hendrikheuvelman@icloud.com11','2023-01-03');
INSERT INTO users (username, password, enabled, email, created_date) VALUES ('someotheruser12', '$2y$10$b7KTevAvbO8kxrV9b6rEH.6iz49k4TFpE1lB4H97BZmlsdByswmPK', true, 'hendrikheuvelman@icloud.com12','2023-01-03');
INSERT INTO users (username, password, enabled, email, created_date) VALUES ('someotheruser13', '$2y$10$b7KTevAvbO8kxrV9b6rEH.6iz49k4TFpE1lB4H97BZmlsdByswmPK', true, 'hendrikheuvelman@icloud.com13','2023-01-03');

INSERT INTO demo (demo_id, created_date, title, length, bpm) VALUES (1001, '2023-01-01', 'Prime Audio', 260, 123.0);
INSERT INTO demo (demo_id, created_date, title, length, bpm) VALUES (1002, '2023-01-02', 'Dance Monkey', 180, 120.0);
INSERT INTO demo (demo_id, created_date, title, length, bpm) VALUES (1003, '2023-01-02', 'Dubstep Wonderland', 180, 120.0);
INSERT INTO demo (demo_id, created_date, title, length, bpm) VALUES (1004, '2023-01-03', 'some other demo 1', 300, 120.0);
INSERT INTO demo (demo_id, created_date, title, length, bpm) VALUES (1005, '2023-01-03', 'some other demo 2', 300, 120.0);
INSERT INTO demo (demo_id, created_date, title, length, bpm) VALUES (1006, '2023-01-03', 'some other demo 3', 300, 120.0);
INSERT INTO demo (demo_id, created_date, title, length, bpm) VALUES (1007, '2023-01-03', 'some other demo 4', 300, 120.0);
INSERT INTO demo (demo_id, created_date, title, length, bpm) VALUES (1008, '2023-01-03', 'some other demo 5', 300, 120.0);
INSERT INTO demo (demo_id, created_date, title, length, bpm) VALUES (1009, '2023-01-03', 'some other demo 6', 300, 120.0);
INSERT INTO demo (demo_id, created_date, title, length, bpm) VALUES (1010, '2023-01-03', 'some other demo 7', 300, 120.0);
INSERT INTO demo (demo_id, created_date, title, length, bpm) VALUES (1011, '2023-01-03', 'some other demo 8', 300, 120.0);
INSERT INTO demo (demo_id, created_date, title, length, bpm) VALUES (1012, '2023-01-03', 'some other demo 9', 300, 120.0);
INSERT INTO demo (demo_id, created_date, title, length, bpm) VALUES (1013, '2023-01-03', 'some other demo 10', 300, 120.0);
INSERT INTO demo (demo_id, created_date, title, length, bpm) VALUES (1014, '2023-01-03', 'some other demo 11', 300, 120.0);
INSERT INTO demo (demo_id, created_date, title, length, bpm) VALUES (1015, '2023-01-03', 'some other demo 12', 300, 120.0);
INSERT INTO demo (demo_id, created_date, title, length, bpm) VALUES (1016, '2023-01-03', 'some other demo 13', 300, 120.0);

INSERT INTO conversation (conversation_id, has_demo, subject, created_date, latest_reply_date, body, read_by_correspondent, read_by_initiator) VALUES (2001, true, 'RE: Prime Audio', '2023-01-11', '2023-01-14','body1' , false, false);
INSERT INTO conversation (conversation_id, has_demo, subject, created_date, latest_reply_date, body, read_by_correspondent, read_by_initiator) VALUES (2002, true, 'RE: Dance Monkey', '2023-01-12', '2023-01-15','body2' , false , false);
INSERT INTO conversation (conversation_id, has_demo, subject, created_date, latest_reply_date, body, read_by_correspondent, read_by_initiator) VALUES (2003, true, 'RE: Dubstep Wonderland', '2023-01-13', '2023-01-16','body3' , false, false);
INSERT INTO conversation (conversation_id, has_demo, subject, created_date, latest_reply_date, body, read_by_correspondent, read_by_initiator) VALUES (2004, false, 'No demo attached 1', '2023-01-14', '2023-01-16','anotherbody1' , false, false);
INSERT INTO conversation (conversation_id, has_demo, subject, created_date, latest_reply_date, body, read_by_correspondent, read_by_initiator) VALUES (2005, false, 'No demo attached 2', '2023-01-15', '2023-01-16','anotherbody2' , false, false);
INSERT INTO conversation (conversation_id, has_demo, subject, created_date, latest_reply_date, body, read_by_correspondent, read_by_initiator) VALUES (2006, true, 'anothersubject3', '2023-01-13', '2023-01-16','anotherbody3' , false, false);
INSERT INTO conversation (conversation_id, has_demo, subject, created_date, latest_reply_date, body, read_by_correspondent, read_by_initiator) VALUES (2007, true, 'anothersubject4', '2023-01-13', '2023-01-16','anotherbody4' , false, false);
INSERT INTO conversation (conversation_id, has_demo, subject, created_date, latest_reply_date, body, read_by_correspondent, read_by_initiator) VALUES (2008, true, 'anothersubject5', '2023-01-13', '2023-01-16','anotherbody5' , false, false);
INSERT INTO conversation (conversation_id, has_demo, subject, created_date, latest_reply_date, body, read_by_correspondent, read_by_initiator) VALUES (2009, true, 'anothersubject6', '2023-01-13', '2023-01-16','anotherbody6' , false, false);
INSERT INTO conversation (conversation_id, has_demo, subject, created_date, latest_reply_date, body, read_by_correspondent, read_by_initiator) VALUES (2010, true, 'anothersubject7', '2023-01-13', '2023-01-16','anotherbody7' , false, false);
INSERT INTO conversation (conversation_id, has_demo, subject, created_date, latest_reply_date, body, read_by_correspondent, read_by_initiator) VALUES (2011, true, 'anothersubject8', '2023-01-13', '2023-01-16','anotherbody8' , false, false);
INSERT INTO conversation (conversation_id, has_demo, subject, created_date, latest_reply_date, body, read_by_correspondent, read_by_initiator) VALUES (2012, true, 'anothersubject9', '2023-01-13', '2023-01-16','anotherbody9' , false, false);
INSERT INTO conversation (conversation_id, has_demo, subject, created_date, latest_reply_date, body, read_by_correspondent, read_by_initiator) VALUES (2013, true, 'anothersubject10', '2023-01-13', '2023-01-16','anotherbody10' , false, false);
INSERT INTO conversation (conversation_id, has_demo, subject, created_date, latest_reply_date, body, read_by_correspondent, read_by_initiator) VALUES (2014, true, 'anothersubject11', '2023-01-13', '2023-01-16','anotherbody11' , false, false);
INSERT INTO conversation (conversation_id, has_demo, subject, created_date, latest_reply_date, body, read_by_correspondent, read_by_initiator) VALUES (2015, true, 'anothersubject12', '2023-01-13', '2023-01-16','anotherbody12' , false, false);
INSERT INTO conversation (conversation_id, has_demo, subject, created_date, latest_reply_date, body, read_by_correspondent, read_by_initiator) VALUES (2016, true, 'anothersubject13', '2023-01-13', '2023-01-16','anotherbody13' , false, false);

INSERT INTO audio_file (audio_file_id, created_date, original_file_name) VALUES (3001, '2023-01-01' ,'PrimeAudio.mp3');
INSERT INTO audio_file (audio_file_id, created_date, original_file_name) VALUES (3002, '2023-01-01' ,'DanceMonkey.mp3');
INSERT INTO audio_file (audio_file_id, created_date, original_file_name) VALUES (3003, '2023-01-01' ,'DubstepWonderland.mp3');
INSERT INTO audio_file (audio_file_id, created_date, original_file_name) VALUES (3004, '2023-01-01' ,'DanceMonkey2.mp3');
INSERT INTO audio_file (audio_file_id, created_date, original_file_name) VALUES (3005, '2023-01-01' ,'DanceMonkey3.mp3');
INSERT INTO audio_file (audio_file_id, created_date, original_file_name) VALUES (3006, '2023-01-01' ,'DanceMonkey4.mp3');
INSERT INTO audio_file (audio_file_id, created_date, original_file_name) VALUES (3007, '2023-01-01' ,'DanceMonkey5.mp3');
INSERT INTO audio_file (audio_file_id, created_date, original_file_name) VALUES (3008, '2023-01-01' ,'DanceMonkey6.mp3');
INSERT INTO audio_file (audio_file_id, created_date, original_file_name) VALUES (3009, '2023-01-01' ,'DanceMonkey7.mp3');
INSERT INTO audio_file (audio_file_id, created_date, original_file_name) VALUES (3010, '2023-01-01' ,'DanceMonkey8.mp3');
INSERT INTO audio_file (audio_file_id, created_date, original_file_name) VALUES (3011, '2023-01-01' ,'DanceMonkey9.mp3');
INSERT INTO audio_file (audio_file_id, created_date, original_file_name) VALUES (3012, '2023-01-01' ,'DanceMonkey10.mp3');
INSERT INTO audio_file (audio_file_id, created_date, original_file_name) VALUES (3013, '2023-01-01' ,'DanceMonkey11.mp3');
INSERT INTO audio_file (audio_file_id, created_date, original_file_name) VALUES (3014, '2023-01-01' ,'DanceMonkey12.mp3');
INSERT INTO audio_file (audio_file_id, created_date, original_file_name) VALUES (3015, '2023-01-01' ,'DanceMonkey13.mp3');
INSERT INTO audio_file (audio_file_id, created_date, original_file_name) VALUES (3016, '2023-01-01' ,'DanceMonkey14.mp3');

INSERT INTO genre (name) VALUES ('Alternative dance');
INSERT INTO genre (name) VALUES ('Dance');
INSERT INTO genre (name) VALUES ('Blues');
INSERT INTO genre (name) VALUES ('Country');
INSERT INTO genre (name) VALUES ('Rock');
INSERT INTO genre (name) VALUES ('Hard Rock');
INSERT INTO genre (name) VALUES ('Metal');
INSERT INTO genre (name) VALUES ('Classical');
INSERT INTO genre (name) VALUES ('Pop');
INSERT INTO genre (name) VALUES ('Hip-hop');
INSERT INTO genre (name) VALUES ('Jazz');
INSERT INTO genre (name) VALUES ('R & B');
INSERT INTO genre (name) VALUES ('Soul');
INSERT INTO genre (name) VALUES ('Punk');
INSERT INTO genre (name) VALUES ('Folk');
INSERT INTO genre (name) VALUES ('Easy Listening');
INSERT INTO genre (name) VALUES ('Underground');
INSERT INTO genre (name) VALUES ('Alternative');
INSERT INTO genre (name) VALUES ('Dub-Step');
INSERT INTO genre (name) VALUES ('Other');
INSERT INTO genre (name) VALUES ('Unknown');


INSERT INTO demos_users_favorites_ (demo_id, user_id) VALUES (1002, 'gerard');


INSERT INTO user_report (user_report_id, created_date, type, subject, body) VALUES (4001, '2023-01-01', 'user', 'About user "gerard"', 'This person frequently sells tracks that he does not even have the rights to.' );
INSERT INTO user_report (user_report_id, created_date, type, subject, body) VALUES (4002, '2023-01-01', 'demo', 'About demo "Dubstep Wonderland"', 'This track is not owned by the user offering it.');
INSERT INTO user_report (user_report_id, created_date, type, subject, body) VALUES (4003, '2023-01-01', 'conversation', 'About conversation "RE Dubstep Wonderland"', 'User "gerard" was extremely rude to me!!');

UPDATE user_report
SET  reporter_name = 'hendrik', reported_user_name = 'gerard'
WHERE user_report_id = 4001;

UPDATE user_report
SET  reporter_name = 'hendrik', reported_demo_id = 1003
WHERE user_report_id = 4002;

UPDATE user_report
SET  reporter_name = 'hendrik', reported_conversation_id = 2003
WHERE user_report_id = 4003;


UPDATE demo
SET producer = 'gerard'
WHERE demo_id = 1001;

UPDATE demo
SET producer = 'admin'
WHERE demo_id = 1002;

UPDATE demo
SET producer = 'gerard'
WHERE demo_id = 1003;

UPDATE demo
SET producer = 'aalke'
WHERE demo_id = 1004;

UPDATE demo
SET producer = 'admin2'
WHERE demo_id = 1005;

UPDATE demo
SET producer = 'someotheruser3'
WHERE demo_id = 1006;

UPDATE demo
SET producer = 'someotheruser4'
WHERE demo_id = 1007;

UPDATE demo
SET producer = 'someotheruser5'
WHERE demo_id = 1008;

UPDATE demo
SET producer = 'someotheruser6'
WHERE demo_id = 1009;

UPDATE demo
SET producer = 'someotheruser7'
WHERE demo_id = 1010;

UPDATE demo
SET producer = 'someotheruser8'
WHERE demo_id = 1011;

UPDATE demo
SET producer = 'someotheruser9'
WHERE demo_id = 1012;

UPDATE demo
SET producer = 'someotheruser10'
WHERE demo_id = 1013;

UPDATE demo
SET producer = 'someotheruser11'
WHERE demo_id = 1014;

UPDATE demo
SET producer = 'someotheruser12'
WHERE demo_id = 1015;

UPDATE demo
SET producer = 'someotheruser13'
WHERE demo_id = 1016;

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
SET audio_file = 3004
WHERE demo_id = 1004;

UPDATE demo
SET audio_file = 3005
WHERE demo_id = 1005;

UPDATE demo
SET audio_file = 3006
WHERE demo_id = 1006;

UPDATE demo
SET audio_file = 3007
WHERE demo_id = 1007;

UPDATE demo
SET audio_file = 3008
WHERE demo_id = 1008;

UPDATE demo
SET audio_file = 3009
WHERE demo_id = 1009;

UPDATE demo
SET audio_file = 3010
WHERE demo_id = 1010;

UPDATE demo
SET audio_file = 3011
WHERE demo_id = 1011;

UPDATE demo
SET audio_file = 3012
WHERE demo_id = 1012;

UPDATE demo
SET audio_file = 3013
WHERE demo_id = 1013;

UPDATE demo
SET audio_file = 3014
WHERE demo_id = 1014;

UPDATE demo
SET audio_file = 3015
WHERE demo_id = 1015;

UPDATE demo
SET audio_file = 3016
WHERE demo_id = 1016;

UPDATE demo
SET genre = 'Alternative dance'
WHERE demo_id = 1001;

UPDATE demo
SET genre = 'Dance'
WHERE demo_id = 1002;

UPDATE demo
SET genre = 'Dub-Step'
WHERE demo_id = 1003;

UPDATE demo
SET genre = 'Dance'
WHERE demo_id = 1004;

UPDATE demo
SET genre = 'Dance'
WHERE demo_id = 1005;

UPDATE demo
SET genre = 'Dance'
WHERE demo_id = 1006;

UPDATE demo
SET genre = 'Dance'
WHERE demo_id = 1007;

UPDATE demo
SET genre = 'Dance'
WHERE demo_id = 1008;

UPDATE demo
SET genre = 'Dance'
WHERE demo_id = 1009;

UPDATE demo
SET genre = 'Dance'
WHERE demo_id = 1010;

UPDATE demo
SET genre = 'Dance'
WHERE demo_id = 1011;

UPDATE demo
SET genre = 'Dance'
WHERE demo_id = 1012;

UPDATE demo
SET genre = 'Dance'
WHERE demo_id = 1013;

UPDATE demo
SET genre = 'Dance'
WHERE demo_id = 1014;

UPDATE demo
SET genre = 'Dance'
WHERE demo_id = 1015;

UPDATE demo
SET genre = 'Dance'
WHERE demo_id = 1016;

UPDATE conversation
SET demo_id = 1001, correspondent = 'gerard', initiator = 'aalke'
WHERE conversation_id = 2001;

UPDATE conversation
SET demo_id = 1002, correspondent = 'admin', initiator = 'gerard'
WHERE conversation_id = 2002;

UPDATE conversation
SET demo_id = 1003, correspondent = 'gerard', initiator = 'admin'
WHERE conversation_id = 2003;

UPDATE conversation
SET demo_id = 1004, correspondent = 'gerard', initiator = 'admin'
WHERE conversation_id = 2004;

UPDATE conversation
SET demo_id = 1005, correspondent = 'hendrik', initiator = 'admin'
WHERE conversation_id = 2005;

UPDATE conversation
SET demo_id = 1006, correspondent = 'gerard', initiator = 'admin'
WHERE conversation_id = 2006;

UPDATE conversation
SET demo_id = 1007, correspondent = 'gerard', initiator = 'admin'
WHERE conversation_id = 2007;

UPDATE conversation
SET demo_id = 1008, correspondent = 'gerard', initiator = 'admin'
WHERE conversation_id = 2008;

UPDATE conversation
SET demo_id = 1009, correspondent = 'gerard', initiator = 'admin'
WHERE conversation_id = 2009;

UPDATE conversation
SET demo_id = 1010, correspondent = 'gerard', initiator = 'admin'
WHERE conversation_id = 2010;

UPDATE conversation
SET demo_id = 1011, correspondent = 'gerard', initiator = 'admin'
WHERE conversation_id = 2011;

UPDATE conversation
SET demo_id = 1012, correspondent = 'gerard', initiator = 'admin'
WHERE conversation_id = 2012;

UPDATE conversation
SET demo_id = 1013, correspondent = 'gerard', initiator = 'admin'
WHERE conversation_id = 2013;

UPDATE conversation
SET demo_id = 1014, correspondent = 'gerard', initiator = 'admin'
WHERE conversation_id = 2014;

UPDATE conversation
SET demo_id = 1015, correspondent = 'gerard', initiator = 'admin'
WHERE conversation_id = 2015;

UPDATE conversation
SET demo_id = 1016, correspondent = 'gerard', initiator = 'admin'
WHERE conversation_id = 2016;