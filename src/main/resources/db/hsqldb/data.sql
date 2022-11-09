-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,email,enabled) VALUES ('admin1','4dm1n', 'admin1@gmail.com',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (1,'admin1','admin');
-- One owner user, named owner1 with passwor 0wn3r
INSERT INTO users(username,password,email,enabled) VALUES ('davdancab','davdancab','davdancab@gmail.com',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (4,'davdancab','jugador');
INSERT INTO users(username,password,email,enabled) VALUES ('manortgar','petclinic','manortgar@gmail.com',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (5,'manortgar','jugador');
INSERT INTO users(username,password,email,enabled) VALUES ('seraguoro','seraguoro','seraguoro@gmail.com',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (6,'seraguoro','jugador');
INSERT INTO users(username,password,email,enabled) VALUES ('juamarher','juamarher','juamarher@gmail.com',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (7,'juamarher','jugador');
INSERT INTO users(username,password,email,enabled) VALUES ('framonmar','framonmar','framonmar@gmail.com',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (8,'framonmar','jugador');
INSERT INTO users(username,password,email,enabled) VALUES ('josibocon','josibocon','josibocon@gmail.com',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (9,'josibocon','jugador');



INSERT INTO jugadores(id, first_name,last_name,username) 
VALUES(1,'David','Dana','davdancab');
INSERT INTO jugadores(id, first_name,last_name,username) 
VALUES(2,'Juan Jesus','Martin','juamarher');
INSERT INTO jugadores(id, first_name,last_name,username) 
VALUES(3,'Manuel','Ortega','manortgar');

INSERT INTO lista_amigos(id_jugador1,id_jugador2) VALUES (1,2);
INSERT INTO lista_amigos(id_jugador1,id_jugador2) VALUES (2,3);

