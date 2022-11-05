-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (1,'admin1','admin');
-- One owner user, named owner1 with passwor 0wn3r
INSERT INTO users(username,password,enabled) VALUES ('owner1','0wn3r',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (2,'owner1','owner');
INSERT INTO users(username,password,enabled) VALUES ('davdancab','davdancab',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (4,'davdancab','jugador');
INSERT INTO users(username,password,enabled) VALUES ('manortgar','petclinic',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (5,'manortgar','jugador');
INSERT INTO users(username,password,enabled) VALUES ('seraguoro','seraguoro',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (6,'seraguoro','jugador');
INSERT INTO users(username,password,enabled) VALUES ('juamarher','juamarher',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (7,'juamarher','jugador');
INSERT INTO users(username,password,enabled) VALUES ('framonmar','framonmar',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (8,'framonmar','jugador');
INSERT INTO users(username,password,enabled) VALUES ('josibocon','josibocon',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (9,'josibocon','jugador');

-- One vet user, named vet1 with passwor v3t
INSERT INTO users(username,password,enabled) VALUES ('vet1','v3t',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (3,'vet1','veterinarian');

INSERT INTO vets(id, first_name,last_name) VALUES (1, 'James', 'Carter');
INSERT INTO vets(id, first_name,last_name) VALUES (2, 'Helen', 'Leary');
INSERT INTO vets(id, first_name,last_name) VALUES (3, 'Linda', 'Douglas');
INSERT INTO vets(id, first_name,last_name) VALUES (4, 'Rafael', 'Ortega');
INSERT INTO vets(id, first_name,last_name) VALUES (5, 'Henry', 'Stevens');
INSERT INTO vets(id, first_name,last_name) VALUES (6, 'Sharon', 'Jenkins');

INSERT INTO specialties VALUES (1, 'radiology');
INSERT INTO specialties VALUES (2, 'surgery');
INSERT INTO specialties VALUES (3, 'dentistry');

INSERT INTO vet_specialties VALUES (2, 1);
INSERT INTO vet_specialties VALUES (3, 2);
INSERT INTO vet_specialties VALUES (3, 3);
INSERT INTO vet_specialties VALUES (4, 2);
INSERT INTO vet_specialties VALUES (5, 1);

INSERT INTO types VALUES (1, 'cat');
INSERT INTO types VALUES (2, 'dog');
INSERT INTO types VALUES (3, 'lizard');
INSERT INTO types VALUES (4, 'snake');
INSERT INTO types VALUES (5, 'bird');
INSERT INTO types VALUES (6, 'hamster');
INSERT INTO types VALUES (7, 'turtle');

INSERT INTO owners VALUES (1, 'George', 'Franklin', '110 W. Liberty St.', 'Madison', '6085551023', 'owner1');
INSERT INTO owners VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'Sun Prairie', '6085551749', 'owner1');
INSERT INTO owners VALUES (3, 'Eduardo', 'Rodriquez', '2693 Commerce St.', 'McFarland', '6085558763', 'owner1');
INSERT INTO owners VALUES (4, 'Harold', 'Davis', '563 Friendly St.', 'Windsor', '6085553198', 'owner1');
INSERT INTO owners VALUES (5, 'Peter', 'McTavish', '2387 S. Fair Way', 'Madison', '6085552765', 'owner1');
INSERT INTO owners VALUES (6, 'Jean', 'Coleman', '105 N. Lake St.', 'Monona', '6085552654', 'owner1');
INSERT INTO owners VALUES (7, 'Jeff', 'Black', '1450 Oak Blvd.', 'Monona', '6085555387', 'owner1');
INSERT INTO owners VALUES (8, 'Maria', 'Escobito', '345 Maple St.', 'Madison', '6085557683', 'owner1');
INSERT INTO owners VALUES (12, 'Manuel', 'Ortega', 'plaza pueblo cortijo 7', 'Camas', '620401349', 'manortgar');
INSERT INTO owners VALUES (9, 'David', 'Schroeder', '2749 Blackhawk Trail', 'Madison', '6085559435', 'owner1');
INSERT INTO owners VALUES (10, 'Carlos', 'Estaban', '2335 Independence La.', 'Waunakee', '6085555487', 'owner1');

INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (1, 'Leo', '2010-09-07', 1, 1);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (2, 'Basil', '2012-08-06', 6, 2);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (3, 'Rosy', '2011-04-17', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (4, 'Jewel', '2010-03-07', 2, 3);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (5, 'Iggy', '2010-11-30', 3, 4);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (6, 'George', '2010-01-20', 4, 5);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (7, 'Samantha', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (8, 'Max', '2012-09-04', 1, 6);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (9, 'Lucky', '2011-08-06', 5, 7);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (10, 'Mulligan', '2007-02-24', 2, 8);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (11, 'Freddy', '2010-03-09', 5, 9);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (15, 'Noa', '2006-11-16', 1, 12);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (12, 'Lucky', '2010-06-24', 2, 10);
INSERT INTO pets(id,name,birth_date,type_id,owner_id) VALUES (13, 'Sly', '2012-06-08', 1, 10);

INSERT INTO visits(id,pet_id,visit_date,description) VALUES (1, 7, '2013-01-01', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (2, 8, '2013-01-02', 'rabies shot');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (3, 8, '2013-01-03', 'neutered');
INSERT INTO visits(id,pet_id,visit_date,description) VALUES (4, 7, '2013-01-04', 'spayed');

INSERT INTO jugadores(id, first_name,last_name,username,estado_Online,numero_De_Contaminacion,numero_de_bacterias,numero_de_sarcinas)
VALUES(1,'David','Dana','davdancab',true,0,9,3);
INSERT INTO jugadores(id, first_name,last_name,username,estado_Online,numero_De_Contaminacion,numero_de_bacterias,numero_de_sarcinas)
VALUES(2,'Juan Jesus','Martin','juamarher',false,0,9,3);
INSERT INTO jugadores(id, first_name,last_name,username,estado_Online,numero_De_Contaminacion,numero_de_bacterias,numero_de_sarcinas)
VALUES(3,'Manuel','Ortega','manortgar',true,0,20,4);
INSERT INTO jugadores(id, first_name,last_name,username,estado_Online,numero_De_Contaminacion,numero_de_bacterias,numero_de_sarcinas)
VALUES(4,'Yeyo','Peyeyo','seraguoro',true,0,20,4);

INSERT INTO lista_amigos(id_jugador1,id_jugador2) VALUES (1,2);

INSERT INTO achievement(id,name,threshold,description,badge_image) VALUES
	(1, 'Viciado', 10.0, 'Si juegas <THRESHOLD> partidas o más, consideramos que ya estás enganchado',
	'https://bit.ly/certifiedGamer'),
	(2, 'Triunfador', 20.0, 'Si ganas <THRESHOLD> o más partidas es que eres todo un triunfador',
	'https://bit.ly/proGamer');


INSERT INTO matches(id, id_jugador1, id_jugador2, es_privada, inicio_de_partida, fin_de_partida, ganador_de_partida, turn)
VALUES(1, 1, 2, true, '1999-04-12 10:12:12.55', '1999-04-12 10:12:12.55', 'UNDEFINED', 5);

INSERT INTO disco VALUES(1,4,0,0,0,1,1);
INSERT INTO disco VALUES(2,1,3,0,0,0,1);
INSERT INTO disco VALUES(3,2,1,0,0,0,1);
INSERT INTO disco VALUES(4,0,3,0,1,0,1);
INSERT INTO disco VALUES(5,0,0,0,0,0,1);
INSERT INTO disco VALUES(6,2,0,0,0,0,1);
INSERT INTO disco VALUES(7,2,4,0,0,0,1);

INSERT INTO matches(id, id_jugador1, id_jugador2, es_privada, inicio_de_partida, fin_de_partida, ganador_de_partida, turn)
VALUES(2, 3, 4, true, '1999-04-12 10:12:12.55', '1999-04-12 10:12:12.55', 'UNDEFINED', 0);

INSERT INTO disco VALUES(8,1,0,0,0,0,2);
INSERT INTO disco VALUES(9,0,0,0,0,0,2);
INSERT INTO disco VALUES(10,0,0,0,0,0,2);
INSERT INTO disco VALUES(11,0,0,0,0,0,2);
INSERT INTO disco VALUES(12,0,0,0,0,0,2);
INSERT INTO disco VALUES(13,0,0,0,0,0,2);
INSERT INTO disco VALUES(14,0,1,0,0,0,2);
