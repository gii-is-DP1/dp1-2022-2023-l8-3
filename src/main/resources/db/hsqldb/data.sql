-- Usuarios con sus respectivos roles
INSERT INTO users(id,username,password,email,enabled) VALUES (1,'admin1','4dm1n', 'admin1@gmail.com',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (1,1,'admin');
INSERT INTO users(id,username,password,email,enabled) VALUES (2,'davdancab','davdancab','davdancab@gmail.com',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (2,2,'jugador');
INSERT INTO users(id,username,password,email,enabled) VALUES (3,'manortgar','petclinic','manortgar@gmail.com',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (3,3,'jugador');
INSERT INTO users(id,username,password,email,enabled) VALUES (4,'seraguoro','seraguoro','seraguoro@gmail.com',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (4,4,'jugador');
INSERT INTO users(id,username,password,email,enabled) VALUES (5,'juamarher','juamarher','juamarher@gmail.com',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (5,5,'jugador');
INSERT INTO users(id,username,password,email,enabled) VALUES (6,'framonmar','framonmar','framonmar@gmail.com',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (6,6,'jugador');
INSERT INTO users(id,username,password,email,enabled) VALUES (7,'josibocon','josibocon','josibocon@gmail.com',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (7,7,'jugador');

--Jugadores
INSERT INTO jugadores(id, first_name,last_name,username,estado_Online) 
VALUES(1,'David','Dana',2,false);
INSERT INTO jugadores(id, first_name,last_name,username,estado_Online) 
VALUES(2,'Juan Jesus','Martin',5,false);
INSERT INTO jugadores(id, first_name,last_name,username,estado_Online) 
VALUES(3,'Manuel','Ortega',3,false);
INSERT INTO jugadores(id, first_name,last_name,username,estado_Online)
VALUES(4,'Yeyo','Peyeyo',4,false);
INSERT INTO jugadores(id, first_name,last_name,username,estado_Online)
VALUES(5,'Francisco Jesús','Montero',6,false);
INSERT INTO jugadores(id, first_name,last_name,username,estado_Online)
VALUES(6,'José Miguel','Iborra',7,false);

--Solicitudes de amistad
INSERT INTO friend_request(id, resultado, jugador1_id, jugador2_id)
VALUES (1, true, 1, 2);
INSERT INTO friend_request(id, resultado, jugador1_id, jugador2_id)
VALUES (2, true, 3, 1);
INSERT INTO friend_request(id, resultado, jugador1_id, jugador2_id)
VALUES (3, false, 4, 5);

--Logros
INSERT INTO achievements(id,name,metrics,threshold,description,visibility,difficulty) VALUES
	(1, 'Viciado', 'JUGAR_PARTIDAS', 10.0, 'Si juegas <THRESHOLD> partidas o más, consideramos que ya estás enganchado', 'PUBLICADO', 'BRONCE'),
	(2, 'Triunfador', 'GANAR_PARTIDAS', 20.0, 'Si ganas <THRESHOLD> o más partidas es que eres todo un triunfador', 'PUBLICADO', 'PLATA');
INSERT INTO achievements_players VALUES (1,1);
INSERT INTO achievements_players VALUES (1,5);
INSERT INTO achievements_players VALUES (2,5);

--Partidas con sus respectivos discos
INSERT INTO matches(id, name, id_jugador1, id_jugador2, abandonada, es_privada, inicio_de_partida, fin_de_partida, ganador_de_partida, contamination_number_of_player_1,
contamination_number_of_player_2, number_of_bacteria_of_player_1, number_of_bacteria_of_player_2, number_of_sarcina_of_player_1, number_of_sarcina_of_player_2, turn)
VALUES(1, 'Primera partida', 1, 2, false, true, '1999-04-12 10:12:12.55', '1999-04-12 10:14:12.32', 'UNDEFINED', 0, 0, 20, 20, 4, 4, 0);
INSERT INTO disco(id,num_bacteria_j1,num_bacteria_j2,num_sarcina_j1,num_sarcina_j2,num_movimientos_j1,num_movimientos_j2,id_match) VALUES(1,4,0,0,0,0,0,1);
INSERT INTO disco(id,num_bacteria_j1,num_bacteria_j2,num_sarcina_j1,num_sarcina_j2,num_movimientos_j1,num_movimientos_j2,id_match) VALUES(2,1,3,0,0,0,0,1);
INSERT INTO disco(id,num_bacteria_j1,num_bacteria_j2,num_sarcina_j1,num_sarcina_j2,num_movimientos_j1,num_movimientos_j2,id_match) VALUES(3,2,1,0,0,0,0,1);
INSERT INTO disco(id,num_bacteria_j1,num_bacteria_j2,num_sarcina_j1,num_sarcina_j2,num_movimientos_j1,num_movimientos_j2,id_match) VALUES(4,0,3,0,0,0,1,1);
INSERT INTO disco(id,num_bacteria_j1,num_bacteria_j2,num_sarcina_j1,num_sarcina_j2,num_movimientos_j1,num_movimientos_j2,id_match) VALUES(5,0,0,0,0,0,0,1);
INSERT INTO disco(id,num_bacteria_j1,num_bacteria_j2,num_sarcina_j1,num_sarcina_j2,num_movimientos_j1,num_movimientos_j2,id_match) VALUES(6,2,0,0,0,0,0,1);
INSERT INTO disco(id,num_bacteria_j1,num_bacteria_j2,num_sarcina_j1,num_sarcina_j2,num_movimientos_j1,num_movimientos_j2,id_match) VALUES(7,2,4,0,0,0,0,1);

INSERT INTO matches(id, name, id_jugador1, id_jugador2, abandonada, es_privada, inicio_de_partida, fin_de_partida, ganador_de_partida, contamination_number_of_player_1,
contamination_number_of_player_2, number_of_bacteria_of_player_1, number_of_bacteria_of_player_2, number_of_sarcina_of_player_1, number_of_sarcina_of_player_2, turn)
VALUES(2, 'Segunda partida', 3, 4, false, true, '1999-04-12 10:12:12.55', '1999-04-12 11:12:12.55', 'FIRST_PLAYER', 0, 0, 20, 20, 4, 4, 0);
INSERT INTO disco(id,num_bacteria_j1,num_bacteria_j2,num_sarcina_j1,num_sarcina_j2,num_movimientos_j1,num_movimientos_j2,id_match) VALUES(8,1,0,0,0,3,2,2);
INSERT INTO disco(id,num_bacteria_j1,num_bacteria_j2,num_sarcina_j1,num_sarcina_j2,num_movimientos_j1,num_movimientos_j2,id_match) VALUES(9,0,0,0,0,0,0,2);
INSERT INTO disco(id,num_bacteria_j1,num_bacteria_j2,num_sarcina_j1,num_sarcina_j2,num_movimientos_j1,num_movimientos_j2,id_match) VALUES(10,0,0,0,0,4,3,2);
INSERT INTO disco(id,num_bacteria_j1,num_bacteria_j2,num_sarcina_j1,num_sarcina_j2,num_movimientos_j1,num_movimientos_j2,id_match) VALUES(11,0,0,0,0,0,0,2);
INSERT INTO disco(id,num_bacteria_j1,num_bacteria_j2,num_sarcina_j1,num_sarcina_j2,num_movimientos_j1,num_movimientos_j2,id_match) VALUES(12,0,0,0,0,0,0,2);
INSERT INTO disco(id,num_bacteria_j1,num_bacteria_j2,num_sarcina_j1,num_sarcina_j2,num_movimientos_j1,num_movimientos_j2,id_match) VALUES(13,0,0,0,0,0,0,2);
INSERT INTO disco(id,num_bacteria_j1,num_bacteria_j2,num_sarcina_j1,num_sarcina_j2,num_movimientos_j1,num_movimientos_j2,id_match) VALUES(14,0,1,0,0,0,0,2);

INSERT INTO matches(id, name, id_jugador1, id_jugador2, abandonada, es_privada, inicio_de_partida, fin_de_partida, ganador_de_partida, contamination_number_of_player_1,
contamination_number_of_player_2, number_of_bacteria_of_player_1, number_of_bacteria_of_player_2, number_of_sarcina_of_player_1, number_of_sarcina_of_player_2, turn)
VALUES(3, 'Tercera partida', 1, 2, false, false, '2000-05-17 15:12:07.55', '2000-05-17 15:20:16.43', 'FIRST_PLAYER', 0, 0, 20, 20, 4, 4, 0);

--Comentarios
INSERT INTO comentarios(id, match_id, texto, id_jugador, fecha_de_publicacion) 
VALUES (1, 1, 'Texto1ashdalsukhdaslkduhasldkuhsdlksauhdlaskudhsalkduhsadlksuahduskadh', 1, '1999-04-12 10:12:12.55');
INSERT INTO comentarios(id, match_id, texto, id_jugador, fecha_de_publicacion) 
VALUES (2, 1, 'Texto2dsfsdfdsfdsfdsfdsf', 1, '1999-04-12 10:13:12.55');
INSERT INTO comentarios(id, match_id, texto, id_jugador, fecha_de_publicacion) 
VALUES (3, 1, 'Texto3sdfsadfdsfsdfsloifhsdifuhsdfiusdhfiksdufhsdifusydhfliudshfdslifuydshfduhf', 2, '1999-04-12 10:14:12.55');
INSERT INTO comentarios(id, match_id, texto, id_jugador, fecha_de_publicacion) 
VALUES (4, 1, 'Texto4sfsdfsdafsdfsdfsdfsadfsdfsdfsdf', 2, '1999-04-12 10:15:12.55');
INSERT INTO comentarios(id, match_id, texto, id_jugador, fecha_de_publicacion) 
VALUES (5, 1, 'Texto5sdfsdfdsfsdfsdfsdfdsf', 2, '1999-04-12 10:16:12.55');
