create table community
(
	id int auto_increment,
	name varchar(50) null,
	account_id varchar(50) null,
	token varchar(100) null,
	gmt_create bigint null,
	gmt_modified bigint null,
	constraint community_pk
		primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;