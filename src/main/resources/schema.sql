create database desafio3;
use desafio3;

create table usuario(
id integer primary key auto_increment,
login varchar(255),
senha varchar(255),
role varchar(8),
reset_token varchar(255),
validade_token timestamp
);

create table produto(
id integer primary key auto_increment,
nome varchar(100),
descricao varchar(600),
preco numeric (20,2),
estoque integer,
ativo tinyint
);

create table pedido(
id integer primary key auto_increment,
usuario_id integer,
data timestamp,
status varchar(20),
total numeric(20,2),
foreign key (usuario_id) references usuario(id)
);

create table item_Pedido(
id integer primary key auto_increment,
pedido_id integer,
foreign key (pedido_id) references pedido(id),
produto_id integer,
foreign key (produto_id) references produto(id),
quantidade integer
);