create table notification
(
	id bigint auto_increment,
	notifier bigint not null,
	receiver bigint not null,
	outer_id bigint not null,
	type int null,
	status int default 0 not null,
	gmt_create bigint not null,
	constraint notification_pk
		primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;