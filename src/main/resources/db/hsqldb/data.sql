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
INSERT INTO jugadores(id, first_name,last_name,username)
VALUES(4,'Yeyo','Peyeyo','seraguoro');
INSERT INTO jugadores(id, first_name,last_name,username)
VALUES(5,'Francisco Jesús','Montero','framonmar');
INSERT INTO jugadores(id, first_name,last_name,username)
VALUES(6,'José Miguel','Iborra','josibocon');



INSERT INTO lista_amigos(id_jugador1,id_jugador2) VALUES (1,2);
INSERT INTO lista_amigos(id_jugador1,id_jugador2) VALUES (2,3);

INSERT INTO achievements(id,name,metrics,threshold,description,visibility,difficulty) VALUES
	(1, 'Viciado', 'JUGAR_PARTIDAS', 10.0, 'Si juegas <THRESHOLD> partidas o más, consideramos que ya estás enganchado', 'PUBLICADO', 'BRONCE'),
	(2, 'Triunfador', 'GANAR_PARTIDAS', 20.0, 'Si ganas <THRESHOLD> o más partidas es que eres todo un triunfador', 'PUBLICADO', 'PLATA');


INSERT INTO matches(id, id_jugador1, id_jugador2, es_privada, inicio_de_partida, fin_de_partida, ganador_de_partida, turn)
VALUES(1, 1, 2, true, '1999-04-12 10:12:12.55', '1999-04-12 10:12:12.55', 'UNDEFINED', 0);

INSERT INTO disco VALUES(1,4,0,0,0,1,1);
INSERT INTO disco VALUES(2,1,3,0,0,0,1);
INSERT INTO disco VALUES(3,2,1,0,0,0,1);
INSERT INTO disco VALUES(4,0,3,0,1,0,1);
INSERT INTO disco VALUES(5,0,0,0,0,0,1);
INSERT INTO disco VALUES(6,2,0,0,0,0,1);
INSERT INTO disco VALUES(7,2,4,0,0,0,1);

INSERT INTO matches(id, id_jugador1, id_jugador2, es_privada, inicio_de_partida, fin_de_partida, ganador_de_partida, turn)
VALUES(2, 3, 4, true, '1999-04-12 10:12:12.55', '1999-04-12 10:12:12.55', 'UNDEFINED', 0);



INSERT INTO matches(id, id_jugador1, id_jugador2, es_privada, inicio_de_partida, fin_de_partida, ganador_de_partida, turn)
VALUES(3, 1, 2, false, '2000-05-17 15:12:07.55', '2000-05-17 15:20:16.43', 'FIRST_PLAYER', 0);

INSERT INTO disco VALUES(8,1,0,0,0,0,2);
INSERT INTO disco VALUES(9,0,0,0,0,0,2);
INSERT INTO disco VALUES(10,0,0,0,0,0,2);
INSERT INTO disco VALUES(11,0,0,0,0,0,2);
INSERT INTO disco VALUES(12,0,0,0,0,0,2);
INSERT INTO disco VALUES(13,0,0,0,0,0,2);
INSERT INTO disco VALUES(14,0,1,0,0,0,2);

INSERT INTO achievements_players VALUES (1,1);
INSERT INTO achievements_players VALUES (1,5);
INSERT INTO achievements_players VALUES (2,5);

