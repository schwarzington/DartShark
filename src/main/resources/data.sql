CREATE TABLE IF NOT EXISTS player (id INT NOT NULL, email VARCHAR(50) NOT NULL, password VARCHAR(200) NOT NULL, first_name VARCHAR(50) NOT NULL, last_name VARCHAR(50) NOT NULL);

TRUNCATE TABLE player;

insert into player(id,email,password,first_name,last_name) values (0,'eschwarzbeck','password','Erich','Schwarzbeck');
insert into player(id,email,password,first_name,last_name) values (1,'ewinterton','password','Eric','Winterton');
insert into player(id,email,password,first_name,last_name) values (2,'jsullivan','password','Jack','Sullivan');
insert into player(id,email,password,first_name,last_name) values (3,'chayek','password','Chris','Hayek');
insert into player(id,email,password,first_name,last_name) values (4,'jheaton','password','Jeff','Heaton');

update player set password = '$2a$10$FBAKClV1zBIOOC9XMXf3AO8RoGXYVYsfvUdoLxGkd/BnXEn4tqT3u';
